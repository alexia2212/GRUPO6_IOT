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
import com.example.grupo_iot.delegadoGeneral.AsignarDelegadoActivity;
import com.example.grupo_iot.delegadoGeneral.EditarActivity;
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
        holder.idTextView.setText(actividad.id);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, EventosActivity.class);
                Actividad selectedLista = actividadLista.get(holder.getAdapterPosition());
                intent.putExtra("listaData", selectedLista);
                context.startActivity(intent);
                Context context1 = view.getContext();
                Intent intent1 = new Intent(context1, EditarActivity.class);
                intent1.putExtra("listaData", selectedLista);
                context1.startActivity(intent1);
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
        TextView idTextView;
        Button button;
        public ActividadViewHolder(@NonNull View itemView){
            super(itemView);
            //ImageView ActividadesBoton = itemView.findViewById(R.id.ActividadesBoton);
            nombreDelegadoTextView = itemView.findViewById(R.id.delegadoActividad);
            descripcionContenidoTextView = itemView.findViewById(R.id.descripcionActividad);
            nombreActividadTextView = itemView.findViewById(R.id.nombreActividad);
            idTextView = itemView.findViewById(R.id.idActividad);
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
