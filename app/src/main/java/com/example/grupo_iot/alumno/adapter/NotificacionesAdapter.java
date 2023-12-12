package com.example.grupo_iot.alumno.adapter;

import android.content.Context;
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
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.Notificacion;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder> {
    private List<Notificacion> notificacionList;
    private Context context;
    private Alumno alumno;
    FirebaseFirestore db;

    public class NotificacionViewHolder extends RecyclerView.ViewHolder {
        Notificacion notificacion;
        public NotificacionViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }

    @NonNull
    @Override
    public NotificacionesAdapter.NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_notificacion_alumno, parent, false);
        return new NotificacionesAdapter.NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionesAdapter.NotificacionViewHolder holder, int position){
        Notificacion notificacion = notificacionList.get(position);
        holder.notificacion = notificacion;
        TextView contenidoNotificacion = holder.itemView.findViewById(R.id.contenidoNotificacion);
        TextView fechaHora = holder.itemView.findViewById(R.id.fechaHoraNotificacion);
        TextView tituloNotificacion = holder.itemView.findViewById(R.id.tituloNotifi);
        ImageView imageViewNotificacion = holder.itemView.findViewById(R.id.img_notif);

        SimpleDateFormat formato = new SimpleDateFormat("HH:mm 'Hrs.' dd/MM/yyyy", new Locale("es", "ES"));
        String fechaHoraStr = formato.format(notificacion.getFechaHora());

        tituloNotificacion.setText(notificacion.getTitulo());
        contenidoNotificacion.setText(notificacion.getContenido());
        fechaHora.setText(fechaHoraStr);
/*
        Glide.with(context)
                .load(notificacion.getImagen())
                .into(imageViewNotificacion);

 */
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imgRef = firebaseStorage.getReference().child("img_notificaciones/"+notificacion.getImagen()+".png");

        Log.d("sdd", "img_notificaciones/"+notificacion.getImagen()+".png");
        Glide.with(context)
                .load(imgRef)
                .into(imageViewNotificacion);

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
