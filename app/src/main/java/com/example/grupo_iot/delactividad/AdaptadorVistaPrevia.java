package com.example.grupo_iot.delactividad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;

import java.util.List;

public class AdaptadorVistaPrevia extends RecyclerView.Adapter<AdaptadorVistaPrevia.ViewHolder> {
    private List<VistaPrevia> dataList;

    public AdaptadorVistaPrevia(List<VistaPrevia> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.previa_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VistaPrevia lista2 = dataList.get(position);

        holder.nombreTextView.setText(lista2.nombre);
        holder.fechaTextView.setText(lista2.fecha);
        holder.imagen1ImageView.setImageResource(lista2.imagen1);
        holder.descripcionTextView.setText(lista2.descripcion);
        holder.ubicacionTextView.setText(lista2.lugar);

        holder.imagen2ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, ActualizarActivity.class);
                context.startActivity(intent);
            }
        });

        holder.imagen3ImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mostrarDialogoDeConfirmacion(view.getContext());
            }
        });

        holder.boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoDeConfirmacion2(view.getContext());
            }
        });

        holder.botonVerListaAlumnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, ListaDeUsuarios.class);
                context.startActivity(intent);
            }
        });







    }

    private void mostrarDialogoDeConfirmacion(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_confirmacion, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button botonSi = dialogView.findViewById(R.id.boton_si);
        botonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar la lógica para eliminar el evento
                dialog.dismiss();
            }
        });

        Button botonCancelar = dialogView.findViewById(R.id.boton_cancelar);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void mostrarDialogoDeConfirmacion2(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_confirmacion2, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button botonSi = dialogView.findViewById(R.id.boton_si);
        botonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar la lógica para eliminar el evento
                dialog.dismiss();
            }
        });

        Button botonCancelar = dialogView.findViewById(R.id.boton_cancelar);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView fechaTextView;
        ImageView imagen1ImageView;

        TextView descripcionTextView;
        TextView ubicacionTextView;

        ImageView imagen2ImageView;

        Button botonVerListaAlumnos;

        ImageView imagen3ImageView;

        Button boton2;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.titulo);
            fechaTextView = itemView.findViewById(R.id.fecha);
            imagen1ImageView = itemView.findViewById(R.id.imagen1);
            descripcionTextView = itemView.findViewById(R.id.descripcion);
            ubicacionTextView = itemView.findViewById(R.id.ubicacion);
            imagen2ImageView = itemView.findViewById(R.id.icono_derecha);
            imagen3ImageView = itemView.findViewById(R.id.icono_izquierda);
            boton2 = itemView.findViewById(R.id.boton2);
            botonVerListaAlumnos = itemView.findViewById(R.id.boton1);


        }
    }
}
