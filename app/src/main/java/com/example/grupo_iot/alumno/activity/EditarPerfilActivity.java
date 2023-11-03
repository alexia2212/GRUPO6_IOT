package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.Usuario;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.databinding.ActivityEditarPerfilBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

public class EditarPerfilActivity extends AppCompatActivity {
    ActivityEditarPerfilBinding binding;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        generarSidebar();
        generarBottomNavigationMenu();

        Intent intent = getIntent();
        Alumno alumno = (Alumno) intent.getSerializableExtra("alumno");

        TextInputLayout passwordTextInputLayout = binding.inputContra;
        TextInputLayout passwordTextRepeatInputLayout = binding.inputRepeatContra;

        passwordTextInputLayout.getEditText().setText(alumno.getPassword());
        passwordTextRepeatInputLayout.getEditText().setText(alumno.getPassword());

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
                    Intent intent = new Intent(EditarPerfilActivity.this, ListaActividadesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(EditarPerfilActivity.this, ListaEventosApoyadosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    Intent intent = new Intent(EditarPerfilActivity.this, ListaDeChatsActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(EditarPerfilActivity.this, DonacionesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(EditarPerfilActivity.this, PerfilActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}