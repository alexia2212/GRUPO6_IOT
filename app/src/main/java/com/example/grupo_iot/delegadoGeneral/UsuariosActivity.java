package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.databinding.ActivityDelactprincipalBinding;
import com.example.grupo_iot.databinding.ActivityMenuUsuariosBinding;

import com.example.grupo_iot.R;
import com.example.grupo_iot.delegadoGeneral.adapter.UsuariosAdapter;
import com.example.grupo_iot.delegadoGeneral.adapter.ValidacionesAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.Usuarios;
import com.example.grupo_iot.delegadoGeneral.entity.Validaciones;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsuariosActivity extends AppCompatActivity {
    ActivityMenuUsuariosBinding binding;
    private UsuariosAdapter usuariosAdapter;
    FirebaseFirestore db;
    FirebaseAuth auth;
    private List<Usuarios> usuariosList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuUsuariosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        usuariosList = new ArrayList<>();
        usuariosAdapter = new UsuariosAdapter(usuariosList);
        RecyclerView recyclerView = findViewById(R.id.usuariosLista);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(usuariosAdapter);

        db.collection("credenciales")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    usuariosList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String documentoId = document.getId();
                        Usuarios user = document.toObject(Usuarios.class);
                        String nombre = user.getNombre();
                        String apellido = user.getApellido();
                        String codigo = user.getCodigo();
                        String email = user.getEmail();
                        String rol = user.getRol();

                        usuariosList.add(new Usuarios(nombre, codigo, documentoId, apellido, email, rol));
                    }

                    // Asigna la lista de actividades al adaptador después de que se haya cargado de Firebase
                    usuariosAdapter.setUsuariosList(usuariosList);
                })
                .addOnFailureListener(e -> {
                    Log.e("UsuariosActivity", "Error en validacion", e);
                });
        //generarBottomNavigationMenu();

    }
    public void usuario(View view){
        Intent intent=new Intent(this, InfoUsuarioActivity.class);
        startActivity(intent);
    }
    /*void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation3);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_estadistica){
                    Intent intent = new Intent(UsuariosActivity.this, EstadisticasActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_validaciones){
                    Intent intent = new Intent(UsuariosActivity.this, ValidacionesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_usuarios){
                    Intent intent = new Intent(UsuariosActivity.this, UsuariosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_actividades){
                    Intent intent = new Intent(UsuariosActivity.this, ActividadesActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }*/
}
