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
import com.example.grupo_iot.databinding.ActivityEventoApoyadoBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class EventoApoyadoActivity extends AppCompatActivity {

    ActivityEventoApoyadoBinding binding;
    DrawerLayout drawerLayout;
    Alumno alumno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventoApoyadoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        generarSidebar();
        generarBottomNavigationMenu();

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

        binding.textView40.setText(nombreActividad);
        binding.textView7.setText(nombreEvento);
        binding.textView13.setText(descripcionEvento);
        binding.textView35.setText("Fecha: "+fechaEvento);
        binding.textView36.setText("Hora: "+horaEvento+" Hrs.");
        binding.textView37.setText("Lugar: "+lugarEvento);
        binding.textView3.setText("Apoyo: "+apoyo);

        ImageView imageViewEvento = findViewById(R.id.imageView26);

        // Carga la imagen basada en el nombre del recurso
        int resourceId = getResources().getIdentifier(nombreImagen , "drawable", getPackageName());
        if (resourceId != 0) {
            imageViewEvento.setImageResource(resourceId);
        }

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

    public void irMensajeria(View view){
        Intent intent = new Intent(this, ListaDeChatsActivity.class);
        startActivity(intent);
    }

    public void abrirNotificaciones(View view){
        Intent intent = new Intent(this, NotificacionesActivity.class);
        startActivity(intent);
    }

    public void verRutaMasCorta(View view){
        Intent intent = new Intent(this, RutaMasCortaActivity.class);
        startActivity(intent);
    }
    public void abrirChatGrupal(View view){
        Intent intent = new Intent(this, ChatGrupalActivity.class);
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
    }

    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_actividades){
                    Intent intent = new Intent(EventoApoyadoActivity.this, ListaActividadesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(EventoApoyadoActivity.this, ListaEventosApoyadosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    Intent intent = new Intent(EventoApoyadoActivity.this, ListaDeChatsActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(EventoApoyadoActivity.this, DonacionesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(EventoApoyadoActivity.this, VistaPreviaPerfil.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

}