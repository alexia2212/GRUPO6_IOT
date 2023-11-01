package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityVistaPreviaCreacionBinding;
import com.example.grupo_iot.databinding.ActivityVistaPreviaEventoBinding;

import java.util.ArrayList;
import java.util.List;

public class VistaPreviaEvento extends AppCompatActivity {

    ActivityVistaPreviaEventoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaPreviaEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageViewsalir.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setTitle("Aviso")
                    .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        startActivity(intent1);
                    })
                    .setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();

        });

        List<VistaPrevia> dataList = new ArrayList<>();
        dataList.add(new VistaPrevia("Convocatoria", "15/09/2023", R.drawable.voley1, "Necesitamos gente que tenga experiencia, ganas y dedicación. Este es el comienzo de la elección para poder ganar Semana de Ingeniería", "Cancha de minas"));

        RecyclerView recyclerView = findViewById(R.id.vistaprevia);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //
        AdaptadorVistaPrevia adapter = new AdaptadorVistaPrevia(dataList);
        recyclerView.setAdapter(adapter);

        ImageView addImage = findViewById(R.id.imageView21);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la nueva actividad aquí
                Intent intent = new Intent(VistaPreviaEvento.this, NuevoEvento.class);
                startActivity(intent);
            }
        });















    }
}