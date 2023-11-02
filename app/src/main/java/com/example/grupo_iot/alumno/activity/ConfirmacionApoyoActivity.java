package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.databinding.ActivityConfirmacionApoyoBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class ConfirmacionApoyoActivity extends AppCompatActivity {

    ActivityConfirmacionApoyoBinding binding;
    DrawerLayout drawerLayout;

    Alumno alumno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmacionApoyoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String nombreActividad = intent.getStringExtra("nombreActividad");
        String nombreEvento = intent.getStringExtra("nombreEvento");
        String descripcionEvento = intent.getStringExtra("descripcionEvento");
        String fechaEvento = intent.getStringExtra("fechaEvento");
        String horaEvento = intent.getStringExtra("horaEvento");
        String lugarEvento = intent.getStringExtra("lugarEvento");
        String nombreImagen  = intent.getStringExtra("idImagenEvento");
        String apoyo = intent.getStringExtra("apoyo");
        alumno = (Alumno) intent.getSerializableExtra("alumno");

        generarSidebar();
        generarBottomNavigationMenu();

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

        binding.textView17.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, EventoApoyadoActivity.class);
            intent1.putExtra("nombreActividad", nombreActividad);
            intent1.putExtra("nombreEvento", nombreEvento);
            intent1.putExtra("descripcionEvento", descripcionEvento);
            intent1.putExtra("lugarEvento", lugarEvento);
            intent1.putExtra("idImagenEvento",nombreImagen);
            intent1.putExtra("fechaEvento", fechaEvento);
            intent1.putExtra("horaEvento", horaEvento);
            intent1.putExtra("apoyo", apoyo);
            intent1.putExtra("alumno", alumno);
            startActivity(intent1);
        });
    }

    public void irEvento(View view){
        Intent intent = new Intent(this, EventoApoyadoActivity.class);
        startActivity(intent);
    }

    public void irInicio(View view){
        Intent intent = new Intent(this, ListaActividadesActivity.class);
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
                    Intent intent = new Intent(ConfirmacionApoyoActivity.this, ListaActividadesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(ConfirmacionApoyoActivity.this, ListaEventosApoyadosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    Intent intent = new Intent(ConfirmacionApoyoActivity.this, ListaDeChatsActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(ConfirmacionApoyoActivity.this, DonacionesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(ConfirmacionApoyoActivity.this, VistaPreviaPerfil.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

}