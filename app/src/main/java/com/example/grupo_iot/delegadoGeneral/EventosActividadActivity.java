package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityActividadEventosBinding;
import com.example.grupo_iot.delegadoGeneral.adapter.ListaEventosActividadAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.delegadoGeneral.entity.DataHolder;
import com.example.grupo_iot.delegadoGeneral.entity.Evento2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventosActividadActivity extends AppCompatActivity {

    ActivityActividadEventosBinding binding;
    private ListaEventosActividadAdapter listaEventosActividadAdapter;
    FirebaseAuth auth;
    FirebaseFirestore db;
    private List<Evento2> evento2List;
    String nombreActividad;
    String idActividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActividadEventosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        evento2List = new ArrayList<>();
        listaEventosActividadAdapter = new ListaEventosActividadAdapter(evento2List);
        RecyclerView recyclerView = findViewById(R.id.eventosActividad);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listaEventosActividadAdapter);
        Intent intent = getIntent();
        if (intent.hasExtra("nombreActividad") && intent.hasExtra("idActividad")) {
            nombreActividad = intent.getStringExtra("nombreActividad");
            idActividad = intent.getStringExtra("idActividad");
            Log.e("EventoActividadActivity", "Nombre de la actividad recibido: " + nombreActividad);
            Log.e("EventoActividadActivity", "Id de la actividad recibido: " + idActividad);
            TextView nombreTextView = findViewById(R.id.nombreActividadXEvento);
            nombreTextView.setText(nombreActividad);
        }
        CollectionReference actividadesCollection = db.collection("actividades");
        DocumentReference actividadDocument = actividadesCollection.document(idActividad);
        CollectionReference listaEventosCollection = actividadDocument.collection("listaEventos");
        listaEventosCollection
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    evento2List.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Evento2 evento2 = document.toObject(Evento2.class);
                        String nombre = evento2.getNombre();
                        String descripcion = evento2.getDescripcion();
                        String lugar = evento2.getLugar();
                        String imagen = evento2.getImagen();
                        Date fechaHora = evento2.getFechaHora();
                        evento2List.add(new Evento2(nombre, descripcion, fechaHora, lugar, imagen));
                    }
                    listaEventosActividadAdapter.setEventoList(evento2List);
                })
                .addOnFailureListener(e -> {
                    Log.e("ActividadesActivity", "Error al obtener los eventos", e);
                });
    }

}
