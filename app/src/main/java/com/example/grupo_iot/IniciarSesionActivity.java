package com.example.grupo_iot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.grupo_iot.alumno.activity.ListaActividadesActivity;
import com.example.grupo_iot.alumno.entity.EventoApoyado;
import com.example.grupo_iot.databinding.ActivityIniciarSesionBinding;
import com.example.grupo_iot.delactividad.Delactprincipal;
import com.example.grupo_iot.delegadoGeneral.MenuDelegadoGeneralActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class IniciarSesionActivity extends AppCompatActivity {
    ActivityIniciarSesionBinding binding;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIniciarSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        binding.btnIngresar.setOnClickListener(view -> {
            String usuarioIngresado = ((TextInputEditText) binding.inputEmail.getEditText()).getText().toString();
            String contrasenaIngresada = ((TextInputEditText) binding.inputPasswd.getEditText()).getText().toString();
            validarUsuario(usuarioIngresado,contrasenaIngresada);
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(usuarioIngresado,contrasenaIngresada);

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

    public void validarUsuario(String usuario, String password){
        db.collection("credenciales")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        boolean usuarioValido = false;
                        Usuario userValido = new Usuario();
                        for (QueryDocumentSnapshot u : task.getResult()) {
                            Usuario user = u.toObject(Usuario.class);
                            if(user.getEmail().equals(usuario) && user.getPassword().equals(password)){
                                userValido = user;
                                usuarioValido = true;
                                break;
                            }
                        }
                        if(usuarioValido){
                            if(userValido.getRol().equals("alumno")){
                                Intent intent = new Intent(IniciarSesionActivity.this, ListaActividadesActivity.class);
                                intent.putExtra("correoAlumno", userValido.getEmail());
                                startActivity(intent);
                            } else if (userValido.getRol().equals("delegado general")) {
                                Intent intent = new Intent(IniciarSesionActivity.this, MenuDelegadoGeneralActivity.class);
                                startActivity(intent);
                            } else if (userValido.getRol().equals("delegado actividad")) {
                                Intent intent = new Intent(IniciarSesionActivity.this, Delactprincipal.class);
                                startActivity(intent);
                            }
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("Correo o contraseña incorrecta. Vuelva a ingresar sus datos.")
                                    .setTitle("Aviso")
                                    .setPositiveButton("Aceptar", (dialog, which) -> {
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });

    }

    public void validarUsuario2(String usuario, String password){
        db.collection("credenciales")
                .addSnapshotListener((snapshot, error) -> {
                    boolean usuarioValido = false;
                    if (error != null) {
                        Log.w("msg-test", "Listen failed.", error);
                        return;
                    }

                    for (QueryDocumentSnapshot u : snapshot) {
                        Usuario user = u.toObject(Usuario.class);
                        if(user.getEmail().equals(usuario) && user.getPassword().equals(password)){
                            Log.d("msg-test","*****email******");

                            Log.d("msg-test",user.getEmail());
                            Log.d("msg-test","*****passwd******");
                            Log.d("msg-test",user.getPassword());
                            Log.d("msg-test",user.getNombre());
                            Log.d("msg-test","*****#####******");
                                /*
                                rol = user.getRol();
                                Log.d("msg-test",user.getNombre());
                                Log.d("msg-test",user.getEmail());
                                Log.d("msg-test",user.getPassword());
                                Log.d("msg-test",user.getRol());
                                 */
                            usuarioValido = true;
                            break;
                        }
                    }
                    if(usuarioValido){
                        Intent intent = new Intent(IniciarSesionActivity.this, MenuDelegadoGeneralActivity.class);
                        startActivity(intent);
                            /*
                            if(rol.equals("alumno")){
                                Intent intent = new Intent(IniciarSesionActivity.this, ListaActividadesActivity.class);
                                startActivity(intent);
                            } else if (rol.equals("delegado general")) {
                                Intent intent = new Intent(IniciarSesionActivity.this, MenuDelegadoGeneralActivity.class);
                                startActivity(intent);
                            } else if (rol.equals("delegado actividad")) {
                                Intent intent = new Intent(IniciarSesionActivity.this, delactprincipal.class);
                                startActivity(intent);
                            }

                             */

                    }else {
                        Toast.makeText(this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }

                });
    }
}