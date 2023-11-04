package com.example.grupo_iot.alumno.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.activity.EventoApoyadoActivity;
import com.example.grupo_iot.alumno.activity.ListaEventosApoyadosActivity;
import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.Evento;
import com.example.grupo_iot.alumno.entity.EventoApoyado;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaEventosApoyadosAdapter extends RecyclerView.Adapter<ListaEventosApoyadosAdapter.EventoApoyadoViewHolder> {
    private List<EventoApoyado> eventoApoyadoList;
    private Context context;
    private String idImagenEvento;
    private Alumno alumno;
    FirebaseFirestore db;

    public class EventoApoyadoViewHolder extends RecyclerView.ViewHolder{
        EventoApoyado eventoApoyado;
        public EventoApoyadoViewHolder(@NonNull View itemView){
            super(itemView);

            db = FirebaseFirestore.getInstance();

            itemView.findViewById(R.id.btnVerEventoApoy).setOnClickListener(view -> {
                CollectionReference actividadesCollection = db.collection("actividades");
                DocumentReference actividadDocument = actividadesCollection.document(eventoApoyado.getActividad());
                CollectionReference listaEventosCollection = actividadDocument.collection("listaEventos");
                Query query = listaEventosCollection.whereEqualTo("nombre", eventoApoyado.getEvento());

                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Evento evento = document.toObject(Evento.class);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
                            String fechaStr = dateFormat.format(evento.getFechaHora()).toString();
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            String horaStr = timeFormat.format(evento.getFechaHora());

                            db.collection("actividades")
                                    .document(eventoApoyado.getActividad())
                                    .get().addOnSuccessListener(documentSnapshot -> {
                                        String nombreImagen = documentSnapshot.getString("idImagenActividad");
                                        Intent intent = new Intent(context, EventoApoyadoActivity.class);
                                        intent.putExtra("nombreActividad", eventoApoyado.getActividad());
                                        intent.putExtra("nombreEvento", eventoApoyado.getEvento());
                                        intent.putExtra("descripcionEvento", evento.getDescripcion());
                                        intent.putExtra("lugarEvento", evento.getLugar());
                                        intent.putExtra("idImagenEvento", nombreImagen);
                                        intent.putExtra("fechaEvento", fechaStr);
                                        intent.putExtra("horaEvento", horaStr);
                                        intent.putExtra("alumno", alumno);
                                        intent.putExtra("apoyo", eventoApoyado.getApoyo());
                                        context.startActivity(intent);
                                    });


                        }
                    }
                });

            });

        }
    }

    @NonNull
    @Override
    public ListaEventosApoyadosAdapter.EventoApoyadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_evento_apoyado_alumno, parent, false);
        return new ListaEventosApoyadosAdapter.EventoApoyadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaEventosApoyadosAdapter.EventoApoyadoViewHolder holder, int position){
        EventoApoyado eventoApoyado = eventoApoyadoList.get(position);
        holder.eventoApoyado = eventoApoyado;

        TextView nombreActividadEvento = holder.itemView.findViewById(R.id.textViewNombreActividadEvento);
        nombreActividadEvento.setText(eventoApoyado.getActividad()+" - "+eventoApoyado.getEvento());

        db.collection("actividades")
                .document(eventoApoyado.getActividad())
                .get().addOnSuccessListener(documentSnapshot -> {
                    String nombreImagen = documentSnapshot.getString("idImagenActividad");
                    ImageView imageViewEvento = holder.itemView.findViewById(R.id.imgEventApoyado);
                    int resourceId = context.getResources().getIdentifier(nombreImagen , "drawable", context.getPackageName());
                    if (resourceId != 0) {
                        imageViewEvento.setImageResource(resourceId);
                    }
                });

        CollectionReference actividadesCollection = db.collection("actividades");
        DocumentReference actividadDocument = actividadesCollection.document(eventoApoyado.getActividad());
        CollectionReference listaEventosCollection = actividadDocument.collection("listaEventos");
        Query query = listaEventosCollection.whereEqualTo("nombre", eventoApoyado.getEvento());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Evento evento = document.toObject(Evento.class);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
                    String fechaStr = dateFormat.format(evento.getFechaHora()).toString();
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    String horaStr = timeFormat.format(evento.getFechaHora());
                    TextView fechaHoraEvento = holder.itemView.findViewById(R.id.textViewFechaEvento);
                    fechaHoraEvento.setText(fechaStr+" - "+horaStr+" Hrs.");
                    TextView lugarEvento = holder.itemView.findViewById(R.id.textViewLugarEvento);
                    lugarEvento.setText(evento.getLugar());
                }

            }
        });
    }

    @Override
    public int getItemCount(){
        return eventoApoyadoList.size();
    }

    public List<EventoApoyado> getEventoApoyadoList() {
        return eventoApoyadoList;
    }

    public void setEventoApoyadoList(List<EventoApoyado> eventoApoyadoList) {
        this.eventoApoyadoList = eventoApoyadoList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getIdImagenEvento() {
        return idImagenEvento;
    }

    public void setIdImagenEvento(String idImagenEvento) {
        this.idImagenEvento = idImagenEvento;
    }


    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
}
