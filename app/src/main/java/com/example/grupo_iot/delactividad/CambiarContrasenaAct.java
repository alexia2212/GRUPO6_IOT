package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.activity.CambiarContrasenaActivity;
import com.example.grupo_iot.alumno.activity.ChatGrupalActivity;
import com.example.grupo_iot.alumno.activity.DonacionesActivity;
import com.example.grupo_iot.alumno.activity.ListaActividadesActivity;
import com.example.grupo_iot.alumno.activity.ListaDeChatsActivity;
import com.example.grupo_iot.alumno.activity.ListaEventosApoyadosActivity;
import com.example.grupo_iot.alumno.activity.PerfilActivity;
import com.example.grupo_iot.alumno.entity.Evento;
import com.example.grupo_iot.databinding.ActivityCambiarContrasenaBinding;
import com.example.grupo_iot.databinding.ActivityCompartirfotosBinding;
import com.example.grupo_iot.databinding.ActivityDelactprincipalBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class CambiarContrasenaAct extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth auth;

    ActivityCambiarContrasenaBinding binding;

    private Adaptador adapter;
    private List<Lista> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCambiarContrasenaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        dataList = new ArrayList<>();
        adapter = new Adaptador(dataList);


        generarBottomNavigationMenu();



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
                    Intent intent = new Intent(CambiarContrasenaAct.this, Delactprincipal.class);
                    startActivity(intent);

                }

                if(menuItem.getItemId()==R.id.navigation_eventos_finalizados){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(CambiarContrasenaAct.this, EventoFinalizadoActivity.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(CambiarContrasenaAct.this, Chatdelact.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(CambiarContrasenaAct.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}