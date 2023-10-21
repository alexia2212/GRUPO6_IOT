package com.example.grupo_iot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.grupo_iot.databinding.ActivityRegistroBinding;
import com.google.android.material.textfield.TextInputEditText;

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

        binding.guardar.setOnClickListener(view -> {

            String nombre = ((TextInputEditText) binding.inputNombre.getEditText()).getText().toString();
            String apellido = ((TextInputEditText) binding.inputApellido.getEditText()).getText().toString();
            String codigo = ((TextInputEditText) binding.inputCodigo.getEditText()).getText().toString();
            String email = ((TextInputEditText) binding.inputEmail.getEditText()).getText().toString();
            String password = ((TextInputEditText) binding.inputPass.getEditText()).getText().toString();
            CheckBox checkBox = binding.checkBoxTerminos;

            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || codigo.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Todos los campos deben estar completos.")
                        .setTitle("Aviso")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (!checkBox.isChecked()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Debe aceptar los términos y condiciones para continuar.")
                        .setTitle("Aviso")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Intent intent = new Intent(RegistrarseActivity.this, ConfirmacionRegistroActivity.class);
                startActivity(intent);
            }
        });
    }
}