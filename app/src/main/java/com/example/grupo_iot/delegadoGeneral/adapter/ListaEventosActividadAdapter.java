package com.example.grupo_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.delegadoGeneral.EventoEspecificoActivity;
import com.example.grupo_iot.delegadoGeneral.entity.Evento2;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListaEventosActividadAdapter extends RecyclerView.Adapter<ListaEventosActividadAdapter.EventosActividadViewHolder>{
    private List<Evento2> eventoList;

    public ListaEventosActividadAdapter(List<Evento2> eventoList) {
        this.eventoList = eventoList;
    }
    @NonNull
    @Override
    public EventosActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_eventos_por_actividad, parent, false);
        return new EventosActividadViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventosActividadViewHolder holder, final int position){
        Evento2 evento = eventoList.get(position);
        holder.nombreTextView.setText(evento.nombre);
        holder.descripcionTextView.setText(evento.descripcion);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaStr = dateFormat.format(evento.getFechaHora()).toString();
        holder.fechaTextView.setText(fechaStr);
        holder.lugarTextView.setText(evento.lugar);
        Picasso.get().load(evento.getImagen()).into(holder.imageView2);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, EventoEspecificoActivity.class);
                Evento2 selectedLista = eventoList.get(holder.getAdapterPosition());
                intent.putExtra("listaData", selectedLista);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return eventoList.size();
    }


    public class  EventosActividadViewHolder extends RecyclerView.ViewHolder{
        TextView nombreTextView;
        TextView descripcionTextView;
        TextView fechaTextView;
        TextView lugarTextView;
        //TextView idTextView;
        //Button button;
        ImageView imageView;
        ImageView imageView2;
        public EventosActividadViewHolder(@NonNull View itemView){
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.tituloEventoActi);
            descripcionTextView = itemView.findViewById(R.id.descriEventoActi);
            fechaTextView = itemView.findViewById(R.id.fechaEventoActi);
            //idTextView = itemView.findViewById(R.id.idActividad);
            lugarTextView = itemView.findViewById(R.id.lugarEventoActi);
            imageView = itemView.findViewById(R.id.btnverEventoEspecifico);
            imageView2 = itemView.findViewById(R.id.imagenEventoActi);
        }
    }

    public void setEventoList(List<Evento2> eventoList){
        this.eventoList = eventoList;
        notifyDataSetChanged();
    }

}
