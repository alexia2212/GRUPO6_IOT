package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.databinding.ActivityPerfilBinding;
import com.google.android.material.navigation.NavigationView;

public class PerfilActivity extends AppCompatActivity {

    ActivityPerfilBinding binding;
    DrawerLayout drawerLayout;
    Alumno alumnoIngresado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
/*
        generarSidebar(nombreAlumno, condicionAlumno, apellidoAlumno);

 */
        binding.btnCambiarContrasena.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, CambiarContrasenaActivity.class);
            intent1.putExtra("alumno", alumnoIngresado);
            startActivity(intent1);
        });
    }

    public void generarSidebar(String nombre, String condicion, String apellido){
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
        usuario.setText(nombre+" "+apellido);
        //usuario.setText("####");
        //Log.d("msg-test", alumno.getNombre());
        estado.setText(condicion);
    }
}