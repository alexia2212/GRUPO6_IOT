package com.example.grupo_iot.delegadoGeneral.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.delegadoGeneral.entity.Usuarios;

import java.util.List;

public class ListaParticipantesActividadAdapter extends RecyclerView.Adapter<ListaParticipantesActividadAdapter.ParticipantesActividadViewHolder>{
    private List<Usuarios> usuariosList;
    private Context context;

    public ListaParticipantesActividadAdapter(List<Usuarios> usuariosList) {
            this.usuariosList = usuariosList;
            }
    @NonNull
    @Override
    public ParticipantesActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_participantes_por_actividad, parent, false);
        return new ParticipantesActividadViewHolder(v);
        }

    @Override
    public void onBindViewHolder(@NonNull ListaParticipantesActividadAdapter.ParticipantesActividadViewHolder holder, final int position) {
        Usuarios usuarios = usuariosList.get(position);
        String nombreCompleto = usuarios.nombre + " " + usuarios.apellido;
        holder.nombreTextView.setText(nombreCompleto);
        holder.apellidoTextView.setText(usuarios.apellido);
        holder.codigoTextView.setText(usuarios.codigo);
        holder.emailTextView.setText(usuarios.email);
        holder.rolTextView.setText(usuarios.rol);
    }
    @Override
    public int getItemCount () {
        return usuariosList.size();
    }


    public class ParticipantesActividadViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView apellidoTextView;
        TextView codigoTextView;
        TextView rolTextView;
        TextView emailTextView;
        ImageView imageView;

        public ParticipantesActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.btnverPartiEspecifico);
            nombreTextView = itemView.findViewById(R.id.nombrePartiActi);
            apellidoTextView = itemView.findViewById(R.id.apellidoPartiActi);
            codigoTextView = itemView.findViewById(R.id.codigoPartiActi);
            emailTextView = itemView.findViewById(R.id.emailPartiActi);
            rolTextView = itemView.findViewById(R.id.condicionPartiActi);
        }
    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void setUsuariosList (List < Usuarios > usuariosList) {
        this.usuariosList = usuariosList;
        notifyDataSetChanged();
    }
}
