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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaActividadesActivity extends AppCompatActivity {
    ActivityListaActividadesAlumnoBinding binding;
    private DrawerLayout drawerLayout;
    FirebaseFirestore db;
    String correoAlumno;
    Alumno alumno = new Alumno();
    List<Actividad> listaActividadesCompleta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaActividadesAlumnoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
/*
        ArrayList<String> eventos = new ArrayList<>();
        eventos.add("Evento");

 */
        Intent intent = getIntent();
        correoAlumno = intent.getStringExtra("correoAlumno");
        buscarDatosAlumnos(correoAlumno);

        cargarDataActividades();
        generarSidebar();
        generarBottomNavigationMenu();

        binding.btnBuscarEvento.setOnClickListener(view -> {
            realizarBusquedaEvento();
        });

        binding.imageView6.setOnClickListener(view -> {
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
    }

    @Override
    protected void onStart(){
        super.onStart();
         //Se carga la data de las actividades (nombre y descripcion) al adapter correspondiente
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
        for(Actividad a : actividadesFiltradas){
            Log.d("msg-test",a.getNombreActividad());
            Log.d("msg-test",a.getIdImagenActividad());
        }

        ListaActividadesAdapter listaActividadesFiltradasAdapter = new ListaActividadesAdapter();
        listaActividadesFiltradasAdapter.setActividadList(actividadesFiltradas);
        listaActividadesFiltradasAdapter.setContext(ListaActividadesActivity.this);
        binding.recyclerViewListaActividades.setAdapter(listaActividadesFiltradasAdapter);
        binding.recyclerViewListaActividades.setLayoutManager(new LinearLayoutManager(ListaActividadesActivity.this));
        listaActividadesFiltradasAdapter.notifyDataSetChanged();
    }


    public void irEvento(View view){
        Intent intent = new Intent(this, ListaEventosActivity.class);
        startActivity(intent);
    }

    public void irMensajeria(View view){
        Intent intent = new Intent(this, ListaDeChatsActivity.class);
        startActivity(intent);
    }

    public void abrirNotificaciones(View view){
        Intent intent = new Intent(this, NotificacionesActivity.class);
        startActivity(intent);
    }

    public void generarSidebar(){
        ImageView abrirSidebar = findViewById(R.id.imageView5);
        drawerLayout = findViewById(R.id.drawer_layout);
        abrirSidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
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
                    Intent intent = new Intent(ListaActividadesActivity.this, ListaActividadesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(ListaActividadesActivity.this, ListaEventosApoyadosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    Intent intent = new Intent(ListaActividadesActivity.this, ListaDeChatsActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(ListaActividadesActivity.this, DonacionesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(ListaActividadesActivity.this, EditarPerfilActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}