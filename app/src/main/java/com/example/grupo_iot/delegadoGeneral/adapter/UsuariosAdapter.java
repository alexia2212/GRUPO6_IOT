package com.example.grupo_iot.delegadoGeneral.adapter;

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
import com.example.grupo_iot.delegadoGeneral.ValidacionActivity;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.delegadoGeneral.entity.Usuarios;
import com.example.grupo_iot.delegadoGeneral.entity.Validaciones;

import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuariosViewHolder>{
    private List<Usuarios> usuariosList;
    private Context context;
    public UsuariosAdapter(List<Usuarios> usuariosList) {
        this.usuariosList = usuariosList;
    }
    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_usuarios, parent, false);
        return new UsuariosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosAdapter.UsuariosViewHolder holder, int position) {
        Usuarios usuarios = usuariosList.get(position);
        String nombreCompleto = usuarios.nombre + " " + usuarios.apellido;
        holder.nombreTextView.setText(nombreCompleto);
        holder.apellidoTextView.setText(usuarios.apellido);
        holder.codigoTextView.setText(usuarios.codigo);
        holder.idTextView.setText(usuarios.id);
        holder.emailTextView.setText(usuarios.email);
        holder.rolTextView.setText(usuarios.rol);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, ValidacionActivity.class);

                // Agrega los datos que deseas conservar en el Intent
                Usuarios selectedLista = usuariosList.get(holder.getAdapterPosition());
                intent.putExtra("listaData", selectedLista);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuariosList.size();
    }
    public class  UsuariosViewHolder extends RecyclerView.ViewHolder {
        Actividad actividad;
        TextView nombreTextView;
        TextView apellidoTextView;
        TextView codigoTextView;
        TextView idTextView;
        TextView rolTextView;
        TextView emailTextView;
        ImageView imageView;
        public UsuariosViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.btnInfoUsuario);
            nombreTextView = itemView.findViewById(R.id.nombreUsuarioLista);
            apellidoTextView = itemView.findViewById(R.id.apellidoUsuarioLista);
            codigoTextView = itemView.findViewById(R.id.codigoUsuarioLista);
            emailTextView = itemView.findViewById(R.id.emailUsuarioLista);
            idTextView = itemView.findViewById(R.id.idUsuarioLista);
            rolTextView = itemView.findViewById(R.id.rolUsuarioLista);


        }

    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void setUsuariosList(List<Usuarios> usuariosList){
        this.usuariosList = usuariosList;
        notifyDataSetChanged();

    }
}
