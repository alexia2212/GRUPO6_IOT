package com.example.grupo_iot.delegadoGeneral;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grupo_iot.EmailSender;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventosActivity extends AppCompatActivity {
    ActivityMenuActividadesEventoBinding binding;
    private EventoAdapter eventoAdapter;
    private ParticipantesAdapter participantesAdapter;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String nombre;
    String delegado;
    private Actividad selectedLista;
    private Activity context;
    String descripcion;
    String id;
    String emailDele;
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
            selectedLista = (Actividad) intent.getSerializableExtra("listaData");

            // Accede a los datos recibidos
            nombre = selectedLista.getNombreActividad();
            descripcion = selectedLista.getDescripcionActividad();
            delegado = selectedLista.getDelegadoActividad();
            id = selectedLista.getId();
            emailDele = selectedLista.getEmailDelegado();
            TextView nombreTextView = findViewById(R.id.nombreActiEvento);
            TextView cdescripcionTextView = findViewById(R.id.descriActiEvento);
            TextView delegadoTextView = findViewById(R.id.nombreDelegado);

            nombreTextView.setText(nombre);
            cdescripcionTextView.setText(descripcion);
            delegadoTextView.setText(delegado);
        }
        cargarListaEventos(id);
        cargarListaIntegrantes(id);
        Button button = findViewById(R.id.buttonAsignar);
        if (delegado == null || delegado.isEmpty()) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EventosActivity.this, DelegadoActivity.class);
                    intent.putExtra("nombreActividad", id);
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
                Intent intent1 = new Intent(EventosActivity.this, EditarActivity.class);
                intent1.putExtra("listaData", selectedLista);
                startActivity(intent1);
            }
        });
        Button button3 = findViewById(R.id.btnBorrarActividad);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delegado == null || delegado.isEmpty()) {
                    confirmarEliminacion();
                } else {
                    mostrarDialogoDeAviso();
                }
            }
        });

        ImageView imageView2 = findViewById(R.id.btnverEvento);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventosActivity.this, EventosActividadActivity.class);
                intent.putExtra("nombreActividad", nombre);
                intent.putExtra("idActividad", id);
                startActivity(intent);
            }
        });
        ImageView imageView = findViewById(R.id.btnverParticipante);
    }
    private void confirmarEliminacion() {
        AlertDialog.Builder alert = new AlertDialog.Builder(EventosActivity.this);
        alert.setTitle("Confirmación");
        alert.setMessage("¿Estás seguro de que deseas eliminar esta actividad?");
        alert.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CollectionReference actividadesCollection = db.collection("actividades");
                DocumentReference actividadDocument = actividadesCollection.document(id);
                actividadDocument
                        .delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EventosActivity.this, "Actividad eliminada con éxito", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EventosActivity.this, ActividadesActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(EventosActivity.this, "Error al eliminar la actividad", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        alert.setNegativeButton("No", null);
        alert.show();

    }
    private void mostrarDialogoDeAviso() {
        AlertDialog.Builder alert = new AlertDialog.Builder(EventosActivity.this);
        alert.setTitle("Aviso");
        alert.setMessage("Hay un delegado asignado. Se enviará un correo de aviso que la actividad se eliminará en 1 minuto.");
        alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String subject = "Proxima eliminación de Actividad";
                String registroMessage = "<html><body style='font-family: sans-serif-medium; '>" +
                        "¡Seprocedera a eliminar la actividad!<br><br>" +
                        "Lamentamos anunciarle que esta actividad se eliminará en 1 minuto.<br><br>" +
                        "Saludos cordiales de parte de Tech-Bat</body></html>";

                Log.e("AsignarDelegadoActivity", "email final: " + emailDele);
                EmailSender.sendEmail(subject, emailDele, registroMessage, EventosActivity.this);
                iniciarTemporizador(10*1000);
            }
        });
        alert.setNegativeButton("Cancelar", null);
        alert.show();
    }
    private void iniciarTemporizador(long tiempoTotal) {
        new CountDownTimer(tiempoTotal, 1000) {//5 * 24 * 60 * 60 * 1000
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
               confirmarEliminacion();
                Log.d("EventosActivity", "Temporizador finalizado, delegado: " + delegado);

            }
        }.start();
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

    public void cargarListaIntegrantes(String nombreActi){
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
