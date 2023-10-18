package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.adapter.ListaActividadesAdapter;
import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.databinding.ActivityListaActividadesAlumnoBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ListaActividadesActivity extends AppCompatActivity {
    ActivityListaActividadesAlumnoBinding binding;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaActividadesAlumnoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<String> eventos = new ArrayList<>();
        eventos.add("Evento");

        generarSidebar();
        generarBottomNavigationMenu();

    }

    @Override
    protected void onStart(){
        super.onStart();
        cargarDataActividades(); //Se carga la data de las actividades (nombre y descripcion) al adapter correspondiente
    }

    public void cargarDataActividades (){

        List<Actividad> actividadList = new ArrayList<>();
        Actividad actividad1 = new Actividad();
        actividad1.setNombreActividad("Baile Cultural");
        actividad1.setDescripcionActividad("Participa en la danza cultural para apoyar a la Fibra, no te lo pierdas.");
        actividad1.setImagenActividad(R.mipmap.evento1);

        Actividad actividad2 = new Actividad();
        actividad2.setNombreActividad("Futsal Damas");
        actividad2.setDescripcionActividad("No te pierdas el debut de la Fibra en futsal damas el próximo lunes.");
        actividad2.setImagenActividad(R.mipmap.evento2);

        Actividad actividad3 = new Actividad();
        actividad3.setNombreActividad("Voley Mixto");
        actividad3.setDescripcionActividad("No te pierdas el debut de la Fibra en voley mixto el próximo martes.");
        actividad3.setImagenActividad(R.mipmap.evento3);

        Actividad actividad4 = new Actividad();
        actividad4.setNombreActividad("Futsal Varones");
        actividad4.setDescripcionActividad("No te pierdas el debut de la Fibra en futsal varones el próximo miercoles.");
        actividad4.setImagenActividad(R.mipmap.evento4);

        Actividad actividad5 = new Actividad();
        actividad5.setNombreActividad("Ajedrez");
        actividad5.setDescripcionActividad("No te pierdas el debut de la Fibra en ajedrez el próximo jueves.");
        actividad5.setImagenActividad(R.mipmap.evento5);

        Actividad actividad6 = new Actividad();
        actividad6.setNombreActividad("Bailetón");
        actividad6.setDescripcionActividad("No te pierdas el debut de la Fibra en bailetón el próximo viernes.");
        actividad6.setImagenActividad(R.mipmap.evento6);

        actividadList.add(actividad1);
        actividadList.add(actividad2);
        actividadList.add(actividad3);
        actividadList.add(actividad4);
        actividadList.add(actividad5);
        actividadList.add(actividad6);

        ListaActividadesAdapter listaActividadesAdapter = new ListaActividadesAdapter();
        listaActividadesAdapter.setActividadList(actividadList);
        listaActividadesAdapter.setContext(ListaActividadesActivity.this);

        binding.recyclerViewListaActividades.setAdapter(listaActividadesAdapter);
        binding.recyclerViewListaActividades.setLayoutManager(new LinearLayoutManager(ListaActividadesActivity.this));
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
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}