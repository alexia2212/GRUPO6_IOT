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
import com.squareup.picasso.Picasso;

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

        holder.nombre.setText(usuario.getNombre() + " " + usuario.getApellido());
        holder.condicion.setText(usuario.getCondicion());
        holder.funcion.setText(usuario.getFuncion());
        Picasso.get().load(usuario.getFoto()).into(holder.foto);

        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, UsuariosInscritos.class);

                // Agrega los datos que deseas conservar en el Intent
                Usuario selectedLista = dataList.get(holder.getAdapterPosition());
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

        TextView nombre;
        TextView condicion;
        TextView funcion;
        ImageView foto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            condicion = itemView.findViewById(R.id.condicion);
            funcion = itemView.findViewById(R.id.funcion);
            foto = itemView.findViewById(R.id.imagen1);

        }
    }

    public void setDataList(List<Usuario> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }
}

