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

public class AdaptadorVistaPreviaActualizacion extends RecyclerView.Adapter<AdaptadorVistaPreviaActualizacion.ViewHolder> {
    private List<VistaPreviaActualizacionLista> dataList;

    public AdaptadorVistaPreviaActualizacion(List<VistaPreviaActualizacionLista> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vistaactualizacion_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VistaPreviaActualizacionLista lista2 = dataList.get(position);

        holder.tituloTextView.setText(lista2.titulo);
        holder.fechaTextView.setText(lista2.fecha);
        holder.imagen1ImageView.setImageResource(lista2.imagen1);
        holder.descripcionTextView.setText(lista2.descripcion);
        holder.ubicacionTextView.setText(lista2.ubicacion);


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
