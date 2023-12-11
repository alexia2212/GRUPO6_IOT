package com.example.grupo_iot.delactividad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;

import java.util.List;

public class AdaptadorVistaFinalizadoLista extends RecyclerView.Adapter<AdaptadorVistaFinalizadoLista.ViewHolder> {
    private List<VistaFinalizadoLista> dataList;

    public AdaptadorVistaFinalizadoLista(List<VistaFinalizadoLista> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vistafinalizado_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VistaFinalizadoLista lista3 = dataList.get(position);

        holder.tituloTextView.setText(lista3.nombre);
        holder.fechaTextView.setText(lista3.fecha);
        holder.imagen1ImageView.setImageResource(lista3.imagen1);
        holder.descripcionTextView.setText(lista3.descripcion);
        holder.ubicacionTextView.setText(lista3.lugar);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView;
        TextView fechaTextView;
        ImageView imagen1ImageView;

        TextView descripcionTextView;
        TextView ubicacionTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.titulo);
            fechaTextView = itemView.findViewById(R.id.fecha);
            imagen1ImageView = itemView.findViewById(R.id.imagen1);
            descripcionTextView = itemView.findViewById(R.id.descripcion);
            ubicacionTextView = itemView.findViewById(R.id.ubicacion);

        }
    }
}