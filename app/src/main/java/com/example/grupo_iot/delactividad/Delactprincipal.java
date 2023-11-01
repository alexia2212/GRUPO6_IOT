package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityCompartirfotosBinding;
import com.example.grupo_iot.databinding.ActivityDelactprincipalBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class Delactprincipal extends AppCompatActivity {

    ActivityDelactprincipalBinding binding;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDelactprincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageViewsalir.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setTitle("Aviso")
                    .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        startActivity(intent1);
                    })
                    .setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();

        });

        List<Lista> dataList = new ArrayList<>();
        dataList.add(new Lista("Convocatoria", "15/09/2023", R.drawable.voley1, R.drawable.baseline_favorite_24));
        dataList.add(new Lista("Entrenamiento", "19/09/2023", R.drawable.voley2, R.drawable.baseline_favorite_border_24));
        dataList.add(new Lista("1er partido", "14/10/2023", R.drawable.voley3, R.drawable.baseline_favorite_border_24));
        dataList.add(new Lista("2do partido", "16/10/2023", R.drawable.voley4, R.drawable.baseline_favorite_24));
        dataList.add(new Lista("3er partido", "18/10/2023", R.drawable.voley5, R.drawable.baseline_favorite_border_24));
        dataList.add(new Lista("4to partido", "22/10/2023", R.drawable.voley1, R.drawable.baseline_favorite_24));

        RecyclerView recyclerView = findViewById(R.id.eventos);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        Adaptador adapter = new Adaptador(dataList);
        recyclerView.setAdapter(adapter);

        ImageView addImage = findViewById(R.id.imageView21);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la nueva actividad aquí
                Intent intent = new Intent(Delactprincipal.this, NuevoEvento.class);
                startActivity(intent);
            }
        });



    }

}
