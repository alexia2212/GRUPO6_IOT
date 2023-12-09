package com.example.grupo_iot.delegadoGeneral;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_iot.EmailSender;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityMenuActividadDelegadoUsuarioBinding;
import com.example.grupo_iot.databinding.ActivityMenuValidacionesUsuariosBinding;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.delegadoGeneral.entity.DataHolder;
import com.example.grupo_iot.delegadoGeneral.entity.Evento;
import com.example.grupo_iot.delegadoGeneral.entity.Usuarios;
import com.example.grupo_iot.delegadoGeneral.entity.Validaciones;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsignarDelegadoActivity extends AppCompatActivity {
    ActivityMenuActividadDelegadoUsuarioBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String nombreAct;
    String nombreCompleto;
    private List<Evento> eventoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuActividadDelegadoUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent1 = getIntent();
        String id;
        String email;
        email = null ;
        id = null;

        if (intent1.hasExtra("listaData2")) {
                Usuarios selectedLista = (Usuarios) intent1.getSerializableExtra("listaData2");
                String nombre = selectedLista.getNombre();
                String codigo = selectedLista.getCodigo();
                String apellido = selectedLista.getApellido();
                email = selectedLista.getEmail();
                String condicion = selectedLista.getCondicion();
                id = selectedLista.getId();
                nombreCompleto = nombre + " " + apellido;
                TextView nombreTextView = findViewById(R.id.nombreDeleUser);
                TextView codigoTextView = findViewById(R.id.codigoDeleUser);
                TextView correoTextView = findViewById(R.id.correoDeleUser);
                TextView condicionTextView = findViewById(R.id.condicionDeleUser);

                nombreTextView.setText(nombreCompleto);
                codigoTextView.setText(codigo);
                correoTextView.setText(email);
                condicionTextView.setText(condicion);
        }
        nombreAct = DataHolder.getInstance().getNombreActividad();

        Log.e("AsignarDelegadoActivity", "nombre Actividad: " + nombreAct);
        Button btnAsignar = findViewById(R.id.botonValidar);
        String finalId = id;
        Log.e("AsignarDelegadoActivity", "id:" + finalId);
        String finalEmail = email;
        Log.e("AsignarDelegadoActivity", "email: " + email);
        btnAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarDato(finalId, nombreAct);
                agregarDato(nombreAct, nombreCompleto);
                AlertDialog.Builder alert = new AlertDialog.Builder(AsignarDelegadoActivity.this); // Corregir aquí
                alert.setTitle("Confirmacion");
                alert.setMessage("Asignar como Delegado de Actividad");
                alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(AsignarDelegadoActivity.this, "Asignacion Realizada", Toast.LENGTH_SHORT).show();
                        String subject = "Asigancion Exitosa";
                        String registroMessage = "<html><body style='font-family: sans-serif-medium; '>" +
                                "¡Se le ha asignado delegado de actividad!<br><br>" +
                                "Nos complace informarte que tu asignacion ha sido realizado.<br><br>" +
                                "Saludos cordiales de parte de Tech-Bat</body></html>";

                        Log.e("AsignarDelegadoActivity", "email final: " + finalEmail);
                        EmailSender.sendEmail(subject, finalEmail, registroMessage, AsignarDelegadoActivity.this);
                        Intent intent = new Intent(AsignarDelegadoActivity.this, ActividadesActivity.class);
                        intent.putExtra("nombreActividad", nombreAct);
                        startActivity(intent);
                    }
                });
                alert.show();
            }
        });
    }
    private void cambiarDato(String id, String nombreAct) {
        DocumentReference docRef = db.collection("credenciales").document(id);
        Map<String, Object> updates = new HashMap<>();
        updates.put("actividadDesignada", nombreAct);
        docRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Documento actualizado correctamente");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al actualizar documento", e);
                });

    }
    private void agregarDato(String nombreAct, String nombreCompleto) {
        DocumentReference docRef = db.collection("actividades").document(nombreAct);
        Map<String, Object> nuevoCampo  = new HashMap<>();
        nuevoCampo.put("delegadoActividad", nombreCompleto);
        docRef.set(nuevoCampo, SetOptions.merge() )
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Documento agregado correctamente");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al agregar documento", e);
                });

    }

}
