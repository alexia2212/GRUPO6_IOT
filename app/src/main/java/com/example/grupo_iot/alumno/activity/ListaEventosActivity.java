package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityListaEventosAlumnoBinding;
import com.google.android.material.navigation.NavigationView;

public class ListaEventosActivity extends AppCompatActivity {
    ActivityListaEventosAlumnoBinding binding;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaEventosAlumnoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String nombreActividad = intent.getStringExtra("nombreActividad");
        String descripcionActividad = intent.getStringExtra("descripcionActividad");
        int imagenActividad = intent.getIntExtra("imagenActividad", 0);


        TextView textViewNombreEvento = findViewById(R.id.textView2);
        //TextView textViewDescripcionEvento = findViewById(R.id.textViewDescripcionEvento);
        ImageView imageViewEvento = findViewById(R.id.imageView2);

        textViewNombreEvento.setText(nombreActividad);
        //textViewDescripcionEvento.setText(descripcionEvento);

        if (imagenActividad != 0) {
            imageViewEvento.setImageResource(imagenActividad);
        } else {
            // Si el ID de recurso no es válido, puedes establecer una imagen de error predeterminada o hacer algo apropiado.
        }

        generarSidebar();

        binding.textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ListaEventosActivity.this, EventoActivity.class);
                intent1.putExtra("nombreEvento", binding.textView4.getText());
                startActivity(intent1);
            }
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

    public void confirmarApoyo(View view){
        Intent intent = new Intent(this, ConfirmacionApoyoActivity.class);
        startActivity(intent);
    }

    public void verRutaMasCorta(View view){
        Intent intent = new Intent(this, RutaMasCortaActivity.class);
        startActivity(intent);
    }

    public void generarSidebar(){
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
                    Intent intent = new Intent(ListaEventosActivity.this, ListaActividadesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.menu_option_2){
                    Intent intent = new Intent(ListaEventosActivity.this, ListaEventosApoyadosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.menu_option_3){
                    Intent intent = new Intent(ListaEventosActivity.this, DonacionesActivity.class);
                    startActivity(intent);
                }
                //Cierra el sidebar después de la selección
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

}