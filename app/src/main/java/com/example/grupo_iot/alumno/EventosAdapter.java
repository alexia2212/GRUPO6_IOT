package com.example.grupo_iot.alumno;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.entity.Evento;

import java.util.List;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.EventoViewHolder>{
    private List<Evento> listaEventos;
    private Context context;
    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_eventos_usuario, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position){
        Evento evento = listaEventos.get(position);
        holder.evento = evento;

        TextView nombreEvento = holder.itemView.findViewById(R.id.textViewNombreEvento);
        TextView descripcionEvento = holder.itemView.findViewById(R.id.textViewDescripcionEvento);
        ImageView imagenEvento = holder.itemView.findViewById(R.id.imgEvento);

        nombreEvento.setText(evento.getNombreEvento());
        descripcionEvento.setText(evento.getDescripcionEvento());
        imagenEvento.setImageResource(evento.getImagenEvento());
    }

    @Override
    public int getItemCount(){
        return listaEventos.size();
    }


    public class  EventoViewHolder extends RecyclerView.ViewHolder{
        Evento evento;
        public EventoViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }


    public List<Evento> getListaEventos() {
        return listaEventos;
    }

    public void setListaEventos(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
