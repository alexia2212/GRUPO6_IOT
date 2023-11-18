package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityCompartirfotosBinding;
import com.example.grupo_iot.databinding.ActivityListaDeUsuariosBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListaDeUsuarios extends AppCompatActivity {

    FirebaseAuth auth;

    FirebaseFirestore db;

    ActivityListaDeUsuariosBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaDeUsuariosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        RecyclerView recyclerView = findViewById(R.id.vistaprevia);

        List<Usuario> dataList = new ArrayList<>();
        dataList.add(new Usuario(R.drawable.stuardo3, "Stuardo Lucho", "Condición: Egresado", "Barra"));
        dataList.add(new Usuario(R.drawable.angeloramos, "Angelo Ramos", "Condición: Alumno", "Barra"));
        dataList.add(new Usuario(R.drawable.carlos, "Carlos Ayala", "Condición: Alumno", "Barra"));
        dataList.add(new Usuario(R.drawable.stefhaniejaramillo, "Stefhanie Jaramillo", "Condición: Alumno", "Barra"));

        AdaptadorUsuario adapter = new AdaptadorUsuario(dataList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }
}
