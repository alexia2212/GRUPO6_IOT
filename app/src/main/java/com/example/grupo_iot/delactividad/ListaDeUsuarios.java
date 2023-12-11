package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityCompartirfotosBinding;
import com.example.grupo_iot.databinding.ActivityListaDeUsuariosBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaDeUsuarios extends AppCompatActivity {

    FirebaseAuth auth;

    FirebaseFirestore db;
    private boolean sinFuncionSelected = false;


    ActivityListaDeUsuariosBinding binding;

    private AdaptadorUsuario adapter;

    private List<Usuario> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaDeUsuariosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        dataList = new ArrayList<>();

        Intent intent = getIntent();
        String documentoActualId = intent.getStringExtra("documentoActualId");
        System.out.println(documentoActualId + "pueda que si");

        adapter = new AdaptadorUsuario(dataList, documentoActualId);

        generarBottomNavigationMenu();

        RecyclerView recyclerView = findViewById(R.id.listauser);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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

        if (intent.hasExtra("documentoActualId")) {

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
                                    .document(documentoActualId)
                                    .collection("integrantes")
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        dataList.clear();

                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            Usuario lista = document.toObject(Usuario.class);
                                            String nombre = lista.getNombre();
                                            String apellido = lista.getApellido();
                                            String condicion = lista.getCondicion();
                                            String funcion = lista.getFuncion();
                                            String foto = lista.getFoto();
                                            String codigo = lista.getCodigo();
                                            String correo = lista.getCorreo();
                                            String idintegrante = lista.getIdintegrante();
                                            dataList.add(new Usuario(nombre, apellido, condicion, funcion, foto, codigo, correo, idintegrante));
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
        }

        ImageView addImage = findViewById(R.id.imageView21);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaDeUsuarios.this, NuevoEvento.class);
                startActivity(intent);
            }
        });





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
                    Intent intent = new Intent(ListaDeUsuarios.this, Delactprincipal.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.navigation_eventos_finalizados){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(ListaDeUsuarios.this, EventoFinalizadoActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(ListaDeUsuarios.this, Chatdelact.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser();
                    Intent intent = new Intent(ListaDeUsuarios.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private void animateColorChange(Button button, int endColor) {
        int startColor = button.getBackgroundTintList().getDefaultColor();

        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, getResources().getColor(endColor));
        colorAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        colorAnimator.setDuration(500);

        colorAnimator.addUpdateListener(animation -> {
            int animatedColor = (int) animation.getAnimatedValue();
            button.setBackgroundColor(animatedColor);
        });

        colorAnimator.start();
    }

    private void filterData(String searchText) {
        List<Usuario> filteredList = new ArrayList<>();

        for (Usuario lista : dataList) {
            if (lista.getNombre() != null && (lista.getNombre().toLowerCase().contains(searchText) ||
                    (lista.getApellido() != null && lista.getApellido().toLowerCase().contains(searchText)) || (lista.getFuncion() != null && lista.getFuncion().toLowerCase().contains(searchText)) )) {
                filteredList.add(lista);
            }
        }

        adapter.setDataList(filteredList);
    }



}
