package com.example.grupo_iot.delegadoGeneral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.activity.ListaActividadesActivity;
import com.example.grupo_iot.databinding.ActivityListaActividadesAlumnoBinding;
import com.example.grupo_iot.delactividad.Chatdelact;
import com.example.grupo_iot.delactividad.Delactprincipal;
import com.example.grupo_iot.delactividad.Lista;
import com.example.grupo_iot.delactividad.NuevoEvento;
import com.example.grupo_iot.delactividad.Perfildelact;
import com.example.grupo_iot.delegadoGeneral.adapter.ListaActividadesAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.databinding.ActivityMenuActividadesBinding;
import com.example.grupo_iot.LoginActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActividadesActivity extends AppCompatActivity {

    ActivityMenuActividadesBinding binding;
    private ListaActividadesAdapter listaActividadesAdapter;

    FirebaseFirestore db;
    FirebaseAuth auth;

    private List<Actividad> actividadLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuActividadesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        actividadLista= new ArrayList<>();
        // Inicializa el adaptador después de que la lista de actividades se haya cargado de Firebase
        listaActividadesAdapter = new ListaActividadesAdapter(actividadLista);
        generarBottomNavigationMenu();
        RecyclerView recyclerView = findViewById(R.id.actividadesDel);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listaActividadesAdapter);

        db.collection("actividades")
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
        ImageView addImage = findViewById(R.id.buttonAgregar);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActividadesActivity.this, CrearActividadActivity.class);
                startActivity(intent);
            }
        });

        Button addButton = findViewById(R.id.masinfoactividad);

    }

    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation3);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_estadistica){
                    Intent intent = new Intent(ActividadesActivity.this, EstadisticasActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_validaciones){
                    Intent intent = new Intent(ActividadesActivity.this, ValidacionesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_usuarios){
                    Intent intent = new Intent(ActividadesActivity.this, UsuariosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_actividades){
                    Intent intent = new Intent(ActividadesActivity.this, ActividadesActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}

