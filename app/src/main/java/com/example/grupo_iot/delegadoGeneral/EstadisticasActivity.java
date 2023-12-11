package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.grupo_iot.databinding.ActivityMenuDelegadoGeneralBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_iot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.grupo_iot.databinding.ActivityEstadisticasCondicionBinding;

public class EstadisticasActivity extends AppCompatActivity {
    AnyChartView anyChartView;
    ActivityEstadisticasCondicionBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String[] months = {"January", "February","March", "April"};
    int[] salary = {16000,20000,30000,50000};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEstadisticasCondicionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        anyChartView = findViewById(R.id.anyChartView);
        db.collection("credenciales")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DataEntry> dataEntries = new ArrayList<>();
                    Log.e("FirestoreURL", "True");
                    // Contadores para las condiciones
                    int estudiantesCount = 0;
                    int egresadosCount = 0;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Verificar si el campo 'condicion' existe en el documento
                        if (document.contains("condicion")) {
                            String condicion = document.getString("condicion");
                            Log.e("EstadisticasActivity", ": " + condicion);

                            if ("Estudiante".equals(condicion)) {
                                estudiantesCount++;
                            } else if ("Egresado".equals(condicion)) {
                                egresadosCount++;
                            }
                        }
                    }

                    // Agregar datos al gráfico
                    dataEntries.add(new ValueDataEntry("Estudiantes", estudiantesCount));
                    dataEntries.add(new ValueDataEntry("Egresados", egresadosCount));

                    // Configurar el gráfico con los datos
                    setupChartView(dataEntries);
                })
                .addOnFailureListener(e -> {
                    // Manejar errores de lectura de Firestore
                    Toast.makeText(EstadisticasActivity.this, "Error al obtener datos de Firestore", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupChartView(List<DataEntry> dataEntries) {
        Pie pie = AnyChart.pie();
        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }
}











