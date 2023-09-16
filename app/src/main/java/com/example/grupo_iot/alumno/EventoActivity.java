package com.example.grupo_iot.alumno;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.grupo_iot.R;
import com.google.android.material.navigation.NavigationView;

public class EventoActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        //SECCION SIDEBAR
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
                    Intent intent = new Intent(EventoActivity.this, MenuEventosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.menu_option_2){
                    Intent intent = new Intent(EventoActivity.this, ListaEventosApoyadosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.menu_option_3){
                    Intent intent = new Intent(EventoActivity.this, DonacionesActivity.class);
                    startActivity(intent);
                }
                //Cierra el sidebar después de la selección
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //FIN SIDEBAR

        //SECCION APOYAR EVENTO
        String[] listaOpciones = {"Apoyar evento", "Barra", "Participante"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, listaOpciones);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);


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

}