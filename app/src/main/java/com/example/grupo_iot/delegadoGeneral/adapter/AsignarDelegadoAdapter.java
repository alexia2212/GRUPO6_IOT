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
import com.example.grupo_iot.delegadoGeneral.AsignarDelegadoActivity;
import com.example.grupo_iot.delegadoGeneral.DelegadoActivity;
import com.example.grupo_iot.delegadoGeneral.ValidacionActivity;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.delegadoGeneral.entity.Usuarios;

import java.util.List;

public class AsignarDelegadoAdapter extends RecyclerView.Adapter<AsignarDelegadoAdapter.DelegadoViewHolder>{
    private List<Usuarios> delegadoList;
    private List<Actividad> actividadList;
    private Context context;
    public AsignarDelegadoAdapter(List<Usuarios> delegadoList, List<Actividad> actividadList) {
        this.delegadoList = delegadoList;
        this.actividadList = actividadList;
    }

    @NonNull
    @Override
    public DelegadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_asignardele, parent, false);
        return new DelegadoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AsignarDelegadoAdapter.DelegadoViewHolder holder, int position) {
        Usuarios usuarios = delegadoList.get(position);
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
                Intent intent1 = new Intent(context, AsignarDelegadoActivity.class);

                // Agrega los datos que deseas conservar en el Intent
                Usuarios selectedLista = delegadoList.get(holder.getAdapterPosition());
                intent1.putExtra("listaData2", selectedLista);
                context.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return delegadoList.size();
    }

    public class DelegadoViewHolder extends RecyclerView.ViewHolder {
        Actividad actividad;
        TextView nombreTextView;
        TextView apellidoTextView;
        TextView codigoTextView;
        TextView idTextView;
        TextView rolTextView;
        TextView emailTextView;
        ImageView imageView;
        public DelegadoViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.btnAsignarDele);
            nombreTextView = itemView.findViewById(R.id.nombreDeleLista);
            apellidoTextView = itemView.findViewById(R.id.apellidoDeleLista);
            codigoTextView = itemView.findViewById(R.id.codigoDeleLista);
            emailTextView = itemView.findViewById(R.id.emailDeleLista);
            idTextView = itemView.findViewById(R.id.idDeleLista);
            rolTextView = itemView.findViewById(R.id.rolDeleLista);


        }

    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void setDelegadoList(List<Usuarios> delegadoList){
        this.delegadoList = delegadoList;
        notifyDataSetChanged();

    }
}
