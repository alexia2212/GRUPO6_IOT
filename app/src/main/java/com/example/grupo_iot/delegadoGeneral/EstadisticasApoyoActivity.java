package com.example.grupo_iot.delegadoGeneral;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityEstadisticasApoyosBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EstadisticasApoyoActivity extends AppCompatActivity {
    AnyChartView anyChartView2;
    ActivityEstadisticasApoyosBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String[] months = {"January", "February","March", "April"};
    int[] salary = {16000,20000,30000,50000};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEstadisticasApoyosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        anyChartView2 = findViewById(R.id.anyChartView2);
        db.collection("actividades")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DataEntry> dataEntries = new ArrayList<>();
                    Log.e("FirestoreURL", "True");

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Verificar si el campo 'actividadDesignada' existe en el documento
                            String actividadDesignada = document.getId();

                            // Contar la cantidad de integrantes en la colección 'listaIntegrantes'
                            db.collection("actividades")
                                    .document(actividadDesignada)
                                    .collection("integrantesActividad")
                                    .get()
                                    .addOnSuccessListener(integrantesSnapshots -> {
                                        int integrantesCount = integrantesSnapshots.size();

                                        // Agregar datos al gráfico
                                        dataEntries.add(new ValueDataEntry(actividadDesignada, integrantesCount));

                                        // Configurar el gráfico con los datos
                                        setupChartView(dataEntries);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Manejar errores de lectura de Firestore para la colección 'listaIntegrantes'
                                        Toast.makeText(EstadisticasApoyoActivity.this, "Error al obtener datos de Firestore (listaIntegrantes)", Toast.LENGTH_SHORT).show();
                                    });
                        }
                })
                .addOnFailureListener(e -> {
                    // Manejar errores de lectura de Firestore para la colección 'actividades'
                    Toast.makeText(EstadisticasApoyoActivity.this, "Error al obtener datos de Firestore (actividades)", Toast.LENGTH_SHORT).show();
                });

    }

    private void setupChartView(List<DataEntry> dataEntries) {
        Pie pie = AnyChart.pie();
        pie.data(dataEntries);
        anyChartView2.setChart(pie);
    }
}
