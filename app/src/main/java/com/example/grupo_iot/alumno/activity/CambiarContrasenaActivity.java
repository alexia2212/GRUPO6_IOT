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
import android.widget.TextView;

import com.example.grupo_iot.IniciarSesionActivity;
import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.Usuario;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.databinding.ActivityCambiarContrasenaBinding;
import com.example.grupo_iot.delactividad.Delactprincipal;
import com.example.grupo_iot.delegadoGeneral.MenuDelegadoGeneralActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class CambiarContrasenaActivity extends AppCompatActivity {
    ActivityCambiarContrasenaBinding binding;
    FirebaseFirestore db;
    DrawerLayout drawerLayout;
    Alumno alumno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCambiarContrasenaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        //String password = intent.getStringExtra("password");
        alumno = (Alumno) intent.getSerializableExtra("alumno");
        buscarDatosAlumnos(alumno.getEmail());
        generarBottomNavigationMenu();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_perfil);

        binding.textView22.setOnClickListener(view -> {
            db.collection("alumnos")
                    .document(alumno.getCodigo())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Alumno alumno1 = document.toObject(Alumno.class);
                                boolean psswdValido = cambiarContrasena(alumno1.getPassword());
                                if(psswdValido){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                    builder.setMessage("Contraseña cambiada exitosamente.")
                                            .setTitle("Aviso")
                                            .setPositiveButton("Aceptar", (dialog, which) -> {
                                                Intent intent1 = new Intent(this, PerfilActivity.class);
                                                intent1.putExtra("alumno", alumno1);
                                                startActivity(intent1);
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            } else {
                                Log.d("nsg-test", "No such document");
                            }
                        } else {
                            Log.d("msg-test", "get failed with ", task.getException());
                        }
                    });
        });
    }

    public boolean cambiarContrasena(String passw){
        boolean psswdValido = false;
        String contra = ((TextInputEditText) binding.inputContra.getEditText()).getText().toString();
        String nuevaContra = ((TextInputEditText) binding.inputNuevaContra.getEditText()).getText().toString();
        String repeNuevaContra = ((TextInputEditText) binding.inputRepeatContra.getEditText()).getText().toString();

        if(contra.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Debe ingresar la contraseña actual.")
                    .setTitle("Aviso")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            if(!contra.equals(passw)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("La contraseña actual ingresada es incorrecta.")
                        .setTitle("Aviso")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                if(nuevaContra.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Debe ingresar la nueva contraseña.")
                            .setTitle("Aviso")
                            .setPositiveButton("Aceptar", (dialog, which) -> {
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    if(!repeNuevaContra.equals(nuevaContra)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("La contraseña nueva no coincide.")
                                .setTitle("Aviso")
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else{
                        psswdValido = true;
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("password", nuevaContra);

                        CollectionReference alumnosCollection = db.collection("alumnos");
                        DocumentReference alumnoDocument = alumnosCollection.document(alumno.getCodigo());
                        alumnoDocument
                                .update(updates)
                                .addOnSuccessListener(unused -> {
                                    Log.d("msg-test","Se cambió contraseña exitosamente.");
                                })
                                .addOnFailureListener(e -> e.printStackTrace());

                        Query query = db.collection("credenciales").whereEqualTo("email",alumno.getEmail());
                        query.get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        DocumentReference docRef = document.getReference();
                                        docRef.update(updates)
                                                .addOnSuccessListener(aVoid -> {
                                                    Log.d("msg-test","Se cambió contraseña exitosamente.");
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Ocurrió un error al intentar actualizar el campo "password".
                                                });
                                    }
                                });
                    }
                }
            }
        }
        return psswdValido;
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
        usuario.setText(alumno.getNombre()+" "+alumno.getApellido());
        estado.setText(alumno.getCondicion());

        binding.logoutContainer.setOnClickListener(view -> {
            Intent intent = new Intent(this, NotificacionesActivity.class);
            intent.putExtra("alumno", alumno);
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
                                alumno = a;
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
                    Intent intent = new Intent(CambiarContrasenaActivity.this, ListaActividadesActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(CambiarContrasenaActivity.this, ListaEventosApoyadosActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    Intent intent = new Intent(CambiarContrasenaActivity.this, ListaDeChatsActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(CambiarContrasenaActivity.this, DonacionesActivity.class);
                    intent.putExtra("alumno", alumno);
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