package com.example.grupo_iot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.grupo_iot.databinding.ActivityOlvidoContrasenaBinding;
import com.google.android.material.textfield.TextInputEditText;

public class OlvidoContrasenaActivity extends AppCompatActivity {

    ActivityOlvidoContrasenaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOlvidoContrasenaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String correoIngresado = ((TextInputEditText) binding.inputCorreo.getEditText()).getText().toString();
        binding.buttonEmail.setOnClickListener(view -> {
            if(correoIngresado.isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Debe ingresar un correo para recuperar su contraseña.")
                        .setTitle("Aviso")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Se ha envíado un mail a "+ correoIngresado+". Revíselo para que pueda acceder nuevamente a la app.")
                        .setTitle("Aviso")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}