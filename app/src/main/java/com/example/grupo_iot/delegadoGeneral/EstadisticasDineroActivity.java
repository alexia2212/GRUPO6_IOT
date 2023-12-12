package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChartView;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityEstadisticasDineroBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EstadisticasDineroActivity extends AppCompatActivity {
    BarChart barChartView;
    ActivityEstadisticasDineroBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEstadisticasDineroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        barChartView = findViewById(R.id.barChartView);
        db.collection("donaciones")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Mapa para almacenar donaciones agrupadas por mes
                    Map<String, Float> monthlyDonations = new HashMap<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.contains("monto") && document.get("monto") instanceof String) {
                            String fechaStr = document.getId();
                            if (fechaStr != null) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm 'Hrs.'", Locale.getDefault());
                                Date fecha;
                                try {
                                    fecha = sdf.parse(fechaStr);
                                    String monthYear = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(fecha);
                                    String montoStr = document.getString("monto");
                                    try {
                                        Float monto = Float.parseFloat(montoStr);
                                        monthlyDonations.put(monthYear, monthlyDonations.getOrDefault(monthYear, 0f) + monto);
                                    } catch (NumberFormatException e) {
                                        // Manejar errores al convertir el monto a número
                                        Log.e("EstadisticasDineroActivity", "Error al convertir monto a número: " + e.getMessage());
                                    }
                                } catch (ParseException e) {
                                    // Manejar errores al parsear la fecha
                                    Log.e("EstadisticasDineroActivity", "Error parsing fecha: " + e.getMessage());
                                }
                            }
                        } else {
                            Log.e("EstadisticasDineroActivity", "El documento no contiene el campo 'monto' o no es un String");
                        }
                    }

                    List<String> monthsList = new ArrayList<>(monthlyDonations.keySet());


                    Collections.sort(monthsList, (month1, month2) -> {
                        try {
                            Date date1 = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(month1);
                            Date date2 = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(month2);

                            // Comparar date1 y date2
                            int comparisonResult = date1.compareTo(date2);

                            if (comparisonResult == 0) {
                                // Si los meses son iguales, compara el año
                                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
                                int year1 = Integer.parseInt(yearFormat.format(date1));
                                int year2 = Integer.parseInt(yearFormat.format(date2));

                                return Integer.compare(year1, year2);
                            } else {
                                return comparisonResult;
                            }
                        } catch (ParseException e) {
                            // Manejar errores al parsear la fecha
                            Log.e("EstadisticasDineroActivity", "Error parsing fecha: " + e.getMessage());
                            return 0; // Devuelve 0 en caso de error
                        }
                    });

                    // Ahora, `monthsList` contiene los meses ordenados y `monthlyDonations` contiene los montos totales agrupados por mes
                    // Continúa con la creación y visualización del gráfico
                    createAndDisplayMonthlyBarChart(monthsList, new ArrayList<>(monthlyDonations.values()));
                })
                .addOnFailureListener(e -> {
                    // Manejar errores de lectura de Firestore
                    Toast.makeText(EstadisticasDineroActivity.this, "Error al obtener datos de Firestore", Toast.LENGTH_SHORT).show();
                });

        ImageView addImage = findViewById(R.id.infoDonacion);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EstadisticasDineroActivity.this, DonacionesActivity.class);
                startActivity(intent);
            }
        });

    }
    private void createAndDisplayBarData(List<BarEntry> barEntries) {
        // Create bar data set and configure the chart
        BarDataSet barDataSet = new BarDataSet(barEntries, "Monto de Donaciones");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setDrawValues(false);

        BarData barData = new BarData(barDataSet);
        barChartView.setData(barData);
        barChartView.animateY(5000);

        // Configure chart description
    }
    private void createAndDisplayMonthlyBarChart(List<String> months, List<Float> amounts) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        // Crear entradas de gráfico para cada mes
        for (int i = months.size() - 1; i >= 0; i--) {
            BarEntry barEntry = new BarEntry(months.size() - i, amounts.get(i));
            barEntries.add(barEntry);
        }

        // Crear conjunto de datos y configurar el gráfico
        BarDataSet barDataSet = new BarDataSet(barEntries, ""); // Establecer descripción como cadena vacía
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setDrawValues(false);
        BarData barData = new BarData(barDataSet);
        barChartView.setData(barData);
        barChartView.animateY(5000);
        barChartView.getDescription().setTextColor(Color.BLUE);
        XAxis xAxis = barChartView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
    }

}

