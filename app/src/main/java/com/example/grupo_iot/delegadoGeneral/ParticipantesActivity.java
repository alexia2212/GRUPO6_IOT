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
import com.example.grupo_iot.databinding.ActivityMenuActividadesEventoBinding;
import com.example.grupo_iot.delegadoGeneral.adapter.EventoAdapter;
import com.example.grupo_iot.delegadoGeneral.adapter.ParticipantesAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.delegadoGeneral.entity.Evento;
import com.example.grupo_iot.delegadoGeneral.entity.Participantes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ParticipantesActivity extends AppCompatActivity {
    ActivityMenuActividadesEventoBinding binding;
    private ParticipantesAdapter participantesAdapter;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String nombre;
    private List<Participantes> participantesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuActividadesEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        participantesList = new ArrayList<>();
        RecyclerView recyclerView2 = findViewById(R.id.recyclerViewParticipantes);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 2);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(participantesAdapter);

        Intent intent = getIntent();
        nombre = null;
        Log.e("ParticipantesActivity", "nombre Actividad Transfiriendose: " + nombre);
        if (intent.hasExtra("listaData1")) {
            Actividad selectedLista1 = (Actividad) intent.getSerializableExtra("listaData1");
            nombre = selectedLista1.getNombreActividad();
        }

        String nombreActi = nombre;
        Log.e("ParticipantesActivity", "nombre Actividad Final: " + nombreActi);

        cargarListaEventos();
    }
    public void cargarListaEventos(){
        CollectionReference actividadesCollection = db.collection("actividades");
        DocumentReference actividadDocument = actividadesCollection.document(nombre);
        CollectionReference listaEventosCollection = actividadDocument.collection("listaEventos");

        listaEventosCollection
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot evento : task.getResult()) {
                            String eventId = evento.getId();
                            Log.e("ParticipantesActivity", "nombre Evento: " + eventId);
                            cargarListaParticipantes(nombre, eventId);
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
        Log.e("ParticipantesActivity", "actividad: " + nombreActividad + " evento: " + nombreEvento);

        integrantesCollection
                .get()
                .addOnCompleteListener(task -> {
                    //Log.e("ParticipantesActivity", "Referencia a integrantesCollection1: " + integrantesCollection.getPath());
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot participantes : task.getResult()) {
                            Participantes p = participantes.toObject(Participantes.class);
                            participantesList.add(p);
                            Log.e("ParticipantesActivity", "nombre participante: " + p.getNombre());
                            }

                    }
                    ParticipantesAdapter participantesAdapter1 = new ParticipantesAdapter();
                    participantesAdapter1.setParticipantesList(participantesList);
                    participantesAdapter1.setContext(ParticipantesActivity.this);
                    binding.recyclerViewParticipantes.setAdapter(participantesAdapter1);
                    binding.recyclerViewParticipantes.setLayoutManager(new LinearLayoutManager(ParticipantesActivity.this));

                })
                .addOnFailureListener(e -> {
                    Log.e("ParticipantesActivity", "Error al obtener datos del documento", e);
                });
    }
}
