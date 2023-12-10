package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.grupo_iot.databinding.ActivityListaActividadesAlumnoBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ListaActividadesActivity extends AppCompatActivity {
    ActivityListaActividadesAlumnoBinding binding;
    private DrawerLayout drawerLayout;
    FirebaseFirestore db;
    String correoAlumno;
    Alumno alumno;
    FirebaseAuth auth;
    List<Actividad> listaActividadesCompleta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaActividadesAlumnoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        if(intent.getStringExtra("correoAlumno")==null){
            alumno = (Alumno) intent.getSerializableExtra("alumno");
            buscarDatosAlumnos(alumno.getEmail());
        }else {
            correoAlumno = intent.getStringExtra("correoAlumno");
            buscarDatosAlumnos(correoAlumno);
        }

        generarBottomNavigationMenu();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_lista_actividades);

        cargarDataActividades();

        binding.btnBuscarEvento.setOnClickListener(view -> {
            realizarBusquedaEvento();
        });

        binding.imageView6.setOnClickListener(view -> {
            cerrarSesion();
        });

        String senderId = auth.getCurrentUser().getUid();

    }


    public void cargarDataActividades (){
        db.collection("actividades")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Actividad> actividadList = new ArrayList<>();
                        for (QueryDocumentSnapshot actividad : task.getResult()) {
                            Actividad activ = actividad.toObject(Actividad.class);
                            actividadList.add(activ);
                        }
                        listaActividadesCompleta = actividadList;
                        ListaActividadesAdapter listaActividadesAdapter = new ListaActividadesAdapter();
                        listaActividadesAdapter.setActividadList(actividadList);
                        listaActividadesAdapter.setContext(ListaActividadesActivity.this);
                        listaActividadesAdapter.setAlumno(alumno);
                        binding.recyclerViewListaActividades.setAdapter(listaActividadesAdapter);
                        binding.recyclerViewListaActividades.setLayoutManager(new LinearLayoutManager(ListaActividadesActivity.this));
                    }
                });
    }

    private void realizarBusquedaEvento() {
        String textoBusqueda = binding.buscarEvento.getEditText().getText().toString().toLowerCase();
        List<Actividad> actividadesFiltradas = new ArrayList<>();
        for (Actividad actividad : listaActividadesCompleta) {
            if (actividad.getNombreActividad().toLowerCase().contains(textoBusqueda)) {
                actividadesFiltradas.add(actividad);
            }
        }
        ListaActividadesAdapter listaActividadesFiltradasAdapter = new ListaActividadesAdapter();
        listaActividadesFiltradasAdapter.setActividadList(actividadesFiltradas);
        listaActividadesFiltradasAdapter.setContext(ListaActividadesActivity.this);
        listaActividadesFiltradasAdapter.setAlumno(alumno);
        binding.recyclerViewListaActividades.setAdapter(listaActividadesFiltradasAdapter);
        binding.recyclerViewListaActividades.setLayoutManager(new LinearLayoutManager(ListaActividadesActivity.this));
        listaActividadesFiltradasAdapter.notifyDataSetChanged();
    }

    public void generarSidebar(){
        ImageView abrirSidebar = binding.imageView5;
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

        usuario.setText(alumno.getNombre()+" "+alumno.getApellido());
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
                    Intent intent = new Intent(ListaActividadesActivity.this, ListaEventosApoyadosActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    Intent intent = new Intent(ListaActividadesActivity.this, ListaDeChatsActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(ListaActividadesActivity.this, DonacionesActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(ListaActividadesActivity.this, PerfilActivity.class);
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