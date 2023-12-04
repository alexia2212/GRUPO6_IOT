package com.example.grupo_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.delegadoGeneral.entity.Evento;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder>{
    private List<Evento> eventoList;
    private Context context;
    private String actividad;
    FirebaseFirestore db;


    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_actividades_eventos_dg, parent, false);
        return new EventoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoAdapter.EventoViewHolder holder, int position) {
        Evento evento = eventoList.get(position);
        holder.nombreTextView.setText(evento.nombre);
        holder.idTextView.setText(evento.id);
        holder.descripcionTextView.setText(evento.descripcion);
        holder.lugarTextView.setText(evento.lugar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaStr = dateFormat.format(evento.getFechaHora()).toString();
        holder.fechaHoraTextView.setText(fechaStr);
       /* holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, ValidacionActivity.class);

                // Agrega los datos que deseas conservar en el Intent
                Validaciones selectedLista = validacionesList.get(holder.getAdapterPosition());
                intent.putExtra("listaData", selectedLista);

                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }
    public class EventoViewHolder extends RecyclerView.ViewHolder {
        Actividad actividad;
        TextView nombreTextView;
        TextView idTextView;
        TextView descripcionTextView;
        TextView fechaHoraTextView;
        TextView lugarTextView;
       // ImageView imageView;
        public EventoViewHolder(@NonNull View itemView){
            super(itemView);
            //db = FirebaseFirestore.getInstance();
            //imageView = itemView.findViewById(R.id.btnValidar);
            nombreTextView = itemView.findViewById(R.id.nombreEvento);
            idTextView = itemView.findViewById(R.id.idEventoAct);
            descripcionTextView = itemView.findViewById(R.id.descripcionEvento);
            fechaHoraTextView = itemView.findViewById(R.id.fechaEvento);
            lugarTextView = itemView.findViewById(R.id.lugarEvento);

            //imageView = itemView.findViewById(R.id.btnValidar);

        }

    }
    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void setEventoList(List<Evento> eventoList){
        this.eventoList = eventoList;
        notifyDataSetChanged();

    }
}
