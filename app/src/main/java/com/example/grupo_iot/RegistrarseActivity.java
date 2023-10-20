package com.example.grupo_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.grupo_iot.databinding.ActivityRegistroBinding;

public class RegistrarseActivity extends AppCompatActivity {

    ActivityRegistroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.register.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrarseActivity.class);
            startActivity(intent);
        });

        binding.iniciosesion.setOnClickListener(view -> {
            Intent intent = new Intent(this, IniciarSesionActivity.class);
            startActivity(intent);
        });



    }
}