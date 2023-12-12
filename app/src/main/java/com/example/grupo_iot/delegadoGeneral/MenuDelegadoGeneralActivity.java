package com.example.grupo_iot.delegadoGeneral;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.grupo_iot.LoginActivity;
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