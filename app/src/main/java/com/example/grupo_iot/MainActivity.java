package com.example.grupo_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.grupo_iot.alumno.MenuEventosActivity;
import com.example.grupo_iot.databinding.ActivityMainBinding;
import com.example.grupo_iot.delactividad.VistaPreviaEvento;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

    public void irMenuAlumno(View view){
        Intent intent = new Intent(this, MenuEventosActivity.class);
        startActivity(intent);
    }

}