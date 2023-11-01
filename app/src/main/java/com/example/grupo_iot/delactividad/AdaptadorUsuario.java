package com.example.grupo_iot.delactividad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;

import java.util.List;

public class AdaptadorUsuario extends RecyclerView.Adapter<AdaptadorUsuario.ViewHolder> {
    private List<Usuario> dataList;

    public AdaptadorUsuario(List<Usuario> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listausuarios1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario usuario = dataList.get(position);

        holder.imagen1.setImageResource(usuario.getImagenResId());
        holder.nombre.setText(usuario.getNombre());
        holder.condicion.setText(usuario.getCondicion());
        holder.funcion.setText(usuario.getFuncion());

        holder.imagen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, UsuariosInscritos.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen1;
        TextView nombre;
        TextView condicion;
        TextView funcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen1 = itemView.findViewById(R.id.imagen1);
            nombre = itemView.findViewById(R.id.nombre);
            condicion = itemView.findViewById(R.id.condicion);
            funcion = itemView.findViewById(R.id.funcion);
        }
    }
}

