package com.example.grupo_iot.delegadoGeneral;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChartView;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityEstadisticasDineroBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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
                .document("Estudiante")
                .collection("subcollections") // This collection references all subcollections
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<BarEntry> barEntries = new ArrayList<>();
                    Log.e("Dinero", "True");


                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String subcollectionName = document.getId(); // Get the subcollection name
                        Log.e("Dinero", "True" + subcollectionName);
                        // Get the documents within each subcollection
                        db.collection("donaciones")
                                .document("Estudiante")
                                .collection(subcollectionName)
                                .get()
                                .addOnSuccessListener(innerQueryDocumentSnapshots -> {
                                    Log.e("Dinero", "True" + subcollectionName);
                                    for (QueryDocumentSnapshot innerDocument : innerQueryDocumentSnapshots) {
                                        // Get the "monto" field and convert it to float
                                        String montoStr = innerDocument.getString("monto");
                                        if (montoStr != null) {
                                            try {
                                                float monto = Float.parseFloat(montoStr);
                                                // Create a bar entry for each document
                                                BarEntry barEntry = new BarEntry(barEntries.size() + 1, monto);
                                                barEntries.add(barEntry);
                                            } catch (NumberFormatException e) {
                                                // Handle invalid "monto" value
                                                Log.e("EstadisticasDineroActivity", "Error parsing monto: " + e.getMessage());
                                            }
                                        }
                                    }

                                    createAndDisplayBarData(barEntries);
                                })
                                .addOnFailureListener(e -> {
                                    // Handle subcollection document read errors
                                    Toast.makeText(EstadisticasDineroActivity.this, "Error al obtener datos de Firestore", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle "subcollections" collection read errors
                    Toast.makeText(EstadisticasDineroActivity.this, "Error al obtener datos de Firestore", Toast.LENGTH_SHORT).show();
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
        barChartView.getDescription().setText("Monto de Donaciones");
        barChartView.getDescription().setTextColor(Color.BLUE);
    }
}

