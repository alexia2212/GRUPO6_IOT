package com.example.grupo_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.delegadoGeneral.entity.Donaciones;
import com.example.grupo_iot.delegadoGeneral.entity.Validaciones;

import java.text.SimpleDateFormat;
import java.util.List;

public class DonacionesAdapter extends RecyclerView.Adapter<DonacionesAdapter.DonacionesViewHolder>{
    private List<Donaciones> donacionesList;
    private Context context;
    @NonNull
    @Override
    public DonacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_donacion, parent, false);
        return new DonacionesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DonacionesAdapter.DonacionesViewHolder holder, int position) {
        Donaciones donaciones = donacionesList.get(position);
        holder.nombreTextView.setText(donaciones.getCondicion());
        holder.montoTextView.setText(donaciones.getMonto());
    }

    @Override
    public int getItemCount() {
        return donacionesList.size();
    }
    public class DonacionesViewHolder extends RecyclerView.ViewHolder {

        TextView nombreTextView;
        TextView montoTextView;
        TextView lugarTextView;
        // ImageView imageView;
        public DonacionesViewHolder(@NonNull View itemView){
            super(itemView);
            //db = FirebaseFirestore.getInstance();
            //imageView = itemView.findViewById(R.id.btnValidar);
            nombreTextView = itemView.findViewById(R.id.condicionDonacion);
            montoTextView = itemView.findViewById(R.id.montoDonacion);

            //imageView = itemView.findViewById(R.id.btnValidar);

        }

    }
    public void setDonacionesList(List<Donaciones> donacionesList){
        this.donacionesList = donacionesList;
        notifyDataSetChanged();

    }
}
