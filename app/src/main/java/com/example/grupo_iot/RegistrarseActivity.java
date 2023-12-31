package com.example.grupo_iot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import javax.mail.Message;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.example.grupo_iot.alumno.activity.ListaActividadesActivity;
import com.example.grupo_iot.alumno.adapter.ListaActividadesAdapter;
import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.databinding.ActivityRegistroBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import javax.mail.PasswordAuthentication;

import java.nio.charset.StandardCharsets;
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
        String[] listaOpciones = {"Condición de Usuario", "Estudiante", "Egresado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner_condicion_usuario, listaOpciones);
        Spinner spinner = binding.spinnerCondicionUsuario;
        spinner.setAdapter(adapter);
        //SECCION ROL DE USUARIO
        String[] opciones = {"Alumno", "Delegado Actividad"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, opciones);

        Spinner spinner1 = binding.spinnerRolUsuario;
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(adapter1.getPosition("Alumno"));

        //SECCION ACTIVIDAD DESIGNADA
        String[] actividadDesig = {"Actividad"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, actividadDesig);

        Spinner spinner2 = binding.spinnerRolUsuario;
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(adapter2.getPosition(""));


        binding.guardar.setOnClickListener(view ->{

            //String contenidoHTML = generarContenidoHTML();
            //EmailSender.sendEmail("Registro exitoso", "pardochurasijhon@gmail.com", contenidoHTML,RegistrarseActivity.this);

            String selectedOption = spinner.getSelectedItem().toString();
            Log.d("sprinner", "condicion"+spinner.getSelectedItem().toString());

            String selectedRol = spinner1.getSelectedItem().toString();
            Log.d("sprinner", "rol"+selectedRol);

            String selectedActividad = spinner1.getSelectedItem().toString();
            Log.d("actividad seleccionada", "rol"+selectedActividad);

            Toast.makeText(this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT);

            // GUARDAR DATOS
            String nombre = ((TextInputEditText) binding.inputNombre.getEditText()).getText().toString();
            String apellido = ((TextInputEditText) binding.inputApellido.getEditText()).getText().toString();
            String codigo = ((TextInputEditText) binding.inputCodigo.getEditText()).getText().toString();
            String condicion = binding.spinnerCondicionUsuario.getSelectedItem().toString();
            String email = ((TextInputEditText) binding.inputEmail.getEditText()).getText().toString();
            String password = ((TextInputEditText) binding.inputPass.getEditText()).getText().toString();
            String rol= binding.spinnerRolUsuario.getSelectedItem().toString();
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
                //binding.inputCodigo.setError("El código debe tener 8 dígitos");
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
                builder.setMessage("El código debe tener 8 dígitos")
                        .setTitle("Aviso")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.contains(" ")){
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
                builder.setMessage("El correo electrónico no es válido")
                        .setTitle("Aviso")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                //binding.inputEmail.setError("El correo electrónico no es válido");
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
                            builder.setMessage("¿Estás seguro de enviar tu registro?")
                                    .setTitle("Aviso")
                                    .setPositiveButton("Confirmar", (dialog, which) -> {
                                        HashMap<String, Object> UsuarioPorRegistrar = new HashMap<>();
                                        UsuarioPorRegistrar.put("nombre", nombre);
                                        UsuarioPorRegistrar.put("apellido", apellido);
                                        UsuarioPorRegistrar.put("codigo", codigo);
                                        UsuarioPorRegistrar.put("condicion", condicion);
                                        UsuarioPorRegistrar.put("email", email);
                                        UsuarioPorRegistrar.put("password", password);
                                        UsuarioPorRegistrar.put("rol", "alumno");
                                        UsuarioPorRegistrar.put("actividadDesignada", "");
                                        db.collection("usuariosPorRegistrar")
                                                .add(UsuarioPorRegistrar)
                                                .addOnSuccessListener(documentReference -> {
                                                    String subject = "Registro exitoso";
                                                    String registroMessage = "<html><body style='font-family: Verdana; font-weight: bold; font-style: italic;'>" +
                                                            "¡Bienvenido a Tech Bat!<br><br>" +
                                                            "Nos complace informarte que tu registro en nuestra aplicación ha sido exitoso. Estamos emocionados de tenerte como parte de nuestra comunidad.<br><br>" +
                                                            "Antes de que puedas comenzar a disfrutar de todas las funciones de Tech Bat, necesitas validar tu cuenta. Pronto recibirás otro correo electrónico con instrucciones sobre cómo completar el proceso de validación.<br><br>" +
                                                            "Gracias por unirte a Tech Bat. Estamos ansiosos de proporcionarte una experiencia excepcional para Semana de Ingeniería.<br><br>" +
                                                            "Saludos cordiales de parte de Tech-Bat</body></html>";

                                                    EmailSender.sendEmail(subject, email, registroMessage,RegistrarseActivity.this);
                                                    Intent intent = new Intent(RegistrarseActivity.this, ConfirmacionRegistroActivity.class);
                                                    startActivity(intent);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e("msg-test", e.getMessage());
                                                    e.printStackTrace();
                                                });
                                    })
                                    .setNegativeButton("Cancelar", (dialog, which) -> {
                                        dialog.dismiss();
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();



                        }

                    }
                });
            }

