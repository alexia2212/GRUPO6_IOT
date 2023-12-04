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
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.databinding.ActivityMenuActividadesEventoBinding;
import com.example.grupo_iot.delegadoGeneral.adapter.EventoAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.Evento;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EventosActivity extends AppCompatActivity {
    ActivityMenuActividadesEventoBinding binding;
    private EventoAdapter eventoAdapter;


    FirebaseFirestore db;
    String nombre;

    private List<Evento> eventoList;
    private RecyclerView.ViewHolder holder;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuActividadesEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEventos);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(eventoAdapter);

        Intent intent = getIntent();
        nombre = null;
        Log.e("EventosActivity", "nombre Actividad Transfiriendose:" + nombre);
        if (intent.hasExtra("listaData")) {
            Actividad selectedLista = (Actividad) intent.getSerializableExtra("listaData");

            // Accede a los datos recibidos
            nombre = selectedLista.getNombreActividad();
            String descripcion = selectedLista.getDescripcionActividad();

            // Muestra los datos en la actividad VistaPreviaEvento
            TextView nombreTextView = findViewById(R.id.nombreActiEvento);
            TextView cdescripcionTextView = findViewById(R.id.descriActiEvento);

            nombreTextView.setText(nombre);
            cdescripcionTextView.setText(descripcion);
        }

        String nombreActi = nombre;
        Log.e("EventosActivity", "nombre Actividad Final:" + nombreActi);

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
                        List<Evento> eventoList = new ArrayList<>();
                        for (QueryDocumentSnapshot evento : task.getResult()) {
                            Evento event = evento.toObject(Evento.class);
                            eventoList.add(event);
                        }
                        EventoAdapter eventosAdapter = new EventoAdapter();
                        eventosAdapter.setEventoList(eventoList);
                        eventosAdapter.setContext(EventosActivity.this);
                        eventosAdapter.setActividad(nombre);
                        binding.recyclerViewEventos.setAdapter(eventosAdapter);
                        binding.recyclerViewEventos.setLayoutManager(new LinearLayoutManager(EventosActivity.this));
                    }
                });
    }
    public void editarActividad(View view){
        Intent intent=new Intent(this, EditarActivity.class);
        startActivity(intent);
    }

}
