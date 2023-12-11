package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.grupo_iot.databinding.ActivityVistaPreviaCreacionBinding;
import com.squareup.picasso.Picasso;

public class VistaPreviaCreacion extends AppCompatActivity {

    ActivityVistaPreviaCreacionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaPreviaCreacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Recuperar datos del intent
        Intent intent = getIntent();
        if (intent != null) {
            String titulo = intent.getStringExtra("titulo");
            String descripcion = intent.getStringExtra("descripcion");
            String fecha = intent.getStringExtra("fecha");
            String lugar = intent.getStringExtra("lugar");
            String imageUrl = intent.getStringExtra("imageUrl");

            // Actualizar elementos de la interfaz de usuario
            binding.titulo.setText(titulo);
            binding.descripcion.setText(descripcion);
            binding.fecha.setText(fecha);
            binding.ubicacion.setText(lugar);

            // Cargar la imagen con Picasso
            Picasso.get().load(imageUrl).into(binding.imagen1);
        }
    }
}
