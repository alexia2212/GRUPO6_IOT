package com.example.grupo_iot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.grupo_iot.delactividad.Adaptador;
import com.example.grupo_iot.delactividad.Lista;

import java.util.ArrayList;
import java.util.List;

public class delactprincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delactprincipal);

        List<Lista> dataList = new ArrayList<>();
        dataList.add(new Lista("Convocatoria", "15/09/2023", R.drawable.voley1, R.drawable.baseline_favorite_24));
        dataList.add(new Lista("Entrenamiento", "19/09/2023", R.drawable.voley2, R.drawable.baseline_favorite_border_24));
        dataList.add(new Lista("1er partido", "14/10/2023", R.drawable.voley3, R.drawable.baseline_favorite_border_24));
        dataList.add(new Lista("2do partido", "16/10/2023", R.drawable.voley4, R.drawable.baseline_favorite_24));
        dataList.add(new Lista("3er partido", "18/10/2023", R.drawable.voley5, R.drawable.baseline_favorite_border_24));
        dataList.add(new Lista("4to partido", "22/10/2023", R.drawable.voley1, R.drawable.baseline_favorite_24));

        RecyclerView recyclerView = findViewById(R.id.eventos);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        Adaptador adapter = new Adaptador(dataList);
        recyclerView.setAdapter(adapter);
    }
}
