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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.adapter.NotificacionesAdapter;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.Notificacion;
import com.example.grupo_iot.databinding.ActivityNotificacionesBinding;
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

public class NotificacionesActivity extends AppCompatActivity {
    ActivityNotificacionesBinding binding;
    DrawerLayout drawerLayout;
    Alumno alumno;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificacionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        alumno = (Alumno) intent.getSerializableExtra("alumno");

        buscarDatosAlumnos(alumno.getEmail());
        cargarNotificaciones();
        generarBottomNavigationMenu();

        binding.imageView6.setOnClickListener(view -> {
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

    private void cargarNotificaciones(){
        CollectionReference alumnosCollection = db.collection("alumnos");
        DocumentReference alumnoDocument = alumnosCollection.document(alumno.getCodigo());
        CollectionReference notificacionesCollection = alumnoDocument.collection("notificaciones");

        notificacionesCollection
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Notificacion> notificacionList = new ArrayList<>();
                        for (QueryDocumentSnapshot notif : task.getResult()){
                            Notificacion notificacion = notif.toObject(Notificacion.class);
                            notificacionList.add(notificacion);
                        }
                        NotificacionesAdapter notificacionesAdapter = new NotificacionesAdapter();
                        notificacionesAdapter.setNotificacionList(notificacionList);
                        notificacionesAdapter.setContext(NotificacionesActivity.this);
                        notificacionesAdapter.setAlumno(alumno);
                        binding.recyclerViewNotificaciones.setAdapter(notificacionesAdapter);
                        binding.recyclerViewNotificaciones.setLayoutManager(new LinearLayoutManager(NotificacionesActivity.this));
                    }
                });
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
                                //Log.d("msg-test", a.getNombre());
                                alumno = a;
                                generarSidebar();
                                break;
                            }
                        }
                    }
                });
    }

    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_actividades){
                    Intent intent = new Intent(NotificacionesActivity.this, ListaActividadesActivity.class);
                    intent.putExtra("alumno",alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(NotificacionesActivity.this, ListaEventosApoyadosActivity.class);
                    intent.putExtra("alumno",alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(NotificacionesActivity.this, DonacionesActivity.class);
                    intent.putExtra("alumno",alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(NotificacionesActivity.this, PerfilActivity.class);
                    intent.putExtra("alumno",alumno);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

}