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
public class AdaptadorNuevoEvento extends RecyclerView.Adapter<AdaptadorNuevoEvento.ViewHolder> {

    private List<NuevoEventoLista> dataList;

    public AdaptadorNuevoEvento(List<NuevoEventoLista> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AdaptadorNuevoEvento.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nuevoevento_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorNuevoEvento.ViewHolder holder, int position) {
        NuevoEventoLista lista4 = dataList.get(position);

        holder.tituloTextView.setText(lista4.nombre);
        holder.fechaTextView.setText(lista4.fecha);



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView;
        TextView fechaTextView;
        TextView descripcionTextView;
        TextView fotoTextView;

        TextView lugarTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.titulonuevoevento);
            fechaTextView = itemView.findViewById(R.id.fechanuevoevento);
            lugarTextView = itemView.findViewById(R.id.lugarnuevoevento);
            descripcionTextView = itemView.findViewById(R.id.descripcionuevoevento);
            fotoTextView = itemView.findViewById(R.id.fotonuevoevento);

        }
    }
}
