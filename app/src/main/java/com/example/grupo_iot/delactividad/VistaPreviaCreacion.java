package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityVistaPreviaCreacionBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class VistaPreviaCreacion extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth auth;

    ActivityVistaPreviaCreacionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        binding = ActivityVistaPreviaCreacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        generarBottomNavigationMenu();

        // Recuperar datos del intent
        Intent intent = getIntent();
        if (intent != null) {
            String titulo = intent.getStringExtra("nombre");
            String descripcion = intent.getStringExtra("descripcion");
            String fecha = intent.getStringExtra("fechaHora");
            String lugar = intent.getStringExtra("lugar");
            String imageUrl = intent.getStringExtra("imageUrl");

            // Actualizar elementos de la interfaz de usuario
            binding.titulo.setText(titulo);
            binding.descripcion.setText(descripcion);
            binding.fecha.setText(fecha);
            binding.ubicacion.setText(lugar);

            // Cargar la imagen con Picasso
            Picasso.get().load(imageUrl).into(binding.imagen1);
        }
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
                    Intent intent = new Intent(VistaPreviaCreacion.this, Delactprincipal.class);
                    startActivity(intent);

                }

                if(menuItem.getItemId()==R.id.navigation_eventos_finalizados){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(VistaPreviaCreacion.this, EventoFinalizadoActivity.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(VistaPreviaCreacion.this, Chatdelact.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(VistaPreviaCreacion.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}
