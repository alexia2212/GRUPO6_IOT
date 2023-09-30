package com.example.grupo_iot.alumno;

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

import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.entity.Evento;
import com.example.grupo_iot.databinding.ActivityMenuEventosBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MenuEventosActivity extends AppCompatActivity {

    ActivityMenuEventosBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuEventosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<String> eventos = new ArrayList<>();
        eventos.add("Evento");

        // SECCION SIDEBAR
        ImageView abrirSidebar = findViewById(R.id.imageView6);
        //ImageView cerrarSidebar = findViewById(R.id.cerrarSidebar);
        drawerLayout = findViewById(R.id.drawer_layout);
        abrirSidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
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

        //Opciones navigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.menu_option_1){
                    Intent intent = new Intent(MenuEventosActivity.this, MenuEventosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.menu_option_2){
                    Intent intent = new Intent(MenuEventosActivity.this, ListaEventosApoyadosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.menu_option_3){
                    Intent intent = new Intent(MenuEventosActivity.this, DonacionesActivity.class);
                    startActivity(intent);
                }
                //Cierra el sidebar después de la selección
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //FIN SIDEBAR
    }

    @Override
    protected void onStart(){
        super.onStart();
        cargarDataEvento(); //Se carga la data del evento (nombre y descripcion) al adapter correspondiente
    }

    public void cargarDataEvento (){

        List<Evento> eventoList = new ArrayList<>();
        Evento evento1 = new Evento();
        evento1.setNombreEvento("Baile Cultural");
        evento1.setDescripcionEvento("Participa en la danza cultural para apoyar a la Fibra, no te lo pierdas.");
        evento1.setImagenEvento(R.mipmap.evento1);

        Evento evento2 = new Evento();
        evento2.setNombreEvento("Futsal Damas");
        evento2.setDescripcionEvento("No te pierdas el debut de la Fibra en futsal damas el próximo lunes.");
        evento2.setImagenEvento(R.mipmap.evento2);

        Evento evento3 = new Evento();
        evento3.setNombreEvento("Voley Mixto");
        evento3.setDescripcionEvento("No te pierdas el debut de la Fibra en voley mixto el próximo martes.");
        evento3.setImagenEvento(R.mipmap.evento3);

        Evento evento4 = new Evento();
        evento4.setNombreEvento("Futsal Varones");
        evento4.setDescripcionEvento("No te pierdas el debut de la Fibra en futsal varones el próximo miercoles.");
        evento4.setImagenEvento(R.mipmap.evento4);

        Evento evento5 = new Evento();
        evento5.setNombreEvento("Ajedrez");
        evento5.setDescripcionEvento("No te pierdas el debut de la Fibra en ajedrez el próximo jueves.");
        evento5.setImagenEvento(R.mipmap.evento5);

        Evento evento6 = new Evento();
        evento6.setNombreEvento("Bailetón");
        evento6.setDescripcionEvento("No te pierdas el debut de la Fibra en bailetón el próximo viernes.");
        evento6.setImagenEvento(R.mipmap.evento6);

        eventoList.add(evento1);
        eventoList.add(evento2);
        eventoList.add(evento3);
        eventoList.add(evento4);
        eventoList.add(evento5);
        eventoList.add(evento6);

        EventosAdapter eventosAdapter = new EventosAdapter();
        eventosAdapter.setListaEventos(eventoList);
        eventosAdapter.setContext(MenuEventosActivity.this);

        binding.recyclerViewListaEventos.setAdapter(eventosAdapter);
        binding.recyclerViewListaEventos.setLayoutManager(new LinearLayoutManager(MenuEventosActivity.this));
    }
    public void irEvento(View view){
        Intent intent = new Intent(this, EventoActivity.class);
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

}