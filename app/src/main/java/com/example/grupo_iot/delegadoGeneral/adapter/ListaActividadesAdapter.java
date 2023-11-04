package com.example.grupo_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.delactividad.Lista;
import com.example.grupo_iot.delegadoGeneral.ActividadesActivity;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;

import java.util.List;

public class ListaActividadesAdapter extends RecyclerView.Adapter<ListaActividadesAdapter.ActividadViewHolder>{
     private List<Actividad> actividadLista;

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
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position){
        Actividad actividad = actividadLista.get(position);
        holder.nombreDelegadoTextView.setText(actividad.delegadoActividad);
        holder.descripcionContenidoTextView.setText(actividad.descripcionActividad);
        holder.nombreActividadTextView.setText(actividad.nombreActividad);
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
        public ActividadViewHolder(@NonNull View itemView){
            super(itemView);
            //ImageView ActividadesBoton = itemView.findViewById(R.id.ActividadesBoton);
            nombreDelegadoTextView = itemView.findViewById(R.id.delegadoActividad);
            descripcionContenidoTextView = itemView.findViewById(R.id.descripcionActividad);
            nombreActividadTextView = itemView.findViewById(R.id.nombreActividad);

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
