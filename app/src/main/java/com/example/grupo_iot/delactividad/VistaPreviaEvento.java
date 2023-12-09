package com.example.grupo_iot.delactividad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityVistaPreviaCreacionBinding;
import com.example.grupo_iot.databinding.ActivityVistaPreviaEventoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class VistaPreviaEvento extends AppCompatActivity {

    FirebaseFirestore db;

    ActivityVistaPreviaEventoBinding binding;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaPreviaEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        generarBottomNavigationMenu();
        isEventoFinalizado();

        binding.imageViewsalir.setOnClickListener(view -> {
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

        binding.iconoIzquierda.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Estás seguro de que deseas eliminar este evento?")
                    .setTitle("Aviso")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        Intent intent1 = new Intent(this, Delactprincipal.class);
                        startActivity(intent1);
                    })
                    .setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();

            eliminarDocumentoFirestore();

        });

        binding.boton1.setOnClickListener(view -> {
            Intent intent = getIntent();
            if (intent.hasExtra("listaData")) {
                Lista selectedLista = (Lista) intent.getSerializableExtra("listaData");
                String titulo = selectedLista.getTitulo();

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
                                        .whereEqualTo("titulo", titulo)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        // Obtiene el ID del documento actual
                                                        String documentoActualId = document.getId();
                                                        System.out.println(documentoActualId + "holaa");

                                                        // Pasa el ID del documento a la siguiente actividad
                                                        Intent siguienteIntent = new Intent(VistaPreviaEvento.this, ListaDeUsuarios.class);
                                                        siguienteIntent.putExtra("documentoActualId", documentoActualId);
                                                        startActivity(siguienteIntent);
                                                    }
                                                } else {
                                                    // Manejo de errores
                                                    Toast.makeText(getApplicationContext(), "Error al obtener el documento", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
            }
        });

        binding.boton2.setOnClickListener(view -> {
            // Check if the event is already finished
            if (!isEventoFinalizado()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Estás seguro de que deseas finalizar este evento?")
                        .setTitle("Aviso")
                        .setPositiveButton("Finalizar", (dialog, which) -> {
                            // Update the estado field in Firestore
                            updateEstadoInFirestore();
                        })
                        .setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // Event already finished, show a message or handle accordingly
                Toast.makeText(getApplicationContext(), "El evento ya está finalizado", Toast.LENGTH_SHORT).show();
            }
        });


        ImageView addImage = findViewById(R.id.imageView21);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la nueva actividad aquí
                Intent intent = new Intent(VistaPreviaEvento.this, NuevoEvento.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("listaData")) {
            Lista selectedLista = (Lista) intent.getSerializableExtra("listaData");

            // Accede a los datos recibidos
            String titulo = selectedLista.getTitulo();
            String fecha = selectedLista.getFecha();
            String imageUrl = selectedLista.getImagen1();
            String descripcion = selectedLista.getDescripcion();
            String lugar = selectedLista.getLugar();

            // Muestra los datos en la actividad VistaPreviaEvento
            TextView tituloTextView = findViewById(R.id.titulo);
            TextView fechaTextView = findViewById(R.id.fecha);
            ImageView imagen1ImageView = findViewById(R.id.imagen1);
            TextView descripcionTextView = findViewById(R.id.descripcion);
            TextView lugarTextView = findViewById(R.id.ubicacion);

            tituloTextView.setText(titulo);
            fechaTextView.setText(fecha);
            lugarTextView.setText(lugar);
            descripcionTextView.setText(descripcion);
            Picasso.get().load(imageUrl).into(imagen1ImageView);
        }

        ImageView editImage = findViewById(R.id.icono_derecha);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la nueva actividad aquí

                Lista selectedLista = (Lista) intent.getSerializableExtra("listaData");

                // Accede a los datos recibidos
                String titulo = selectedLista.getTitulo();
                String fecha = selectedLista.getFecha();
                String imageUrl = selectedLista.getImagen1();
                String descripcion = selectedLista.getDescripcion();
                String lugar = selectedLista.getLugar();
                String estado = selectedLista.getEstado();

                Intent intent = new Intent(VistaPreviaEvento.this, ActualizarActivity.class);
                intent.putExtra("titulo", titulo); // Pasa el título como extra
                intent.putExtra("descripcion", descripcion); // Pasa la descripción como extra
                intent.putExtra("fecha", fecha); // Pasa la fecha como extra
                intent.putExtra("lugar", lugar); // Pasa el lugar como extra
                intent.putExtra("imagenUrl", imageUrl);
                intent.putExtra("estado", estado);


                startActivity(intent);
            }
        });

    }

    private void eliminarDocumentoFirestore() {
        Intent intent = getIntent();
        if (intent.hasExtra("listaData")) {
            Lista selectedLista = (Lista) intent.getSerializableExtra("listaData");
            String titulo = selectedLista.getTitulo();

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
                                    .whereEqualTo("titulo", titulo)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    // Obtiene el ID del documento actual
                                                    String documentoActualId = document.getId();

                                                    // Elimina el documento
                                                    db.collection("actividades")
                                                            .document(idActividad)
                                                            .collection("listaEventos")
                                                            .document(documentoActualId)
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // El documento se eliminó con éxito
                                                                    Toast.makeText(getApplicationContext(), "Evento eliminado", Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // Manejo de errores en caso de que la eliminación falle
                                                                    Toast.makeText(getApplicationContext(), "Error al eliminar el documento", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            } else {
                                                // Manejo de errores
                                                Toast.makeText(getApplicationContext(), "Error al eliminar el documento", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });
        }
    }


    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_eventos){
                    Intent intent = new Intent(VistaPreviaEvento.this, Delactprincipal.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    Intent intent = new Intent(VistaPreviaEvento.this, Chatdelact.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    Intent intent = new Intent(VistaPreviaEvento.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private boolean isEventoFinalizado() {
        Intent intent = getIntent();
        if (intent.hasExtra("listaData")) {
            Lista selectedLista = (Lista) intent.getSerializableExtra("listaData");

            // Dynamically set the button color based on the estado
            if ("finalizado".equals(selectedLista.getEstado())) {
                binding.boton2.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                binding.boton2.setEnabled(false); // Disable the button
                return true;
            } else {
                // Set the button color to its default color
                binding.boton2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                return false;
            }
        }
        return false;
    }

    private void updateEstadoInFirestore() {
        Intent intent = getIntent();
        if (intent.hasExtra("listaData")) {
            Lista selectedLista = (Lista) intent.getSerializableExtra("listaData");
            String titulo = selectedLista.getTitulo();

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
                                    .whereEqualTo("titulo", titulo)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    // Obtiene el ID del documento actual
                                                    String documentoActualId = document.getId();

                                                    // Update the estado field to "finalizado"
                                                    db.collection("actividades")
                                                            .document(idActividad)
                                                            .collection("listaEventos")
                                                            .document(documentoActualId)
                                                            .update("estado", "finalizado")
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // El campo "estado" se actualizó con éxito
                                                                    Toast.makeText(getApplicationContext(), "Evento finalizado", Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // Manejo de errores en caso de que la actualización falle
                                                                    Toast.makeText(getApplicationContext(), "Error al actualizar el estado del evento", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            } else {
                                                // Manejo de errores
                                                Toast.makeText(getApplicationContext(), "Error al obtener el documento", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });
        }
    }

}