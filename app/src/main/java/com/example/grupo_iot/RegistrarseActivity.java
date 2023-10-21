package com.example.grupo_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.grupo_iot.databinding.ActivityRegistroBinding;

public class RegistrarseActivity extends AppCompatActivity {

    ActivityRegistroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.guardar.setOnClickListener(view -> {
            Intent intent = new Intent(this, ConfirmacionRegistroActivity.class);
            startActivity(intent);
        });

        //SECCION CONDICION USUARIO
        String[] listaOpciones = {"Condición de Usuario", "Alumno", "Egresado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner_condicion_usuario, listaOpciones);
        Spinner spinner = binding.spinnerCondicionUsuario;
        spinner.setAdapter(adapter);

    }
}