package com.example.grupo_iot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.grupo_iot.alumno.activity.ListaActividadesActivity;
import com.example.grupo_iot.delegadoGeneral.MenuDelegadoGeneralActivity;
import com.example.grupo_iot.delactividad.Delactprincipal;
import com.example.grupo_iot.databinding.ActivityIniciarSesionBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class IniciarSesionActivity extends AppCompatActivity {
    ActivityIniciarSesionBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIniciarSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.btnIngresar.setOnClickListener(view -> {
            String usuarioIngresado = ((TextInputEditText) binding.inputEmail.getEditText()).getText().toString();
            String contrasenaIngresada = ((TextInputEditText) binding.inputPasswd.getEditText()).getText().toString();
            validarUsuario(usuarioIngresado, contrasenaIngresada);
        });

        binding.button4.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrarseActivity.class);
            startActivity(intent);
        });

        binding.olvidoPassword.setOnClickListener(view -> {
            Intent intent = new Intent(this, OlvidoContrasenaActivity.class);
            startActivity(intent);
        });
    }

    private void validarUsuario(String usuario, String password) {
        auth.signInWithEmailAndPassword(usuario, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("IniciarSesionActivity", "Inicio de sesión exitoso");

                        if (auth.getCurrentUser() != null) {
                            redirigirSegunRol(auth.getCurrentUser().getEmail());
                        }
                    } else {
                        Log.w("IniciarSesionActivity", "Inicio de sesión fallido", task.getException());

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Correo o contraseña incorrecta. Vuelva a ingresar sus datos.")
                                .setTitle("Aviso")
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
    }

    private void redirigirSegunRol(String correo) {
        db.collection("credenciales")
                .whereEqualTo("email", correo)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String rol = document.getString("rol");

                            if (rol != null) {
                                switch (rol) {
                                    case "alumno":
                                        Intent intentAlumno = new Intent(IniciarSesionActivity.this, ListaActividadesActivity.class);
                                        intentAlumno.putExtra("correoAlumno", correo);
                                        startActivity(intentAlumno);
                                        break;
                                    case "delegado general":
                                        Intent intentDelegadoGeneral = new Intent(IniciarSesionActivity.this, MenuDelegadoGeneralActivity.class);
                                        startActivity(intentDelegadoGeneral);
                                        break;
                                    case "delegado actividad":
                                        Intent intentDelegadoActividad = new Intent(IniciarSesionActivity.this, Delactprincipal.class);
                                        startActivity(intentDelegadoActividad);
                                        break;
                                }
                                finish();
                            }
                        }
                    } else {
                        Log.w("IniciarSesionActivity", "Error al obtener el rol", task.getException());
                    }
                });
    }
}
