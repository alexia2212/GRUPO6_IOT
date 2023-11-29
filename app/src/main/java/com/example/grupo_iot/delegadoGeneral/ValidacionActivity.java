package com.example.grupo_iot.delegadoGeneral;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.grupo_iot.ConfirmacionRegistroActivity;
import com.example.grupo_iot.EmailSender;
import com.example.grupo_iot.R;
import com.example.grupo_iot.RegistrarseActivity;
import com.example.grupo_iot.alumno.activity.ListaEventosActivity;
import com.example.grupo_iot.alumno.adapter.ListaEventosAdapter;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.Evento;
import com.example.grupo_iot.delactividad.Lista;
import com.example.grupo_iot.delegadoGeneral.entity.Validaciones;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.grupo_iot.databinding.ActivityMenuValidacionesUsuariosBinding;
import com.example.grupo_iot.delegadoGeneral.adapter.ValidacionesAdapter;
import com.example.grupo_iot.databinding.ActivityMenuValidacionesBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidacionActivity extends AppCompatActivity  {
    ActivityMenuValidacionesUsuariosBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuValidacionesUsuariosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String id;
        String email;
        email = null ;
        id = null;
        if (intent.hasExtra("listaData")) {
            Validaciones selectedLista = (Validaciones) intent.getSerializableExtra("listaData");

            String nombre = selectedLista.getNombre();
            String codigo = selectedLista.getCodigo();
            String apellido = selectedLista.getApellido();
            email = selectedLista.getEmail();
            String rol = selectedLista.getRol();
            id = selectedLista.getId();

            String nombreCompleto = nombre + " " + apellido;
            TextView nombreTextView = findViewById(R.id.nombreUser);
            TextView codigoTextView = findViewById(R.id.codigoUser);
            TextView correoTextView = findViewById(R.id.correoUser);
            //TextView apellidoTextView = findViewById(R.id.apellido);
            TextView rolTextView = findViewById(R.id.rolUser);

            nombreTextView.setText(nombreCompleto);
            codigoTextView.setText(codigo);
            correoTextView.setText(email);
            rolTextView.setText(rol);

        }

        Log.e("ValidacionActivity", "id:" + id);
        Button btnValidar = findViewById(R.id.botonValidar);
        String finalId = id;
        Log.e("ValidacionActivity", "id:" + finalId);
        String finalEmail = email;
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transferirDatos(finalId);
                Dialog dialog = new Dialog(ValidacionActivity.this);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                Button btnEnviarCorreo = dialog.findViewById(R.id.btnEnviarCorreoValidacion);
                btnEnviarCorreo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ValidacionActivity.this, "Validacion Realizada", Toast.LENGTH_SHORT).show();
                        String subject = "Validacion exitosa";
                        String registroMessage = "<html><body style='font-family: sans-serif-medium; '>" +
                                "¡Bienvenido a Tech Bat!<br><br>" +
                                "Nos complace informarte que tu registro en nuestra aplicación ha sido exitoso.<br><br>" +
                                "Saludos cordiales de parte de Tech-Bat</body></html>";

                        EmailSender.sendEmail(subject, finalEmail, registroMessage, ValidacionActivity.this);
                        Intent intent = new Intent(ValidacionActivity.this, ValidacionesActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });



    }

    private void transferirDatos(String id) {
        db.collection("usuariosPorRegistrar")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> userData = documentSnapshot.getData(); //Obtener datos del colletion
                        String email = (String) userData.get("email"); //extraccion
                        String password = (String) userData.get("password");
                        auth.createUserWithEmailAndPassword(email, password) //crear en la tabla de auth
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.e("ValidacionActivity", "Usuario creado en Firebase Authentication");
                                        String userId = auth.getCurrentUser().getUid(); //ID del usuario recién creado
                                        //Almacenar en credenciales
                                        db.collection("credenciales")
                                                .document(userId)
                                                .set(userData)
                                                .addOnSuccessListener(aVoid -> {
                                                    Log.e("ValidacionActivity", "Exito de transferencia");
                                                    //Para eliminar
                                                    db.collection("usuariosPorRegistrar")
                                                            .document(id)
                                                            .delete()
                                                            .addOnSuccessListener(aVoid1 -> {
                                                                Log.e("ValidacionActivity", "Usuario eliminado de usuariosPorRegistrar");
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.e("ValidacionActivity", "Error al eliminar documento de usuariosPorRegistrar", e);
                                                            });
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e("ValidacionActivity", "Error tranferencia", e);
                                                });
                                    } else {
                                        Log.e("ValidacionActivity", "Error al crear usuario en Firebase Authentication", task.getException());
                                    }
                                });
                    } else {
                        Log.d("ValidacionActivity", "El documento no existe en la colección de usuariosPorRegistrar");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ValidacionActivity", "Error al obtener datos del documento de usuariosPorRegistrar", e);
                });
    }

}