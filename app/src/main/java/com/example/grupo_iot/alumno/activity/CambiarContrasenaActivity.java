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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.grupo_iot.EmailSender;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class CambiarContrasenaActivity extends AppCompatActivity {
    ActivityCambiarContrasenaBinding binding;
    FirebaseFirestore db;
    DrawerLayout drawerLayout;
    Alumno alumno;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCambiarContrasenaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
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
                                                String subjectChangePwd = "Cambio de contraseña exitoso";
                                                String resetPasswordMessage = "La contraseña de la cuenta se ha restablecido recientemente. " +
                                                        "Si ha realizado este restablecimiento de contraseña, este mensaje es solo para su información.\n\n" +
                                                        "Id. de usuario: " + alumno.getEmail() + "\n\n" +
                                                        "Si no está seguro de si usted o su administrador ha realizado este restablecimiento de contraseña, " +
                                                        "debe ponerse en contacto con su administrador inmediatamente.\n\n" +
                                                        "Recuerde: asegúrese de actualizar todos sus dispositivos (teléfonos, tabletas y PC) con la nueva contraseña.\n\n" +
                                                        "Atentamente,";
                                                EmailSender.sendEmail(subjectChangePwd,alumno.getEmail(), resetPasswordMessage,this);

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

        binding.imageView6.setOnClickListener(view -> {
            cerrarSesion();
        });
    }

    public boolean cambiarContrasena(String passw){
        boolean psswdValido = false;
        String contraAntigua = ((TextInputEditText) binding.inputContra.getEditText()).getText().toString();
        String nuevaContra = ((TextInputEditText) binding.inputNuevaContra.getEditText()).getText().toString();
        String repeNuevaContra = ((TextInputEditText) binding.inputRepeatContra.getEditText()).getText().toString();
        Log.d("msg-test", "Iniciando cambio de contraseña");

        if(contraAntigua.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Debe ingresar la contraseña actual.")
                    .setTitle("Aviso")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            if(!contraAntigua.equals(passw)){
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

                        updatePassword(alumno.getEmail(), contraAntigua, nuevaContra);
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
        abrirSidebar.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView usuario = headerView.findViewById(R.id.textView6);
        TextView estado = headerView.findViewById(R.id.estado);
        ImageView fotoPerfil = headerView.findViewById(R.id.imageViewFotoPerfil);

        usuario.setText(alumno.getNombre()+" "+alumno.getApellido());
        estado.setText(alumno.getCondicion());

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imgRef = firebaseStorage.getReference().child("img_perfiles/"+alumno.getNombre()+" "+alumno.getApellido()+".jpg");
        Glide.with(this)
                .load(imgRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(fotoPerfil);

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

    public void updatePassword(String email, String oldPassword, String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            // Contraseña actualizada con éxito
                                            Toast.makeText(this, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Error al actualizar la contraseña
                                            Toast.makeText(this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Error en la reautenticación
                            Toast.makeText(this, "Error al reautenticar al usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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

    public void cerrarSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setTitle("Aviso")
                .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                    Intent intent1 = new Intent(this, LoginActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                })
                .setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}