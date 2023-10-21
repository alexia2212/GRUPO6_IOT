package com.example.grupo_iot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grupo_iot.alumno.activity.ListaActividadesActivity;
import com.example.grupo_iot.alumno.adapter.ListaActividadesAdapter;
import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.databinding.ActivityIniciarSesionBinding;
import com.example.grupo_iot.delactividad.delactprincipal;
import com.example.grupo_iot.delegadoGeneral.MenuDelegadoGeneralActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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
            Log.d("msg-test",usuarioIngresado);
            Log.d("msg-test",contrasenaIngresada);

            validarUsuario(usuarioIngresado,contrasenaIngresada);
        });


        binding.button4.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrarseActivity.class);
            startActivity(intent);
        });

        binding.olvidoPassword.setOnClickListener(view -> {
            Intent intent = new Intent(this, OlvidoContrasena.class);
            startActivity(intent);
        });

    }

    public void validarUsuario(String usuario, String password){
        db.collection("credenciales")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        boolean usuarioValido = false;
                        //String rol = null;
                        for (QueryDocumentSnapshot u : task.getResult()) {
                            Usuario user = u.toObject(Usuario.class);
                            if(user.getEmail().equals(usuario) && user.getPassword().equals(password)){
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
                    }
                });

    }
}