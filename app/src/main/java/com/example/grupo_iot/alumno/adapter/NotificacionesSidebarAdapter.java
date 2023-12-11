package com.example.grupo_iot.alumno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.Notificacion;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotificacionesSidebarAdapter extends RecyclerView.Adapter<NotificacionesSidebarAdapter.NotificacionesViewHolder> {
    private List<Notificacion> notificacionList;
    private Context context;
    private Alumno alumno;
    FirebaseFirestore db;

    public class NotificacionesViewHolder extends RecyclerView.ViewHolder {
        Notificacion notificacion;
        public NotificacionesViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }

    @NonNull
    @Override
    public NotificacionesSidebarAdapter.NotificacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_notificacion_sidebar_alumno, parent, false);
        return new NotificacionesSidebarAdapter.NotificacionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionesSidebarAdapter.NotificacionesViewHolder holder, int position){
        Notificacion notificacion = notificacionList.get(position);
        holder.notificacion = notificacion;

        TextView tituloNotificacion = holder.itemView.findViewById(R.id.tituloNotificacion);
        ImageView imgNotificacion = holder.itemView.findViewById(R.id.imgNotificacion);

        tituloNotificacion.setText(notificacion.getTitulo());

        Glide.with(context)
                .load(notificacion.getImagen())
                .into(imgNotificacion);
    }

    @Override
    public int getItemCount(){
        return notificacionList.size();
    }

    public List<Notificacion> getNotificacionList() {
        return notificacionList;
    }

    public void setNotificacionList(List<Notificacion> notificacionList) {
        this.notificacionList = notificacionList;
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
