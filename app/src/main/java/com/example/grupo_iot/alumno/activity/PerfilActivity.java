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
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.databinding.ActivityPerfilBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class PerfilActivity extends AppCompatActivity {
    ActivityPerfilBinding binding;
    DrawerLayout drawerLayout;
    Alumno alumnoIngresado;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        alumnoIngresado = (Alumno) intent.getSerializableExtra("alumno");

        String nombreAlumno = alumnoIngresado.getNombre();
        String apellidoAlumno = alumnoIngresado.getApellido();
        String codigoAlumno = alumnoIngresado.getCodigo();
        String condicionAlumno = alumnoIngresado.getCondicion();
        String correoAlumno = alumnoIngresado.getEmail();

        binding.textViewNombre.setText(nombreAlumno+" "+apellidoAlumno);
        binding.textViewCodigoPUCP.setText(codigoAlumno);
        binding.textViewCondicion.setText(condicionAlumno);
        binding.textViewCorreo.setText(correoAlumno);

        buscarDatosAlumnos(alumnoIngresado.getEmail());
        generarBottomNavigationMenu();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_perfil);

        binding.btnCambiarContrasena.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, CambiarContrasenaActivity.class);
            intent1.putExtra("alumno", alumnoIngresado);
            startActivity(intent1);
        });
    }

    public void generarSidebar(){
        ImageView abrirSidebar = findViewById(R.id.imageView5);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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

        View headerView = navigationView.getHeaderView(0);
        //ImageView imageView12 = headerView.findViewById(R.id.imageView12);
        TextView usuario = headerView.findViewById(R.id.textView6);
        TextView estado = headerView.findViewById(R.id.estado);

        //imageView12.setImageResource(R.mipmap.perfil1);
        usuario.setText(alumnoIngresado.getNombre()+" "+alumnoIngresado.getApellido());
        estado.setText(alumnoIngresado.getCondicion());

        binding.logoutContainer.setOnClickListener(view -> {
            Intent intent = new Intent(this, NotificacionesActivity.class);
            intent.putExtra("alumno", alumnoIngresado);
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
                                alumnoIngresado = a;
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
                    Intent intent = new Intent(PerfilActivity.this, ListaActividadesActivity.class);
                    intent.putExtra("alumno", alumnoIngresado);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(PerfilActivity.this, ListaEventosApoyadosActivity.class);
                    intent.putExtra("alumno", alumnoIngresado);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    Intent intent = new Intent(PerfilActivity.this, ListaDeChatsActivity.class);
                    intent.putExtra("alumno", alumnoIngresado);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(PerfilActivity.this, DonacionesActivity.class);
                    intent.putExtra("alumno", alumnoIngresado);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    menuItem.setEnabled(false);
                    menuItem.setChecked(true);
                }
                return true;
            }
        });
    }
}