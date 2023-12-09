package com.example.grupo_iot.delactividad;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {
    private List<Lista> dataList;

    FirebaseFirestore db;
    FirebaseAuth auth;

    public Adaptador(List<Lista> dataList) {
        this.dataList = dataList;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
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

        if ("activo".equals(lista.estado)) {
            holder.imagen2ImageView.setImageResource(R.drawable.baseline_check_circle_outline_24);
        } else {
            holder.imagen2ImageView.setImageResource(R.drawable.baseline_check_circle_24);
        }

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

        holder.imagen2ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cambiar la imagen y el estado al hacer clic
                if ("activo".equals(lista.estado)) {
                    holder.imagen2ImageView.setImageResource(R.drawable.baseline_check_circle_24);
                    lista.estado = "finalizado";
                    actualizarEstadoEnFirestore(lista.titulo, "finalizado"); // Actualizar el estado en Firestore
                } else {
                    holder.imagen2ImageView.setImageResource(R.drawable.baseline_check_circle_outline_24);
                    lista.estado = "activo";
                    actualizarEstadoEnFirestore(lista.titulo, "activo"); // Actualizar el estado en Firestore
                }
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
        ImageView imagen2ImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.titulo);
            fechaTextView = itemView.findViewById(R.id.fecha);
            imagen1ImageView = itemView.findViewById(R.id.imagen1);
            imagen2ImageView = itemView.findViewById(R.id.imagen2);
        }
    }

    public void setDataList(List<Lista> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    private void actualizarEstadoEnFirestore(String eventId, String nuevoEstado) {
        String userID = auth.getCurrentUser().getUid();

        db.collection("credenciales")
                .document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String idActividad = documentSnapshot.getString("actividadDesignada");

                        db.collection("actividades")
                                .document(idActividad)
                                .collection("listaEventos")
                                .document(eventId)
                                .update("estado", nuevoEstado)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Delactprincipal", "Estado actualizado correctamente en Firestore");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Delactprincipal", "Error al actualizar el estado en Firestore", e);
                                });
                    } else {
                        // El documento no existe, manejar según sea necesario
                        Log.e("Delactprincipal", "El documento de credenciales no existe para el usuario " + userID);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Delactprincipal", "Error al obtener las credenciales", e);
                });
    }
}
