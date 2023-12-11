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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdaptadorFinalizado extends RecyclerView.Adapter<AdaptadorFinalizado.ViewHolder> {
    private List<Lista> dataList;

    FirebaseFirestore db;
    FirebaseAuth auth;

    public AdaptadorFinalizado(List<Lista> dataList) {
        this.dataList = dataList;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Lista lista = dataList.get(position);

        holder.tituloTextView.setText(lista.nombre);
        String fechaHoraStr = obtenerFechaYHora(lista.getFechaHora());
        holder.fechaTextView.setText(fechaHoraStr);
        Picasso.get().load(lista.getImagen()).into(holder.imagen1ImageView);

        holder.imagen1ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, EventoFinalizadoExcedeFotos.class);

                // Agrega los datos que deseas conservar en el Intent
                Lista selectedLista = dataList.get(holder.getAdapterPosition());
                intent.putExtra("idDelDocumentoActual", lista.getNombre());
                intent.putExtra("listaData", selectedLista);
                intent.putStringArrayListExtra("urlsImagenes", new ArrayList<>(lista.getUrlsImagenes()));
                System.out.println(lista.getUrlsImagenes() + "pssssss");


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
            tituloTextView = itemView.findViewById(R.id.titulo2);
            fechaTextView = itemView.findViewById(R.id.fecha2);
            imagen1ImageView = itemView.findViewById(R.id.imagen2);
        }
    }

    public void setDataList(List<Lista> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    private String obtenerFechaYHora(Date fechaHora) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy (HH:mm)", Locale.getDefault());
        return sdf.format(fechaHora);
    }


}
