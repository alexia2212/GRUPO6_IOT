package com.example.grupo_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.grupo_iot.databinding.ActivityLoginBinding;
import com.example.grupo_iot.delegadoGeneral.MenuDelegadoGeneralActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnIniciarSesion.setOnClickListener(view -> {
            Intent intent = new Intent(this, IniciarSesionActivity.class);
            startActivity(intent);
        });

        binding.btnIrARegistrarse.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrarseActivity.class);
            startActivity(intent);
        });

        db = FirebaseFirestore.getInstance();
    }

}