/*
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
                    String condicion = binding.spinnerCondicionUsuario.getSelectedItem().toString();
                    String email = ((TextInputEditText) binding.inputEmail.getEditText()).getText().toString();
                    String password = ((TextInputEditText) binding.inputPass.getEditText()).getText().toString();
                    String rol= binding.spinnerRolUsuario.getSelectedItem().toString();
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
                        //binding.inputCodigo.setError("El código debe tener 8 dígitos");
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
                        builder.setMessage("El código debe tener 8 dígitos")
                                .setTitle("Aviso")
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.contains(" ")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
                        builder.setMessage("El correo electrónico no es válido")
                                .setTitle("Aviso")
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        //binding.inputEmail.setError("El correo electrónico no es válido");
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
                                    UsuarioPorRegistrar.put("condicion", condicion);
                                    UsuarioPorRegistrar.put("email", email);
                                    UsuarioPorRegistrar.put("password", password);
                                    UsuarioPorRegistrar.put("rol", "Alumno");
                                    UsuarioPorRegistrar.put("actividadDesignada", "");
                                    db.collection("usuariosPorRegistrar")
                                            .add(UsuarioPorRegistrar)
                                            .addOnSuccessListener(documentReference -> {
                                                String subject = "Registro exitoso";
                                                String registroMessage = "<html><body style='font-family: Verdana; font-weight: bold; font-style: italic;'>" +
                                                        "¡Bienvenido a Tech Bat!<br><br>" +
                                                        "Nos complace informarte que tu registro en nuestra aplicación ha sido exitoso. Estamos emocionados de tenerte como parte de nuestra comunidad.<br><br>" +
                                                        "Antes de que puedas comenzar a disfrutar de todas las funciones de Tech Bat, necesitas validar tu cuenta. Pronto recibirás otro correo electrónico con instrucciones sobre cómo completar el proceso de validación.<br><br>" +
                                                        "Gracias por unirte a Tech Bat. Estamos ansiosos de proporcionarte una experiencia excepcional para Semana de Ingeniería.<br><br>" +
                                                        "Saludos cordiales de parte de Tech-Bat</body></html>";

                                                EmailSender.sendEmail(subject, email, registroMessage,RegistrarseActivity.this);
                                                Intent intent = new Intent(RegistrarseActivity.this, ConfirmacionRegistroActivity.class);
                                                startActivity(intent);
                                            })
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
                    Intent intent = new Intent(RegistrarseActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            alert.show();*/
        });

    }
    private String generarContenidoHTML() {
        return "<!-- assets/mensaje_registro.html -->\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Verdana', sans-serif;\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            border: 1px solid #ccc;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #3498db;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        p {\n" +
                "            line-height: 1.6;\n" +
                "            text-align: justify;\n" +
                "        }\n" +
                "        img {\n" +
                "            display: block;\n" +
                "            margin: 0 auto;\n" +
                "            max-width: 100%;\n" +
                "            height: auto;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Bienvenido a Tech Bat</h1>\n" +
                "    <img src=\"https://firebasestorage.googleapis.com/v0/b/proyecto-iot-65516.appspot.com/o/logo.png?alt=media&token=cc38d89f-d6f0-4009-a1db-16bf274313bf\" alt=\"Logo de Tech Bat\" width=\"200\" height=\"50\">\n" +
                "    <p>Nos complace informarte que tu registro en nuestra aplicación ha sido exitoso. Estamos emocionados de tenerte como parte de nuestra comunidad.</p>\n" +
                "    <p>Antes de que puedas comenzar a disfrutar de todas las funciones de Tech Bat, necesitamos validar tu cuenta. Pronto recibirás otro correo electrónico con instrucciones sobre cómo completar el proceso de validación.</p>\n" +
                "    <p>Gracias por unirte a Tech Bat. Estamos ansiosos de proporcionarte una experiencia excepcional para Semana de Ingeniería.</p>\n" +
                "    <p>Saludos cordiales de parte de Tech-Bat.</p>\n" +
                "</body>\n" +
                "</html>";

    }


}
