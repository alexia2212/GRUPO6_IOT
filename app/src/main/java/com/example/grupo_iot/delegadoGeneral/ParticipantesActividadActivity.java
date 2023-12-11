package com.example.grupo_iot.delegadoGeneral;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityActividadParticipantesBinding;
import com.example.grupo_iot.delegadoGeneral.adapter.ListaParticipantesActividadAdapter;
import com.example.grupo_iot.delegadoGeneral.adapter.ParticipantesAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.Participantes;
import com.example.grupo_iot.delegadoGeneral.entity.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ParticipantesActividadActivity extends AppCompatActivity  {
    ActivityActividadParticipantesBinding binding;
    ListaParticipantesActividadAdapter listaParticipantesActividadAdapter;
    FirebaseAuth auth;
    FirebaseFirestore db;
    private List<Usuarios> usuariosList;
    String nombreActividad;
    String idActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActividadParticipantesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usuariosList = new ArrayList<>();
        listaParticipantesActividadAdapter = new ListaParticipantesActividadAdapter(usuariosList);
        RecyclerView recyclerView = findViewById(R.id.participantesActividad);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listaParticipantesActividadAdapter);
        Intent intent = getIntent();
        if (intent.hasExtra("nombreActividad") && intent.hasExtra("idActividad")) {
            nombreActividad = intent.getStringExtra("nombreActividad");
            idActividad = intent.getStringExtra("idActividad");
            Log.e("EventoActividadActivity", "Nombre de la actividad recibido: " + nombreActividad);
            Log.e("EventoActividadActivity", "Id de la actividad recibido: " + idActividad);
            TextView nombreTextView = findViewById(R.id.nombreActividadXParticipante);
            nombreTextView.setText(nombreActividad);
        }
        CollectionReference actividadesCollection = db.collection("actividades");
        DocumentReference actividadDocument = actividadesCollection.document(idActividad);
        CollectionReference listaEventosCollection = actividadDocument.collection("listaEventos");
        listaEventosCollection
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot evento : task.getResult()) {
                            String eventId = evento.getId();
                            Log.e("ParticipantesActividadActivity", "nombre Evento: " + eventId);
                            cargarListaParticipantes(idActividad, eventId);
                        }
                    }
                });
    }
    public void cargarListaParticipantes(String nombreActividad, String nombreEvento) {
        CollectionReference actividadesCollection = db.collection("actividades");
        DocumentReference actividadDocument = actividadesCollection.document(nombreActividad);
        CollectionReference listaEventosCollection = actividadDocument.collection("listaEventos");
        DocumentReference eventoDocument = listaEventosCollection.document(nombreEvento);
        CollectionReference integrantesCollection = eventoDocument.collection("integrantes");
        Log.e("ParticipantesActividadActivity", "actividad: " + nombreActividad + " evento: " + nombreEvento);
        integrantesCollection
                .get()
                .addOnCompleteListener(task -> {
                    //Log.e("ParticipantesActivity", "Referencia a integrantesCollection1: " + integrantesCollection.getPath());
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot usuarios : task.getResult()) {
                            Usuarios u = usuarios.toObject(Usuarios.class);
                            usuariosList.add(u);
                            Log.e("ParticipantesActividadActivity", "nombre participante: " + u.getNombre());
                        }
                    }
                    ListaParticipantesActividadAdapter listaParticipantesActividadAdapter = new ListaParticipantesActividadAdapter(usuariosList);
                    listaParticipantesActividadAdapter.setUsuariosList(usuariosList);
                    listaParticipantesActividadAdapter.setContext(ParticipantesActividadActivity.this);
                    binding.participantesActividad.setAdapter(listaParticipantesActividadAdapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("ParticipantesActividadActivity", "Error al obtener datos del documento", e);
                });
    }
}
