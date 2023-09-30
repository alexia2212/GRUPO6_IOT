package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.grupo_iot.R;

import java.util.ArrayList;
import java.util.List;

public class EventoFinalizadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_finalizado);

        List<VistaFinalizadoLista> dataList = new ArrayList<>();
        dataList.add(new VistaFinalizadoLista("1er partido", "15/09/2023", R.drawable.voley3, "Necesitamos gente que tenga experiencia, ganas y dedicación. Este es el comienzo de la elección para poder ganar Semana de Ingeniería", "Cancha de minas"));

        RecyclerView recyclerView = findViewById(R.id.vistapreviadfinalizado);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdaptadorVistaFinalizadoLista adapter = new AdaptadorVistaFinalizadoLista(dataList);
        recyclerView.setAdapter(adapter);
    }
}