package com.example.grupo_iot.alumno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.Notificacion;
import com.google.firebase.firestore.FirebaseFirestore;

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
        //ImageView imageView = holder.itemView.findViewById(R.id.chat1);

        SimpleDateFormat formato = new SimpleDateFormat("HH:mm 'Hrs.' dd/MM/yyyy", new Locale("es", "ES"));
        String fechaHoraStr = formato.format(notificacion.getFechaHora());

        contenidoNotificacion.setText(notificacion.getContenido());
        fechaHora.setText(fechaHoraStr);
        //imageView.setImageResource(R.drawable.baseline_error_outline_24);
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
