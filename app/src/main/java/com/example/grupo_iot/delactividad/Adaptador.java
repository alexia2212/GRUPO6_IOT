package com.example.grupo_iot.delactividad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {
    private List<Lista> dataList;

    public Adaptador(List<Lista> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Lista lista = dataList.get(position);

        holder.tituloTextView.setText(lista.titulo);
        holder.fechaTextView.setText(lista.fecha);
        Picasso.get().load(lista.getImagen1()).into(holder.imagen1ImageView);

        holder.imagen1ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, VistaPreviaEvento.class);

                // Agrega los datos que deseas conservar en el Intent
                Lista selectedLista = dataList.get(holder.getAdapterPosition());
                intent.putExtra("listaData", selectedLista);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView;
        TextView fechaTextView;
        ImageView imagen1ImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.titulo);
            fechaTextView = itemView.findViewById(R.id.fecha);
            imagen1ImageView = itemView.findViewById(R.id.imagen1);
        }
    }

    public void setDataList(List<Lista> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }
}
