package com.example.grupo_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.grupo_iot.databinding.ActivityMainBinding;
import com.example.grupo_iot.delactividad.EventoFinalizadoActivity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

    public void irMenuAlumno(View view){
        Intent intent = new Intent(this, EventoFinalizadoActivity.class);
        startActivity(intent);
    }

}