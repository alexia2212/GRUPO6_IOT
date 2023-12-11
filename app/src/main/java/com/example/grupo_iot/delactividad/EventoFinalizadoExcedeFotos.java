package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityCompartirfotosBinding;
import com.example.grupo_iot.databinding.ActivityEventoFinalizadoExcedeFotosBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventoFinalizadoExcedeFotos extends AppCompatActivity {

    FirebaseFirestore db;

    ActivityEventoFinalizadoExcedeFotosBinding binding;

    FirebaseAuth auth;

    private String documentoActualId;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int LIMITE_FOTOS = 8;

    private List<ImageView> cuadrosDeFotos = new ArrayList<>();
    private int contadorFotos = 0;

    private List<String> urlsDeImagenes = new ArrayList<>();

    private Button guardarFotosButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventoFinalizadoExcedeFotosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        generarBottomNavigationMenu();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String listaImagenes = preferences.getString("listaImagenes", "");
        ArrayList<String> urlsImagenes = getIntent().getStringArrayListExtra("urlsImagenes");

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

        cuadrosDeFotos.add(findViewById(R.id.cuadro_foto_2));
        cuadrosDeFotos.add(findViewById(R.id.cuadro_foto_3));
        cuadrosDeFotos.add(findViewById(R.id.cuadro_foto_4));
        cuadrosDeFotos.add(findViewById(R.id.cuadro_foto_5));
        cuadrosDeFotos.add(findViewById(R.id.cuadro_foto_6));
        cuadrosDeFotos.add(findViewById(R.id.cuadro_foto_7));
        cuadrosDeFotos.add(findViewById(R.id.cuadro_foto_8));
        cuadrosDeFotos.add(findViewById(R.id.cuadro_foto_9));

        for (int i = 0; i < urlsImagenes.size(); i++) {
            String url = urlsImagenes.get(i);
            ImageView cuadroFotos = cuadrosDeFotos.get(i);
            cargarImagenDesdeFirestoreYMostrar(url, cuadroFotos);
        }

        Intent intent = getIntent();
        if (intent.hasExtra("listaData")) {
            Lista selectedLista = (Lista) intent.getSerializableExtra("listaData");
            documentoActualId = intent.getStringExtra("idDelDocumentoActual");
            System.out.println(documentoActualId + "veamos");

            // Accede a los datos recibidos
            String titulo = selectedLista.getNombre();
            Date fecha = selectedLista.getFechaHora();
            String imageUrl = selectedLista.getImagen();
            String descripcion = selectedLista.getDescripcion();
            String lugar = selectedLista.getLugar();

            // Muestra los datos en la actividad EventoFinalizadoExcedeFotos
            TextView tituloTextView = findViewById(R.id.titulo);
            TextView fechaTextView = findViewById(R.id.fecha);
            ImageView imagen1ImageView = findViewById(R.id.cuadro_foto_1);
            TextView descripcionTextView = findViewById(R.id.descripcion);
            TextView lugarTextView = findViewById(R.id.ubicacion);

            tituloTextView.setText(titulo);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy (HH:mm)", Locale.getDefault());
            String fechaFormateada = dateFormat.format(fecha);
            fechaTextView.setText(fechaFormateada);
            lugarTextView.setText(lugar);
            descripcionTextView.setText(descripcion);
            Picasso.get().load(imageUrl).into(imagen1ImageView);
        }

        Button subirFotoButton = findViewById(R.id.botonsubirfoto);
        subirFotoButton.setOnClickListener(view -> {
            // Verifica si ya se alcanzó el límite de 8 fotos
            if (seAlcanzoLimiteDeFotos()) {
                // Muestra un mensaje o realiza alguna acción indicando que se alcanzó el límite
                // Puedes desactivar el botón o mostrar un Toast
                // ...
            } else {
                verificarYCrearCamposEnFirestore();

                // Abre la galería para seleccionar una imagen
                abrirGaleria();
            }
        });

        Button eliminarFotoButton = findViewById(R.id.botonEliminar);
        eliminarFotoButton.setOnClickListener(view -> eliminarFoto());


        guardarFotosButton = findViewById(R.id.botonGuardarFotos);





    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private boolean seAlcanzoLimiteDeFotos() {
        return contadorFotos >= cuadrosDeFotos.size();
    }

    private void actualizarCuadroDeFoto(Uri uri) {
        // Obtiene el índice del próximo cuadro de foto disponible
        int indiceCuadro = contadorFotos;

        // Verifica si el índice está dentro del rango válido
        if (indiceCuadro < cuadrosDeFotos.size()) {
            ImageView cuadroFotos = cuadrosDeFotos.get(indiceCuadro);

            // Guarda la URL de la imagen en la base de datos
            guardarUrlEnFirestore(uri.toString(), "cuadrofoto" + (indiceCuadro + 2));

            // Carga la imagen en el ImageView
            Picasso.get().load(uri).into(cuadroFotos);
            urlsDeImagenes.add(uri.toString());

            // Incrementa el contador de fotos
            contadorFotos++;
        }
    }



    private void eliminarFoto() {
        // Verifica si hay fotos para eliminar
        if (contadorFotos > 0) {
            // Obtiene el índice del último cuadro de foto utilizado
            int indiceCuadro = contadorFotos - 1;

            // Obtiene el campo correspondiente en la base de datos (cuadrofotoX)
            String campo = "cuadrofoto" + (indiceCuadro + 2);

            // Restaura la imagen por defecto en el cuadro correspondiente
            ImageView cuadroFotos = cuadrosDeFotos.get(indiceCuadro);
            cuadroFotos.setImageResource(R.drawable.imagenpordefecto);

            // Elimina la URL de la imagen en la base de datos
            eliminarUrlEnFirestore(campo);

            // Decrementa el contador de fotos
            contadorFotos--;

            if (!urlsDeImagenes.isEmpty()) {
                urlsDeImagenes.remove(urlsDeImagenes.size() - 1);
            }

            // Habilita nuevamente el botón "Subir Foto" si se encontraba deshabilitado
            Button subirFotoButton = findViewById(R.id.botonsubirfoto);
            subirFotoButton.setEnabled(true);
            subirFotoButton.setBackgroundTintList(getResources().getColorStateList(R.color.turquesa));
        }
    }

    private void verificarYCrearCamposEnFirestore() {
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
                                .get()
                                .addOnSuccessListener(document -> {
                                    for (int i = 2; i <= 9; i++) {
                                        String campo = "cuadrofoto" + i;
                                        if (!document.contains(campo) || "URL_predeterminada".equals(document.getString(campo))) {
                                            // Solo actualiza el cuadro si el campo no existe o contiene la URL predeterminada
                                            // (puedes ajustar la condición según tus necesidades)
                                            document.getReference().update(campo, "URL_predeterminada");
                                            // También puedes guardar la URL en Firestore aquí si es necesario
                                        }
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Maneja el error al obtener el documento "listaEventos"
                                    e.printStackTrace();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneja el error al obtener el documento "credenciales"
                    e.printStackTrace();
                });
    }


    private void eliminarUrlEnFirestore(String campo) {
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
                                .update(campo, "") // Establece el campo como vacío
                                .addOnSuccessListener(aVoid -> {
                                    // Éxito al eliminar la URL en Firestore
                                })
                                .addOnFailureListener(e -> {
                                    // Maneja el error al eliminar la URL en Firestore
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneja el error al obtener el documento "credenciales"
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            actualizarCuadroDeFoto(uri);

            if (seAlcanzoLimiteDeFotos()) {
                Button subirFotoButton = findViewById(R.id.botonsubirfoto);
                subirFotoButton.setBackgroundTintList(getResources().getColorStateList(R.color.lightGrey));
                subirFotoButton.setEnabled(false);
            }

            // Llamar al botón "Guardar Fotos" después de actualizar la foto
            if (guardarFotosButton != null) {
                guardarFotosButton.performClick();
            }
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("urlsDeImagenes", (ArrayList<String>) urlsDeImagenes);

        // Guarda la lista de URLs en SharedPreferences
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("listaImagenes", TextUtils.join(",", urlsDeImagenes));
        editor.apply();
    }

    private void cargarImagenesGuardadas() {
        int minSize = Math.min(urlsDeImagenes.size(), cuadrosDeFotos.size());

        for (int i = 0; i < minSize; i++) {
            String url = urlsDeImagenes.get(i);
            ImageView cuadroFotos = cuadrosDeFotos.get(i);

            if (cuadroFotos != null) {
                if ("URL_predeterminada".equals(url)) {
                    cuadroFotos.setImageResource(R.drawable.imagenpordefecto);
                } else {
                    cargarImagenDesdeFirestoreYMostrar(url, cuadroFotos);
                }
            }
        }
    }

    private void guardarUrl(String url, String campo) {
        db.collection("credenciales")
                .document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String idActividad = documentSnapshot.getString("actividadDesignada");
                        db.collection("actividades")
                                .document(idActividad)
                                .collection("listaEventos")
                                .document(documentoActualId)
                                .update(campo, url)
                                .addOnSuccessListener(aVoid -> {
                                    // Éxito al guardar la URL en Firestore
                                })
                                .addOnFailureListener(e -> {
                                    // Maneja el error al guardar la URL en Firestore
                                    e.printStackTrace();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneja el error al obtener el documento "credenciales"
                    e.printStackTrace();
                });
    }

    private void guardarUrlEnFirestore(String url, String campo) {
        // Verifica si la URL es predeterminada
        if ("URL_predeterminada".equals(url)) {
            // Si es predeterminada, establece el campo con la URL predeterminada
            db.collection("credenciales")
                    .document(auth.getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String idActividad = documentSnapshot.getString("actividadDesignada");
                            db.collection("actividades")
                                    .document(idActividad)
                                    .collection("listaEventos")
                                    .document(documentoActualId)
                                    .update(campo, "URL_predeterminada")
                                    .addOnSuccessListener(aVoid -> {
                                        // Éxito al guardar la URL predeterminada en Firestore
                                    })
                                    .addOnFailureListener(e -> {
                                        // Maneja el error al guardar la URL predeterminada en Firestore
                                        e.printStackTrace();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Maneja el error al obtener el documento "credenciales"
                        e.printStackTrace();
                    });
        } else {
            // Si no es predeterminada, guarda la URL en Firestore
            guardarUrl(url, campo);
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        urlsDeImagenes = savedInstanceState.getStringArrayList("urlsDeImagenes");
        cargarImagenesGuardadas();

        // Actualiza la base de datos con las URLs correctas
        for (int i = 0; i < urlsDeImagenes.size(); i++) {
            String campo = "cuadrofoto" + (i + 2);
            String url = urlsDeImagenes.get(i);
            guardarUrlEnFirestore(url, campo);
        }
    }




    private void cargarImagenDesdeFirestoreYMostrar(String url, ImageView imageView) {
        // Cargar la imagen directamente desde la URL usando Picasso
        Picasso.get().load(url).into(imageView);
    }


    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_eventos){
                    Intent intent = new Intent(EventoFinalizadoExcedeFotos.this, Delactprincipal.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_finalizados){
                    Intent intent = new Intent(EventoFinalizadoExcedeFotos.this, EventoFinalizadoActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    Intent intent = new Intent(EventoFinalizadoExcedeFotos.this, Chatdelact.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    Intent intent = new Intent(EventoFinalizadoExcedeFotos.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });


    }
}