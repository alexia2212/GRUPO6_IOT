package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.databinding.ActivityMenuActividadesBinding;
import com.example.grupo_iot.delegadoGeneral.adapter.ListaActividadesAdapter;
import com.example.grupo_iot.delegadoGeneral.adapter.ValidacionesAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.delegadoGeneral.entity.Validaciones;
import com.example.grupo_iot.databinding.ActivityMenuValidacionesBinding;
import com.example.grupo_iot.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ValidacionesActivity extends AppCompatActivity {
    ActivityMenuValidacionesBinding binding;
    private ValidacionesAdapter validacionesAdapter;

    FirebaseFirestore db;
    FirebaseAuth auth;

    private List<Validaciones> validacionesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuValidacionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        validacionesList = new ArrayList<>();
        validacionesAdapter = new ValidacionesAdapter(validacionesList);
        RecyclerView recyclerView = findViewById(R.id.validacionesDel);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(validacionesAdapter);

        db.collection("usuariosPorRegistrar")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    validacionesList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String documentoId = document.getId();
                        Validaciones val = document.toObject(Validaciones.class);
                        String nombre = val.getNombre();
                        String apellido = val.getApellido();
                        String codigo = val.getCodigo();
                        String email = val.getEmail();
                        String rol = val.getRol();
                        String condicion = val.getCondicion();

                        validacionesList.add(new Validaciones(nombre, codigo, documentoId, apellido, email, rol, condicion));
                    }

                    // Asigna la lista de actividades al adaptador después de que se haya cargado de Firebase
                    validacionesAdapter.setValidacionesList(validacionesList);
                })
                .addOnFailureListener(e -> {
                    Log.e("ValidacionesActivity", "Error en validacion", e);
                });



        //Button addButton = findViewById(R.id.masinfoactividad);
        //addButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent intent = new Intent(ActividadesActivity.this, EventosActivity.class);
        //        startActivity(intent);
        //    }
        //});
       // generarBottomNavigationMenu();
    }

    public void validacion(View view){
        Intent intent=new Intent(this, ValidacionActivity.class);
        startActivity(intent);
    }
    /*void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation3);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_estadistica){
                    Intent intent = new Intent(ValidacionesActivity.this, EstadisticasActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_validaciones){
                    Intent intent = new Intent(ValidacionesActivity.this, ValidacionesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_usuarios){
                    Intent intent = new Intent(ValidacionesActivity.this, UsuariosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_actividades){
                    Intent intent = new Intent(ValidacionesActivity.this, ActividadesActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }*/
}
