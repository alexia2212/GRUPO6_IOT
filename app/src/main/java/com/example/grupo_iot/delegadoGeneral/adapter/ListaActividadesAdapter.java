package com.example.grupo_iot.delegadoGeneral.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.delactividad.Lista;
import com.example.grupo_iot.delegadoGeneral.ActividadesActivity;
import com.example.grupo_iot.delegadoGeneral.EventosActivity;
import com.example.grupo_iot.delegadoGeneral.ParticipantesActivity;
import com.example.grupo_iot.delegadoGeneral.ValidacionActivity;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.delegadoGeneral.entity.Evento;
import com.example.grupo_iot.delegadoGeneral.entity.Participantes;

import java.util.List;

public class ListaActividadesAdapter extends RecyclerView.Adapter<ListaActividadesAdapter.ActividadViewHolder>{
     private List<Actividad> actividadLista;
    private List<Actividad> actividadLista1;
     private List<Evento> eventoList;
     private List<Participantes> participantesList;

    public ListaActividadesAdapter(List<Actividad> actividadLista) {
        this.actividadLista = actividadLista;
    }
    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_actividades_delgeneral, parent, false);
        return new ActividadViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, final int position){
        Actividad actividad = actividadLista.get(position);
        holder.nombreDelegadoTextView.setText(actividad.delegadoActividad);
        holder.descripcionContenidoTextView.setText(actividad.descripcionActividad);
        holder.nombreActividadTextView.setText(actividad.nombreActividad);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent eventosIntent = new Intent(context, EventosActivity.class);
                Actividad selectedLista = actividadLista.get(holder.getAdapterPosition());
                eventosIntent.putExtra("listaData", selectedLista);
                Intent participantesIntent = new Intent(context, ParticipantesActivity.class);
                Actividad selectedLista1 = actividadLista.get(holder.getAdapterPosition());
                participantesIntent.putExtra("listaData1", selectedLista1);
                context.startActivities(new Intent[]{eventosIntent, participantesIntent});
            }
        });

    }

    @Override
    public int getItemCount(){
        return actividadLista.size();
    }


    public class  ActividadViewHolder extends RecyclerView.ViewHolder{
        Actividad actividad;
        TextView nombreDelegadoTextView;
        TextView descripcionContenidoTextView;
        TextView nombreActividadTextView;
        Button button;
        public ActividadViewHolder(@NonNull View itemView){
            super(itemView);
            //ImageView ActividadesBoton = itemView.findViewById(R.id.ActividadesBoton);
            nombreDelegadoTextView = itemView.findViewById(R.id.delegadoActividad);
            descripcionContenidoTextView = itemView.findViewById(R.id.descripcionActividad);
            nombreActividadTextView = itemView.findViewById(R.id.nombreActividad);
            button = itemView.findViewById(R.id.masinfoactividad);

           // ActividadesBoton.setOnClickListener(view -> {
            //    Intent intent = new Intent(context, ActividadesActivity.class);
            //    intent.putExtra("nombreActividad", nombreActividad.getText().toString());
            //    intent.putExtra("descripcionActividad", descripcionActividad.getText().toString());
            //    intent.putExtra("delegadoActividad", delegadoActividad.getText().toString());
            //    context.startActivity(intent);
            //});

        }
    }

    public void setActividadLista(List<Actividad> actividadLista){
        this.actividadLista = actividadLista;
        notifyDataSetChanged();

    }

}
