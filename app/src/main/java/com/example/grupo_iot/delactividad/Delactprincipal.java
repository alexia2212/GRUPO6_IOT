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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.activity.ChatGrupalActivity;
import com.example.grupo_iot.alumno.activity.DonacionesActivity;
import com.example.grupo_iot.alumno.activity.ListaActividadesActivity;
import com.example.grupo_iot.alumno.activity.ListaDeChatsActivity;
import com.example.grupo_iot.alumno.activity.ListaEventosApoyadosActivity;
import com.example.grupo_iot.alumno.activity.PerfilActivity;
import com.example.grupo_iot.alumno.entity.Evento;
import com.example.grupo_iot.databinding.ActivityCompartirfotosBinding;
import com.example.grupo_iot.databinding.ActivityDelactprincipalBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class Delactprincipal extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth auth;

    ActivityDelactprincipalBinding binding;

    private Adaptador adapter;
    private List<Lista> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDelactprincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        dataList = new ArrayList<>();
        adapter = new Adaptador(dataList);


        generarBottomNavigationMenu();

        RecyclerView recyclerView = findViewById(R.id.eventos);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        binding.imageViewsalir.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setTitle("Aviso")
                    .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                        auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();
                    })
                    .setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        /*StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        StorageReference imagenRef = storageRef.child("imagenes/imagenpordefecto.jpg");

        imagenRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imagenUrl = uri.toString();
            System.out.println("URL de la imagen: " + imagenUrl);

        }).addOnFailureListener(exception -> {
        });*/


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
                                        String nombreActividad = lista.getNombreactividad();
                                        String estado = lista.getEstado();

                                        dataList.add(new Lista(titulo, fecha, imageUrl, descripcion, lugar, nombreActividad, estado));
                                    }

                                    adapter.setDataList(dataList);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Delactprincipal", "Error al obtener los eventos", e);
                                });
                    } else {
                        // El documento no existe, manejar según sea necesario
                        Log.e("Delactprincipal", "El documento de credenciales no existe para el usuario " + userID);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Delactprincipal", "Error al obtener las credenciales", e);
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
            if (lista.getTitulo() != null && lista.getTitulo().toLowerCase().contains(searchText)) {
                filteredList.add(lista);
            }
        }

        adapter.setDataList(filteredList);
    }



    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_eventos){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(Delactprincipal.this, Delactprincipal.class);
                    startActivity(intent);

                }

                if(menuItem.getItemId()==R.id.navigation_eventos_finalizados){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(Delactprincipal.this, EventoFinalizadoActivity.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(Delactprincipal.this, Chatdelact.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(Delactprincipal.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}