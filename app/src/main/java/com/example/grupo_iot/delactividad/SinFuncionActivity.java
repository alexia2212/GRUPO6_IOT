package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityListaDeUsuariosBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SinFuncionActivity extends AppCompatActivity {

    FirebaseAuth auth;

    FirebaseFirestore db;

    ActivityListaDeUsuariosBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaDeUsuariosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        generarBottomNavigationMenu();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.imageViewsalir.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setTitle("Aviso")
                    .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                        auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();
                    })
                    .setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();

        });



    }

    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_eventos){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(SinFuncionActivity.this, Delactprincipal.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(SinFuncionActivity.this, Chatdelact.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(SinFuncionActivity.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}