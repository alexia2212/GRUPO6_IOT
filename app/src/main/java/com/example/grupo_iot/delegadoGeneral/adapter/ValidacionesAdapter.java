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
import com.example.grupo_iot.alumno.activity.ListaEventosActivity;
import com.example.grupo_iot.delactividad.Lista;
import com.example.grupo_iot.delactividad.VistaPreviaEvento;
import com.example.grupo_iot.delegadoGeneral.ValidacionActivity;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.delegadoGeneral.entity.Validaciones;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class ValidacionesAdapter extends RecyclerView.Adapter<ValidacionesAdapter.ValidacionViewHolder>{
    private List<Validaciones> validacionesList;
    private Context context;
    public ValidacionesAdapter(List<Validaciones> validacionesList) {
        this.validacionesList = validacionesList;
    }
    @NonNull
    @Override
    public ValidacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_usuarios_validar, parent, false);
        return new ValidacionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ValidacionesAdapter.ValidacionViewHolder holder, int position) {
        Validaciones validaciones = validacionesList.get(position);
        String nombreCompleto = validaciones.nombre + " " + validaciones.apellido;
        holder.nombreTextView.setText(nombreCompleto);
        holder.apellidoTextView.setText(validaciones.apellido);
        holder.codigoTextView.setText(validaciones.codigo);
        holder.idTextView.setText(validaciones.id);
        holder.emailTextView.setText(validaciones.email);
        holder.rolTextView.setText(validaciones.rol);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, ValidacionActivity.class);

                // Agrega los datos que deseas conservar en el Intent
                Validaciones selectedLista = validacionesList.get(holder.getAdapterPosition());
                intent.putExtra("listaData", selectedLista);

                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return validacionesList.size();
    }

    public class  ValidacionViewHolder extends RecyclerView.ViewHolder {
        Actividad actividad;
        TextView nombreTextView;
        TextView apellidoTextView;
        TextView codigoTextView;
        TextView idTextView;
        TextView rolTextView;
        TextView emailTextView;
        ImageView imageView;
        public ValidacionViewHolder(@NonNull View itemView){
            super(itemView);
            //ImageView ActividadesBoton = itemView.findViewById(R.id.ActividadesBoton);
            imageView = itemView.findViewById(R.id.btnValidar);
            nombreTextView = itemView.findViewById(R.id.nombreUsuario);
            apellidoTextView = itemView.findViewById(R.id.apellido);
            codigoTextView = itemView.findViewById(R.id.codigoUsuario);
            emailTextView = itemView.findViewById(R.id.email);
            idTextView = itemView.findViewById(R.id.id);
            rolTextView = itemView.findViewById(R.id.rol);

            imageView = itemView.findViewById(R.id.btnValidar);

        }

    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void setValidacionesList(List<Validaciones> validacionesList){
        this.validacionesList = validacionesList;
        notifyDataSetChanged();

    }
}
