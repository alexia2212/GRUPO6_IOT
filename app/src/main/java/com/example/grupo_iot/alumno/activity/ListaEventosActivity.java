package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.adapter.ListaActividadesAdapter;
import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.databinding.ActivityListaEventosAlumnoBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaEventosActivity extends AppCompatActivity {
    ActivityListaEventosAlumnoBinding binding;
    private DrawerLayout drawerLayout;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaEventosAlumnoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String nombreActividad = intent.getStringExtra("nombreActividad");
        String descripcionActividad = intent.getStringExtra("descripcionActividad");
        String nombreImagen  = intent.getStringExtra("imagenActividad");


        TextView textViewNombreEvento = findViewById(R.id.textView2);
        //TextView textViewDescripcionEvento = findViewById(R.id.textViewDescripcionEvento);
        ImageView imageViewEvento = findViewById(R.id.imageView2);

        textViewNombreEvento.setText(nombreActividad);
        //textViewDescripcionEvento.setText(descripcionEvento);

        // Carga la imagen basada en el nombre del recurso
        int resourceId = getResources().getIdentifier(nombreImagen , "drawable", getPackageName());

        if (resourceId != 0) {
            imageViewEvento.setImageResource(resourceId);
        }

        cargarListaEventos();
        generarSidebar();
        generarBottomNavigationMenu();

        binding.textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ListaEventosActivity.this, EventoActivity.class);
                intent1.putExtra("nombreEvento", binding.textView4.getText());
                //intent1.putExtra("imgEvento", imagenActividad);
                startActivity(intent1);
            }
        });
    }

    public void cargarListaEventos(){

    }

    public void irMensajeria(View view){
        Intent intent = new Intent(this, ListaDeChatsActivity.class);
        startActivity(intent);
    }

    public void abrirNotificaciones(View view){
        Intent intent = new Intent(this, NotificacionesActivity.class);
        startActivity(intent);
    }

    public void confirmarApoyo(View view){
        Intent intent = new Intent(this, ConfirmacionApoyoActivity.class);
        startActivity(intent);
    }

    public void verRutaMasCorta(View view){
        Intent intent = new Intent(this, RutaMasCortaActivity.class);
        startActivity(intent);
    }

    public void generarSidebar(){
        ImageView abrirSidebar = findViewById(R.id.imageView5);
        //ImageView cerrarSidebar = findViewById(R.id.cerrarSidebar);
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
        /*
        cerrarSidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra el sidebar al hacer clic en el botón "Cerrar Sidebar"
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });*/

        /*
        //Opciones navigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.menu_notif){

                }
                if(menuItem.getItemId()==R.id.menu_option_1){

                }
                if(menuItem.getItemId()==R.id.menu_option_2){

                }
                if(menuItem.getItemId()==R.id.menu_option_3){

                }

                //Cierra el sidebar después de la selección
                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });

         */
    }

    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_actividades){
                    Intent intent = new Intent(ListaEventosActivity.this, ListaActividadesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(ListaEventosActivity.this, ListaEventosApoyadosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    Intent intent = new Intent(ListaEventosActivity.this, ListaDeChatsActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(ListaEventosActivity.this, DonacionesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(ListaEventosActivity.this, EditarPerfilActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}