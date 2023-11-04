package com.example.grupo_iot.delegadoGeneral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.activity.ListaActividadesActivity;
import com.example.grupo_iot.databinding.ActivityListaActividadesAlumnoBinding;
import com.example.grupo_iot.delactividad.Lista;
import com.example.grupo_iot.delegadoGeneral.adapter.ListaActividadesAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.databinding.ActivityMenuActividadesBinding;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActividadesActivity extends AppCompatActivity {

    ActivityMenuActividadesBinding binding;
    private ListaActividadesAdapter listaActividadesAdapter;

    FirebaseFirestore db;

    private List<Actividad> actividadLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuActividadesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        // Inicializa el adaptador después de que la lista de actividades se haya cargado de Firebase
        listaActividadesAdapter = new ListaActividadesAdapter(new ArrayList<>());
        RecyclerView recyclerView = findViewById(R.id.actividadesDel);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listaActividadesAdapter);

        db.collection("actividadesG")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    actividadLista.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Actividad activ = document.toObject(Actividad.class);
                        String nombre = activ.getNombreActividad();
                        String descripcion = activ.getDescripcionActividad();
                        String delegado = activ.getDelegadoActividad();

                        actividadLista.add(new Actividad(nombre, descripcion, delegado));
                    }

                    // Asigna la lista de actividades al adaptador después de que se haya cargado de Firebase
                    listaActividadesAdapter.setActividadLista(actividadLista);
                })
                .addOnFailureListener(e -> {
                    Log.e("ActividadesActivity", "Error al obtener los eventos", e);
                });
    }
}

