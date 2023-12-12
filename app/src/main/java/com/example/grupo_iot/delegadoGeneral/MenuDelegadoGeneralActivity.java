package com.example.grupo_iot.delegadoGeneral;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityIniciarSesionBinding;
import com.example.grupo_iot.databinding.ActivityMenuDelegadoGeneralBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MenuDelegadoGeneralActivity extends AppCompatActivity {

    ActivityMenuDelegadoGeneralBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuDelegadoGeneralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
    }

    public void actividades(View view){
        Intent intent = new Intent(this, ActividadesActivity.class);
        startActivity(intent);
    }

    public void estadisticas(View view){
        Intent intent=new Intent(this, EstadisticasPrincipalActivity.class);
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