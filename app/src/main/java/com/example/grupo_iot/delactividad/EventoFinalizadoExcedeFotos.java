package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventoFinalizadoExcedeFotos extends AppCompatActivity {

    FirebaseFirestore db;

    ActivityEventoFinalizadoExcedeFotosBinding binding;

    FirebaseAuth auth;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int LIMITE_FOTOS = 8;

    private List<ImageView> cuadrosDeFotos = new ArrayList<>();
    private int contadorFotos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventoFinalizadoExcedeFotosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        generarBottomNavigationMenu();

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

        Intent intent = getIntent();
        if (intent.hasExtra("listaData")) {
            Lista selectedLista = (Lista) intent.getSerializableExtra("listaData");

            // Accede a los datos recibidos
            String titulo = selectedLista.getTitulo();
            String fecha = selectedLista.getFecha();
            String imageUrl = selectedLista.getImagen1();
            String descripcion = selectedLista.getDescripcion();
            String lugar = selectedLista.getLugar();

            // Muestra los datos en la actividad EventoFinalizadoExcedeFotos
            TextView tituloTextView = findViewById(R.id.titulo);
            TextView fechaTextView = findViewById(R.id.fecha);
            ImageView imagen1ImageView = findViewById(R.id.cuadro_foto_1);
            TextView descripcionTextView = findViewById(R.id.descripcion);
            TextView lugarTextView = findViewById(R.id.ubicacion);

            tituloTextView.setText(titulo);
            fechaTextView.setText(fecha);
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
                // Abre la galería para seleccionar una imagen
                abrirGaleria();
            }
        });

        Button eliminarFotoButton = findViewById(R.id.botonEliminar);
        eliminarFotoButton.setOnClickListener(view -> eliminarFoto());


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
            Picasso.get().load(uri).into(cuadroFotos);

            // Incrementa el contador de fotos
            contadorFotos++;
        }
    }

    private void eliminarFoto() {
        // Verifica si hay fotos para eliminar
        if (contadorFotos > 0) {
            // Decrementa el contador de fotos
            contadorFotos--;

            // Obtiene el índice del último cuadro de foto utilizado
            int indiceCuadro = contadorFotos;

            // Restaura la imagen por defecto en el cuadro correspondiente
            ImageView cuadroFotos = cuadrosDeFotos.get(indiceCuadro);
            cuadroFotos.setImageResource(R.drawable.imagenpordefecto);

            // Habilita nuevamente el botón "Subir Foto" si se encontraba deshabilitado
            Button subirFotoButton = findViewById(R.id.botonsubirfoto);
            subirFotoButton.setEnabled(true);
            subirFotoButton.setBackgroundTintList(getResources().getColorStateList(R.color.turquesa));
        }
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
        }
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