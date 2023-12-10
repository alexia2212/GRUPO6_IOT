package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityUsuariosInscritosBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class UsuariosInscritos extends AppCompatActivity {

    private FirebaseAuth auth;
    private RadioButton radioButtonBarra;
    private RadioButton radioButtonEquipo;
    private ActivityUsuariosInscritosBinding binding;
    private String documentoActualId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuariosInscritosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        generarBottomNavigationMenu();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.imageView6.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setTitle("Aviso")
                    .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                        auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();
                    })
                    .setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        ImageView addImage = findViewById(R.id.imageView21);
        addImage.setOnClickListener(view -> {
            Intent intent = new Intent(UsuariosInscritos.this, NuevoEvento.class);
            startActivity(intent);
        });

        Intent intent = getIntent();
        if (intent.hasExtra("listaData")) {
            Usuario selectedLista = (Usuario) intent.getSerializableExtra("listaData");
            documentoActualId = intent.getStringExtra("documentoActualId");

            // Accede a los datos recibidos
            String nombre = selectedLista.getNombre();
            String condicion = selectedLista.getCondicion();
            String foto = selectedLista.getFoto();
            String apellido = selectedLista.getApellido();
            String codigo = selectedLista.getCodigo();
            String correo = selectedLista.getCorreo();
            String funcion = selectedLista.getFuncion();
            String idintegrante = selectedLista.getIdintegrante();

            radioButtonBarra = findViewById(R.id.radioButton1);
            radioButtonEquipo = findViewById(R.id.radioButton2);

            if ("Barra".equals(funcion)) {
                radioButtonBarra.setChecked(true);
            } else if ("Equipo".equals(funcion)) {
                radioButtonEquipo.setChecked(true);
            }

            // Muestra los datos en la actividad
            TextView nombreTextView = binding.nombre;
            ImageView imagen1ImageView = binding.foto;
            TextView codigoTextView = binding.codigo;
            TextView correoTextView = binding.correo;
            TextView condicionTextView = binding.condicion;

            nombreTextView.setText(nombre + " " + apellido);
            condicionTextView.setText("Condicion: " + condicion);
            codigoTextView.setText("Codigo: " + codigo);
            correoTextView.setText("Correo: " + correo);
            Picasso.get().load(foto).into(imagen1ImageView);
        }

        Button btnGuardar = binding.guardar;
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarCambiosEnBaseDeDatos();
            }
        });
    }

    void generarBottomNavigationMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.navigation_lista_eventos) {
                    Intent intent = new Intent(UsuariosInscritos.this, Delactprincipal.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.navigation_eventos_finalizados) {
                    Intent intent = new Intent(UsuariosInscritos.this, EventoFinalizadoActivity.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.navigation_lista_chatsdelact) {
                    Intent intent = new Intent(UsuariosInscritos.this, Chatdelact.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.navigation_perfildelact) {
                    Intent intent = new Intent(UsuariosInscritos.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private void guardarCambiosEnBaseDeDatos() {
        String funcionSeleccionada;

        if (radioButtonBarra != null || radioButtonEquipo != null) {
            if (radioButtonBarra.isChecked()) {
                funcionSeleccionada = "Barra";
            } else if (radioButtonEquipo.isChecked()) {
                funcionSeleccionada = "Equipo";
            } else {
                // Ningún RadioButton seleccionado
                return;
            }

            Intent intent = getIntent();
            if (intent.hasExtra("listaData")) {
                Usuario selectedLista = (Usuario) intent.getSerializableExtra("listaData");

                // Accede a los datos recibidos
                String idintegrante = selectedLista.getIdintegrante();

                String userID = auth.getCurrentUser().getUid();

                db.collection("credenciales")
                        .document(userID)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String idActividad = documentSnapshot.getString("actividadDesignada");

                                db.collection("actividades")
                                        .document(idActividad)
                                        .collection("listaEventos")
                                        .document(documentoActualId)
                                        .collection("integrantes")
                                        .document(idintegrante)
                                        .update("funcion", funcionSeleccionada)
                                        .addOnSuccessListener(aVoid -> {
                                            // Éxito al actualizar en la base de datos
                                            Toast.makeText(UsuariosInscritos.this, "Función actualizada", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Error al actualizar en la base de datos
                                            Toast.makeText(UsuariosInscritos.this, "Error al actualizar la función en la base de datos", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Manejar error al obtener credenciales
                            Toast.makeText(this, "Error al obtener las credenciales", Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }
}
