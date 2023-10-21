package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.grupo_iot.R;

import java.util.ArrayList;
import java.util.List;

public class NuevoEvento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_evento);
        List<NuevoEventoLista> dataList = new ArrayList<>();
        dataList.add(new NuevoEventoLista("Convocatoria", "15/09/2023", "20/19/2023", "Necesitamos gente que tenga experiencia, ganas y dedicación. Este es el comienzo de la elección para poder ganar Semana de Ingeniería", "Cancha de minas"));

        RecyclerView recyclerView = findViewById(R.id.eventosnuevoevento);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdaptadorNuevoEvento adapter = new AdaptadorNuevoEvento(dataList);
        recyclerView.setAdapter(adapter);
    }


}