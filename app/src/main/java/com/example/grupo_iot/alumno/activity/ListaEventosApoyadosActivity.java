package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.grupo_iot.alumno.adapter.ListaEventosApoyadosAdapter;
import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.Evento;
import com.example.grupo_iot.alumno.entity.EventoApoyado;
import com.example.grupo_iot.databinding.ActivityEventoApoyadoBinding;
import com.example.grupo_iot.databinding.ActivityEventosApoyadosBinding;
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

public class ListaEventosApoyadosActivity extends AppCompatActivity {
    ActivityEventosApoyadosBinding binding;
    DrawerLayout drawerLayout;
    FirebaseFirestore db;
    Alumno alumno;
    List<EventoApoyado> listaEventosApoyadosCompleta = new ArrayList<>();
    List<EventoApoyado> eventoApoyadoBarraList = new ArrayList<>();
    List<EventoApoyado> eventoApoyadoParticipanteList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventosApoyadosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        alumno = (Alumno) intent.getSerializableExtra("alumno");
        buscarDatosAlumnos(alumno.getEmail());
        generarBottomNavigationMenu();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_eventos_apoyados);

        cargarEventosApoyados();

        binding.textViewOpcionBarra.setOnClickListener(view -> {
            cargarEventosApoyadosOpcionBarra();
        });

        binding.textViewOpcionParticipante.setOnClickListener(view -> {
            cargarEventosApoyadosOpcionParticipante();
        });

        binding.imageView6.setOnClickListener(view -> {
            cerrarSesion();
        });
    }

    public void cargarEventosApoyados (){
        CollectionReference alumnosCollection = db.collection("alumnos");
        DocumentReference alumnoDocument = alumnosCollection.document(alumno.getCodigo());
        CollectionReference listaEventosApoyadosCollection = alumnoDocument.collection("listaEventosApoyados");

        listaEventosApoyadosCollection
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<EventoApoyado> eventoApoyadoParticList = new ArrayList<>();
                        for (QueryDocumentSnapshot evento : task.getResult()) {
                            EventoApoyado event = evento.toObject(EventoApoyado.class);
                            listaEventosApoyadosCompleta.add(event);
                            if(event.getApoyo().equals("Participante")){
                                eventoApoyadoParticList.add(event);
                            }else{
                                if(event.getApoyo().equals("Barra")){
                                    eventoApoyadoBarraList.add(event);
                                }
                            }
                        }
                        eventoApoyadoParticipanteList = eventoApoyadoParticList;

                        ListaEventosApoyadosAdapter listaEventosApoyadosAdapter = new ListaEventosApoyadosAdapter();
                        listaEventosApoyadosAdapter.setEventoApoyadoList(eventoApoyadoParticList);
                        listaEventosApoyadosAdapter.setContext(ListaEventosApoyadosActivity.this);
                        listaEventosApoyadosAdapter.setAlumno(alumno);
                        binding.recyclerViewListaEventosApoyados.setAdapter(listaEventosApoyadosAdapter);
                        binding.recyclerViewListaEventosApoyados.setLayoutManager(new LinearLayoutManager(ListaEventosApoyadosActivity.this));
                    }
                });
    }

    public void cargarEventosApoyadosOpcionParticipante(){
        TextView textViewOpcionBarra = binding.textViewOpcionBarra;
        TextView textViewOpcionParticipante = binding.textViewOpcionParticipante;

        textViewOpcionBarra.setBackgroundResource(R.drawable.background5);
        textViewOpcionBarra.setTextColor(ContextCompat.getColor(this, android.R.color.black));

        textViewOpcionParticipante.setBackgroundResource(R.drawable.background11);
        textViewOpcionParticipante.setTextColor(ContextCompat.getColor(this, android.R.color.white));

        ListaEventosApoyadosAdapter listaEventosApoyadosAdapter = new ListaEventosApoyadosAdapter();
        listaEventosApoyadosAdapter.setEventoApoyadoList(eventoApoyadoParticipanteList);
        listaEventosApoyadosAdapter.setContext(ListaEventosApoyadosActivity.this);
        listaEventosApoyadosAdapter.setAlumno(alumno);
        binding.recyclerViewListaEventosApoyados.setAdapter(listaEventosApoyadosAdapter);
        binding.recyclerViewListaEventosApoyados.setLayoutManager(new LinearLayoutManager(ListaEventosApoyadosActivity.this));
        listaEventosApoyadosAdapter.notifyDataSetChanged();
    }

    public void cargarEventosApoyadosOpcionBarra(){
        TextView textViewOpcionBarra = binding.textViewOpcionBarra;
        TextView textViewOpcionParticipante = binding.textViewOpcionParticipante;

        textViewOpcionBarra.setBackgroundResource(R.drawable.background11);
        textViewOpcionBarra.setTextColor(ContextCompat.getColor(this, android.R.color.white));

        textViewOpcionParticipante.setBackgroundResource(R.drawable.background5);
        textViewOpcionParticipante.setTextColor(ContextCompat.getColor(this, android.R.color.black));

        ListaEventosApoyadosAdapter listaEventosApoyadosAdapter = new ListaEventosApoyadosAdapter();
        listaEventosApoyadosAdapter.setEventoApoyadoList(eventoApoyadoBarraList);
        listaEventosApoyadosAdapter.setContext(ListaEventosApoyadosActivity.this);
        listaEventosApoyadosAdapter.setAlumno(alumno);
        binding.recyclerViewListaEventosApoyados.setAdapter(listaEventosApoyadosAdapter);
        binding.recyclerViewListaEventosApoyados.setLayoutManager(new LinearLayoutManager(ListaEventosApoyadosActivity.this));
        listaEventosApoyadosAdapter.notifyDataSetChanged();
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
                    Intent intent = new Intent(ListaEventosApoyadosActivity.this, ListaActividadesActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    menuItem.setEnabled(false);
                    menuItem.setChecked(true);
                }

                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(ListaEventosApoyadosActivity.this, DonacionesActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(ListaEventosApoyadosActivity.this, PerfilActivity.class);
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