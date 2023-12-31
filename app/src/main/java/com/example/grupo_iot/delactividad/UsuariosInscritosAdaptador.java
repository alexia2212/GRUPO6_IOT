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

public class UsuariosInscritosAdaptador extends RecyclerView.Adapter<UsuariosInscritosAdaptador.ViewHolder> {

    private List<UsuariosLista> listaDatos; // Reemplaza TuModelo con el nombre de tu modelo de datos

    public UsuariosInscritosAdaptador(List<UsuariosLista> listaDatos) {
        this.listaDatos = listaDatos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listausuarios1, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsuariosLista dato = listaDatos.get(position);

        // Configura las vistas de ViewHolder según los datos del modelo
        holder.nombre.setText(dato.getNombre());
        holder.funcion.setText(dato.getFuncion());
        holder.condicion.setText(dato.getCondicion());
        holder.imagen.setImageResource(dato.img1);

        holder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, VistaUsuarioActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView nombre;
        TextView condicion;
        TextView funcion;

        public ViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagen1);
            nombre = itemView.findViewById(R.id.titulo);
            condicion = itemView.findViewById(R.id.condicion);
            funcion = itemView.findViewById(R.id.funcion);

        }
    }

}