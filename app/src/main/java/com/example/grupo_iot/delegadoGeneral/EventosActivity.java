package com.example.grupo_iot.delegadoGeneral;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grupo_iot.R;
import com.example.grupo_iot.delegadoGeneral.adapter.ParticipantesAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.databinding.ActivityMenuActividadesEventoBinding;
import com.example.grupo_iot.delegadoGeneral.adapter.EventoAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.DataHolder;
import com.example.grupo_iot.delegadoGeneral.entity.Evento;
import com.example.grupo_iot.delegadoGeneral.entity.Participantes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class EventosActivity extends AppCompatActivity {
    ActivityMenuActividadesEventoBinding binding;
    private EventoAdapter eventoAdapter;
    private ParticipantesAdapter participantesAdapter;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String nombre;
    String delegado;
    private List<Evento> eventoList;
    private RecyclerView.ViewHolder holder;
    private Activity context;
    String descripcion;
    String id;
    private List<Participantes> participantesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuActividadesEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEventos);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(eventoAdapter);
        participantesList = new ArrayList<>();
        RecyclerView recyclerView2 = findViewById(R.id.recyclerViewParticipantes);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 2);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(participantesAdapter);
        CollectionReference actividadesCollection = db.collection("actividades");
        id = actividadesCollection.getId();
        Log.e("EventoActivity", "Nombre id: " + id);

        Intent intent = getIntent();
        nombre = null;
        if (intent.hasExtra("listaData")) {
            Actividad selectedLista = (Actividad) intent.getSerializableExtra("listaData");

            // Accede a los datos recibidos
            nombre = selectedLista.getNombreActividad();
            descripcion = selectedLista.getDescripcionActividad();
            delegado = selectedLista.getDelegadoActividad();
            TextView nombreTextView = findViewById(R.id.nombreActiEvento);
            TextView cdescripcionTextView = findViewById(R.id.descriActiEvento);
            TextView delegadoTextView = findViewById(R.id.nombreDelegado);

            nombreTextView.setText(nombre);
            cdescripcionTextView.setText(descripcion);
            delegadoTextView.setText(delegado);
        }
        cargarListaEventos(nombre);
        cargarListaIntegrantes(nombre);
        Button button = findViewById(R.id.buttonAsignar);
        if (delegado == null || delegado.isEmpty()) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EventosActivity.this, DelegadoActivity.class);
                    intent.putExtra("nombreActividad", nombre);
                    startActivity(intent);
                }
            });
        } else {
            button.setVisibility(View.GONE); // Opcional: Puedes cambiar a INVISIBLE si prefieres mantener el espacio del botón.
        }
        Button button2 = findViewById(R.id.btnEditarActividad);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventosActivity.this, EditarActivity.class);
                startActivity(intent);
            }
        });
        Button button3 = findViewById(R.id.btnBorrarActividad);
        ImageView imageView2 = findViewById(R.id.btnverEvento);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventosActivity.this, DelegadoActivity.class);
                intent.putExtra("nombreActividad", nombre);
                startActivity(intent);
            }
        });
        ImageView imageView = findViewById(R.id.btnverParticipante);
    }

    public void cargarListaEventos(String nombreActi){
        CollectionReference actividadesCollection = db.collection("actividades");
        DocumentReference actividadDocument = actividadesCollection.document(nombreActi);
        CollectionReference listaEventosCollection = actividadDocument.collection("listaEventos");

        listaEventosCollection
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Evento> eventoList = new ArrayList<>();
                        for (QueryDocumentSnapshot evento : task.getResult()) {
                            Evento event = evento.toObject(Evento.class);
                            eventoList.add(event);


                        }
                        EventoAdapter eventosAdapter = new EventoAdapter();
                        eventosAdapter.setEventoList(eventoList);
                        eventosAdapter.setContext(EventosActivity.this);
                        eventosAdapter.setActividad(nombreActi);
                        binding.recyclerViewEventos.setAdapter(eventosAdapter);
                        binding.recyclerViewEventos.setLayoutManager(new LinearLayoutManager(EventosActivity.this));
                    }
                });
    }
    public void editarActividad(View view){
        Intent intent=new Intent(this, EditarActivity.class);
        startActivity(intent);
    }public void cargarListaIntegrantes(String nombreActi){
        CollectionReference actividadesCollection = db.collection("actividades");
        DocumentReference actividadDocument = actividadesCollection.document(nombreActi);
        CollectionReference listaEventosCollection = actividadDocument.collection("listaEventos");

        listaEventosCollection
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot evento : task.getResult()) {
                            String eventId = evento.getId();
                            Log.e("ParticipantesActivity", "nombre Evento: " + eventId);
                            cargarListaParticipantes(nombreActi, eventId);
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
                    participantesAdapter1.setContext(EventosActivity.this);
                    binding.recyclerViewParticipantes.setAdapter(participantesAdapter1);
                    binding.recyclerViewParticipantes.setLayoutManager(new LinearLayoutManager(EventosActivity.this));

                })
                .addOnFailureListener(e -> {
                    Log.e("ParticipantesActivity", "Error al obtener datos del documento", e);
                });
    }

}
