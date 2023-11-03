package com.example.grupo_iot.delactividad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityActualizarBinding;
import com.example.grupo_iot.databinding.ActivityNuevoEventoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NuevoEvento extends AppCompatActivity {

    ActivityNuevoEventoBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevoEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        binding.guardarnuevoevento.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                guardarNuevoEvento();
            }
        });

    }

    private void guardarNuevoEvento() {
        // Obtén los valores de los campos de entrada
        String titulo = binding.titulonuevoevento.getText().toString();
        String descripcion = binding.descripcionuevoevento.getText().toString();
        String fecha = binding.fechanuevoevento.getText().toString();
        String lugar = binding.lugarnuevoevento.getText().toString();
        String foto = binding.fotonuevoevento.getText().toString();

        // Crea un nuevo objeto para representar el evento
        EventoList evento = new EventoList(titulo, fecha, foto, descripcion, lugar);

        // Accede a la colección "listaeventos" y agrega un nuevo documento con los datos del evento
        db.collection("listaeventos")
                .add(evento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // El evento se ha guardado con éxito
                        Toast.makeText(NuevoEvento.this, "Evento guardado con éxito", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Hubo un error al guardar el evento
                        Toast.makeText(NuevoEvento.this, "Error al guardar el evento", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}