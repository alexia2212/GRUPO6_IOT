package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.adapter.ListaActividadesAdapter;
import com.example.grupo_iot.alumno.adapter.ListaEventosAdapter;
import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.Evento;
import com.example.grupo_iot.databinding.ActivityListaEventosAlumnoBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ListaEventosActivity extends AppCompatActivity {
    ActivityListaEventosAlumnoBinding binding;
    private DrawerLayout drawerLayout;
    FirebaseFirestore db;
    String nombreActividad;
    String nombreImagen;
    String descripcionActividad;


    Alumno alumno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaEventosAlumnoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        nombreActividad = intent.getStringExtra("nombreActividad");
        descripcionActividad = intent.getStringExtra("descripcionActividad");
        nombreImagen  = intent.getStringExtra("imagenActividad");
        alumno = (Alumno) intent.getSerializableExtra("alumno");

        buscarDatosAlumnos(alumno.getEmail());
        generarBottomNavigationMenu();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_lista_actividades);

        TextView textViewNombreEvento = findViewById(R.id.textView2);
        ImageView imageViewEvento = findViewById(R.id.imageView2);

        textViewNombreEvento.setText(nombreActividad);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imgRef = firebaseStorage.getReference().child("img_actividades/"+nombreImagen+".png");

        Glide.with(this)
                .load(imgRef)
                .into(imageViewEvento);
/*
        // Carga la imagen basada en el nombre del recurso
        int resourceId = getResources().getIdentifier(nombreImagen , "drawable", getPackageName());

        if (resourceId != 0) {
            imageViewEvento.setImageResource(resourceId);
        }

 */

        cargarListaEventos();

        binding.imageView6.setOnClickListener(view -> {
            cerrarSesion();
        });
    }

    public void cargarListaEventos(){
        CollectionReference actividadesCollection = db.collection("actividades");
        DocumentReference actividadDocument = actividadesCollection.document(nombreActividad);
        CollectionReference listaEventosCollection = actividadDocument.collection("listaEventos");

        listaEventosCollection
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Evento> eventoList = new ArrayList<>();
                        for (QueryDocumentSnapshot evento : task.getResult()) {
                            Evento event = evento.toObject(Evento.class);
                            eventoList.add(event);
                        }
                        ListaEventosAdapter listaEventosAdapter = new ListaEventosAdapter();
                        listaEventosAdapter.setEventoList(eventoList);
                        listaEventosAdapter.setContext(ListaEventosActivity.this);
                        listaEventosAdapter.setIdImagenEvento(nombreImagen);
                        listaEventosAdapter.setActividad(nombreActividad);
                        listaEventosAdapter.setAlumno(alumno);
                        binding.recyclerViewListaEventos.setAdapter(listaEventosAdapter);
                        binding.recyclerViewListaEventos.setLayoutManager(new LinearLayoutManager(ListaEventosActivity.this));
                    }
                });
    }

    public void irMensajeria(View view){
        Intent intent = new Intent(this, ListaDeChatsActivity.class);
        intent.putExtra("alumno",alumno);
        startActivity(intent);
    }

    public void abrirNotificaciones(View view){
        Intent intent = new Intent(this, NotificacionesActivity.class);
        intent.putExtra("alumno",alumno);
        startActivity(intent);
    }

    public void confirmarApoyo(View view){
        Intent intent = new Intent(this, ConfirmacionApoyoActivity.class);
        intent.putExtra("alumno",alumno);
        startActivity(intent);
    }

    public void verRutaMasCorta(View view){
        Intent intent = new Intent(this, RutaMasCortaActivity.class);
        intent.putExtra("alumno",alumno);
        startActivity(intent);
    }

    public void generarSidebar(){
        ImageView abrirSidebar = findViewById(R.id.imageView5);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        abrirSidebar.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView usuario = headerView.findViewById(R.id.textView6);
        TextView estado = headerView.findViewById(R.id.estado);
        ImageView fotoPerfil = headerView.findViewById(R.id.imageViewFotoPerfil);

        String primerNombreApellido = alumno.getNombre().split("\\s+")[0] + " "+ alumno.getApellido().split("\\s+")[0];

        usuario.setText(primerNombreApellido);
        estado.setText(alumno.getCondicion());

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imgRef = firebaseStorage.getReference().child("img_perfiles/"+alumno.getNombre()+" "+alumno.getApellido()+".jpg");
        Glide.with(this)
                .load(imgRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(fotoPerfil);

        binding.logoutContainer.setOnClickListener(view -> {
            Intent intent = new Intent(this, NotificacionesActivity.class);
            intent.putExtra("alumno", alumno);
            startActivity(intent);
        });
    }

    public void buscarDatosAlumnos(String correo){
        db.collection("alumnos")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot user : task.getResult()) {
                            Alumno a = user.toObject(Alumno.class);
                            if(a.getEmail().equals(correo)){
                                alumno = a;
                                generarSidebar();
                                break;
                            }
                        }
                    }
                });
    }

    public void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.navigation_lista_actividades){
                    menuItem.setEnabled(false);
                    menuItem.setChecked(true);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(ListaEventosActivity.this, ListaEventosApoyadosActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(ListaEventosActivity.this, DonacionesActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(ListaEventosActivity.this, PerfilActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    public void cerrarSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setTitle("Aviso")
                .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                    Intent intent1 = new Intent(this, LoginActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                })
                .setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}