package com.example.grupo_iot.alumno.adapter;

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

import com.bumptech.glide.Glide;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.activity.ListaEventosActivity;
import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ListaActividadesAdapter extends RecyclerView.Adapter<ListaActividadesAdapter.ActividadViewHolder>{
    private List<Actividad> actividadList;
    private Context context;
    private Alumno alumno;
    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_actividades_alumno, parent, false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position){
        Actividad actividad = actividadList.get(position);
        holder.actividad = actividad;

        TextView nombreActividad = holder.itemView.findViewById(R.id.textViewNombreActividad);
        TextView descripcionActividad = holder.itemView.findViewById(R.id.textViewDescripcionActividad);
        ImageView imagenActividad = holder.itemView.findViewById(R.id.imgActividad);

        nombreActividad.setText(actividad.getNombreActividad());
        descripcionActividad.setText(actividad.getDescripcionActividad());

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imgRef = firebaseStorage.getReference().child("img_actividades/"+actividad.getIdImagenActividad()+".png");

        Glide.with(context)
                .load(imgRef)
                .into(imagenActividad);
    }

    @Override
    public int getItemCount(){
        return actividadList.size();
    }


    public class  ActividadViewHolder extends RecyclerView.ViewHolder{
        Actividad actividad;
        public ActividadViewHolder(@NonNull View itemView){
            super(itemView);
            ImageView btnVerActividad = itemView.findViewById(R.id.btnVerActividad);
            TextView nombreActividad = itemView.findViewById(R.id.textViewNombreActividad);
            TextView descripcionActividad = itemView.findViewById(R.id.textViewDescripcionActividad);
            ImageView imagenActividad = itemView.findViewById(R.id.imgActividad);

            btnVerActividad.setOnClickListener(view -> {
                Intent intent = new Intent(context, ListaEventosActivity.class);
                intent.putExtra("nombreActividad", nombreActividad.getText().toString());
                intent.putExtra("descripcionActividad", descripcionActividad.getText().toString());
                intent.putExtra("imagenActividad", actividad.getIdImagenActividad());
                intent.putExtra("alumno",alumno);
                context.startActivity(intent);
            });

        }
    }


    public List<Actividad> getActividadList() {
        return actividadList;
    }

    public void setActividadList(List<Actividad> actividadList) {
        this.actividadList = actividadList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
}
