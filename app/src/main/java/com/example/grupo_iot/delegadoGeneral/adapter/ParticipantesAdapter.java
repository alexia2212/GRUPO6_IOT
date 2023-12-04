package com.example.grupo_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.delegadoGeneral.entity.Evento;
import com.example.grupo_iot.delegadoGeneral.entity.Participantes;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;

import java.text.SimpleDateFormat;
import java.util.List;

public class ParticipantesAdapter extends RecyclerView.Adapter<ParticipantesAdapter.ParticipantesViewHolder>{
    private List<Participantes> participantesList;
    private Context context;
    private String actividad;
    @NonNull
    @Override
    public ParticipantesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_actividades_participantes_actividad_dg, parent, false);
        return new ParticipantesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantesAdapter.ParticipantesViewHolder holder, int position) {
        Participantes participantes = participantesList.get(position);
        holder.nombreTextView.setText(participantes.nombre);
        holder.codigoTextView.setText(participantes.codigo);

    }

    @Override
    public int getItemCount() {
        return participantesList.size();
    }

    public class ParticipantesViewHolder extends RecyclerView.ViewHolder {
        Actividad actividad;
        TextView nombreTextView;
        TextView codigoTextView;
        TextView apellidoTextView;
        public ParticipantesViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreParticipante);
            codigoTextView = itemView.findViewById(R.id.codigoParticipante);

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

    public void setParticipantesList(List<Participantes> participantesList){
        this.participantesList = participantesList;
        notifyDataSetChanged();

    }
}

