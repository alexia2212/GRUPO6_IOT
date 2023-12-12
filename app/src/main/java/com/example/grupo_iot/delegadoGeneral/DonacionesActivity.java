package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityEstadisticasDonacionBinding;
import com.example.grupo_iot.delegadoGeneral.adapter.DonacionesAdapter;
import com.example.grupo_iot.delegadoGeneral.adapter.ValidacionesAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.Donaciones;
import com.example.grupo_iot.delegadoGeneral.entity.Validaciones;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DonacionesActivity extends AppCompatActivity {
    ActivityEstadisticasDonacionBinding binding;
    private DonacionesAdapter donacionesAdapter;

    FirebaseFirestore db;
    FirebaseAuth auth;

    private List<Donaciones> donacionesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEstadisticasDonacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        donacionesList = new ArrayList<>();
        donacionesAdapter = new DonacionesAdapter();
        RecyclerView recyclerView = findViewById(R.id.donaciones);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(donacionesAdapter);

        db.collection("donaciones")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    donacionesList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Donaciones donaciones = document.toObject(Donaciones.class);
                        String monto = donaciones.getMonto();
                        String condicion = donaciones.getCondicion();

                        donacionesList.add(new Donaciones(monto, condicion));
                    }

                    // Asigna la lista de actividades al adaptador después de que se haya cargado de Firebase
                    donacionesAdapter.setDonacionesList(donacionesList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Donaciones", "Error en validacion", e);
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
