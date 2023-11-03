package com.example.grupo_iot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.grupo_iot.alumno.activity.ListaActividadesActivity;
import com.example.grupo_iot.alumno.adapter.ListaActividadesAdapter;
import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.databinding.ActivityRegistroBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.net.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegistrarseActivity extends AppCompatActivity {

    ActivityRegistroBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        //SECCION CONDICION USUARIO
        String[] listaOpciones = {"Condición de Usuario", "Alumno", "Egresado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner_condicion_usuario, listaOpciones);
        Spinner spinner = binding.spinnerCondicionUsuario;
        spinner.setAdapter(adapter);

        binding.guardar.setOnClickListener(view ->{
            String selectedOption = spinner.getSelectedItem().toString();
            Log.d("sprinner", "a"+spinner.getSelectedItem().toString());
            Toast.makeText(this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT);

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Confirmacion");
            alert.setMessage("Estas seguro de enviar tu registro");
            alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // GUARDAR DATOS
                    String nombre = ((TextInputEditText) binding.inputNombre.getEditText()).getText().toString();
                    String apellido = ((TextInputEditText) binding.inputApellido.getEditText()).getText().toString();
                    String codigo = ((TextInputEditText) binding.inputCodigo.getEditText()).getText().toString();
                    String email = ((TextInputEditText) binding.inputEmail.getEditText()).getText().toString();
                    String password = ((TextInputEditText) binding.inputPass.getEditText()).getText().toString();
                    String rol= binding.spinnerCondicionUsuario.getSelectedItem().toString();
                    CheckBox checkBox = binding.checkBoxTerminos;

                    if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || codigo.isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
                        builder.setMessage("Todos los campos deben estar completos.")
                                .setTitle("Aviso")
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }else if(codigo.length() != 8 || codigo.contains(" ")) {
                        binding.inputCodigo.setError("El código debe tener 8 dígitos");
                    }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.contains(" ")){
                        binding.inputEmail.setError("El correo electrónico no es válido");
                    }else if(selectedOption.equals("Condición de Usuario")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
                        builder.setMessage("Por favor, seleccione una condición válida (Egresado o Alumno).")
                                .setTitle("Aviso")
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }else if (!checkBox.isChecked()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
                        builder.setMessage("Debe aceptar los términos y condiciones para continuar.")
                                .setTitle("Aviso")
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        db.collection("credenciales").get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                List<String> listaCorreosRegistrados = new ArrayList<>();
                                boolean usuarioRegistrado = false;
                                boolean usuarioPorRegistrar = false;
                                for (QueryDocumentSnapshot u : task.getResult()) {
                                    String emailIngresado = (String) u.get("email");
                                    listaCorreosRegistrados.add(emailIngresado);
                                }

                                for( String e : listaCorreosRegistrados){
                                    if(e.equals(email)){
                                        usuarioRegistrado = true;
                                    }
                                }
                                if(usuarioRegistrado){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
                                    builder.setMessage("Usted ya se encuentra registrado. Inicie sesión con su correo PUCP y contraseña.")
                                            .setTitle("Aviso")
                                            .setPositiveButton("Aceptar", (dialog, which) -> {
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }else{
                                    HashMap<String, Object> UsuarioPorRegistrar = new HashMap<>();
                                    UsuarioPorRegistrar.put("nombre", nombre);
                                    UsuarioPorRegistrar.put("apellido", apellido);
                                    UsuarioPorRegistrar.put("codigo", codigo);
                                    UsuarioPorRegistrar.put("email", email);
                                    UsuarioPorRegistrar.put("password", password);
                                    UsuarioPorRegistrar.put("rol", rol);
                                    db.collection("credenciales")
                                            .add(UsuarioPorRegistrar)
                                            .addOnSuccessListener(documentReference -> {
                                                Intent intent = new Intent(RegistrarseActivity.this, ConfirmacionRegistroActivity.class);
                                                startActivity(intent);                        })
                                            .addOnFailureListener(e -> {
                                                Log.e("msg-test", e.getMessage());
                                                e.printStackTrace();
                                            });
                                }

                            }
                        });
                    }
                }
            });
            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // enviar a iniciar sesion
                }
            });
            alert.show();
        });

    }


}
