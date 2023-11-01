package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityUsuariosInscritosBinding;
import com.example.grupo_iot.databinding.ActivityVistaPreviaCreacionBinding;
import com.example.grupo_iot.databinding.ActivityVistaPreviaEventoBinding;

import java.util.ArrayList;
import java.util.List;

public class VistaPreviaCreacion extends AppCompatActivity {

    ActivityVistaPreviaCreacionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaPreviaCreacionBinding.inflate(getLayoutInflater());
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

        List<VistaPreviaCreacionLista> dataList = new ArrayList<>();
        dataList.add(new VistaPreviaCreacionLista("Convocatoria", "15/09/2023", R.drawable.voley5, "Necesitamos gente que tenga experiencia, ganas y dedicación. Este es el comienzo de la elección para poder ganar Semana de Ingeniería", "Cancha de minas"));

        RecyclerView recyclerView = findViewById(R.id.vistapreviadcreacion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdaptadorVistaPreviaCreacion adapter = new AdaptadorVistaPreviaCreacion(dataList);
        recyclerView.setAdapter(adapter);
    }
}