package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_iot.databinding.ActivityMenuEstadisticasBinding;
public class EstadisticasPrincipalActivity extends AppCompatActivity {
    ActivityMenuEstadisticasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuEstadisticasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    public void estudiante(View view){
        Intent intent = new Intent(this, EstadisticasActivity.class);
        startActivity(intent);
    }

    public void dinero(View view){
        Intent intent=new Intent(this, EstadisticasDineroActivity.class);
        startActivity(intent);
    }
    public void apoyos(View view){
        Intent intent=new Intent(this, EstadisticasApoyoActivity.class);
        startActivity(intent);
    }
}
