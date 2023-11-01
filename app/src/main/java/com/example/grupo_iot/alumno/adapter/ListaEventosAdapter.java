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
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.activity.EventoActivity;
import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.Evento;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListaEventosAdapter extends RecyclerView.Adapter<ListaEventosAdapter.EventoViewHolder>{
    private List<Evento> eventoList;
    private Context context;
    private String idImagenEvento;
    private String actividad;
    private Alumno alumno;

    public class EventoViewHolder extends RecyclerView.ViewHolder{
        Evento evento;
        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView btnVerEvento = itemView.findViewById(R.id.textViewNombreEvento);

            btnVerEvento.setOnClickListener(view -> {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fechaStr = dateFormat.format(evento.getFechaHora()).toString();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String horaStr = timeFormat.format(evento.getFechaHora());

                Intent intent = new Intent(context, EventoActivity.class);
                intent.putExtra("nombreActividad", actividad);
                intent.putExtra("nombreEvento", evento.getNombre());
                intent.putExtra("descripcionEvento", evento.getDescripcion());
                intent.putExtra("lugarEvento", evento.getLugar());
                intent.putExtra("idImagenEvento",idImagenEvento);
                intent.putExtra("fechaEvento", fechaStr);
                intent.putExtra("horaEvento", horaStr);
                intent.putExtra("alumno",alumno);
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public ListaEventosAdapter.EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_eventos_alumno, parent, false);
        return new ListaEventosAdapter.EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaEventosAdapter.EventoViewHolder holder, int position){
        Evento evento = eventoList.get(position);
        holder.evento = evento;

        TextView nombreEvento = holder.itemView.findViewById(R.id.textViewNombreEvento);
        nombreEvento.setText(evento.getNombre());
    }

    @Override
    public int getItemCount(){
        return eventoList.size();
    }

    public List<Evento> getEventoList() {
        return eventoList;
    }

    public void setEventoList(List<Evento> eventoList) {
        this.eventoList = eventoList;
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

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
}
