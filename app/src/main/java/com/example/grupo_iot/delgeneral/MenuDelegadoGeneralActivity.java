package com.example.grupo_iot.delgeneral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_iot.databinding.ActivityMenuDelegadoGeneralBinding;
import com.example.grupo_iot.delegadoGeneral.ActividadesActivity;
import com.example.grupo_iot.delegadoGeneral.EstadisticasActivity;
import com.example.grupo_iot.delegadoGeneral.UsuariosActivity;
import com.example.grupo_iot.delegadoGeneral.ValidacionesActivity;

public class MenuDelegadoGeneralActivity extends AppCompatActivity {

    ActivityMenuDelegadoGeneralBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuDelegadoGeneralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    public void actividades(View view){
        Intent intent=new Intent(this, ActividadesActivity.class);
        startActivity(intent);
    }

    public void estadisticas(View view){
        Intent intent=new Intent(this, EstadisticasActivity.class);
        startActivity(intent);
    }
    public void usuarios(View view){
        Intent intent=new Intent(this, UsuariosActivity.class);
        startActivity(intent);
    }
    public void validaciones(View view){
        Intent intent=new Intent(this, ValidacionesActivity.class);
        startActivity(intent);
    }

}
