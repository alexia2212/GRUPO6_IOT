package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.entity.Evento;
import com.example.grupo_iot.databinding.ActivityCompartirfotosBinding;
import com.example.grupo_iot.databinding.ActivityDelactprincipalBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class Delactprincipal extends AppCompatActivity {

    FirebaseFirestore db;

    ActivityDelactprincipalBinding binding;

    private Adaptador adapter;
    private List<Lista> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDelactprincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        dataList = new ArrayList<>();
        adapter = new Adaptador(dataList);

        RecyclerView recyclerView = findViewById(R.id.eventos);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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

        // Obtén una referencia a Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

// Crea una referencia para la ubicación de la imagen que deseas obtener
        StorageReference imagenRef = storageRef.child("imagenes/voley6.jpg");

// Obtiene el enlace de descarga de la imagen
        imagenRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imagenUrl = uri.toString();
            // Imprime la URL de la imagen
            System.out.println("URL de la imagen: " + imagenUrl);

            // También puedes mostrar la URL en un TextView o cualquier otro lugar necesario en tu aplicación
        }).addOnFailureListener(exception -> {
            // Maneja errores si no se puede obtener el enlace de descarga
        });


        TextInputEditText searchEditText = findViewById(R.id.searchEditText);

        if (searchEditText != null) {
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String searchText = charSequence.toString().toLowerCase().trim();
                    filterData(searchText);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }

        db.collection("listaeventos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    dataList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Lista lista = document.toObject(Lista.class);
                        String titulo = lista.getTitulo();
                        String fecha = lista.getFecha();
                        String imageUrl = lista.getImagen1();
                        String descripcion = lista.getDescripcion();
                        String lugar = lista.getLugar();

                        dataList.add(new Lista(titulo, fecha, imageUrl, descripcion, lugar));
                    }

                    adapter.setDataList(dataList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Delactprincipal", "Error al obtener los eventos", e);
                });

        ImageView addImage = findViewById(R.id.imageView21);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Delactprincipal.this, NuevoEvento.class);
                startActivity(intent);
            }
        });
    }

    private void filterData(String searchText) {
        List<Lista> filteredList = new ArrayList<>();

        for (Lista lista : dataList) {
            if (lista.getTitulo().toLowerCase().contains(searchText)) {
                filteredList.add(lista);
            }
        }

        adapter.setDataList(filteredList);
    }
}