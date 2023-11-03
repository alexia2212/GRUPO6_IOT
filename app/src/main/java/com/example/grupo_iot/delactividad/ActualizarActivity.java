package com.example.grupo_iot.delactividad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.alumno.activity.DonacionesActivity;
import com.example.grupo_iot.alumno.activity.ListaActividadesActivity;
import com.example.grupo_iot.alumno.activity.ListaDeChatsActivity;
import com.example.grupo_iot.alumno.activity.ListaEventosApoyadosActivity;
import com.example.grupo_iot.alumno.activity.NotificacionesActivity;
import com.example.grupo_iot.databinding.ActivityActualizarBinding;

import com.example.grupo_iot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ActualizarActivity extends AppCompatActivity {

    private Spinner spinnerLugar;
    FirebaseFirestore db;
    private TextInputEditText etFecha;

    DrawerLayout drawerLayout;

    ActivityActualizarBinding binding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActualizarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        spinnerLugar = findViewById(R.id.spinnerLugar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.simple_items, // El array de opciones definido en strings.xml
                android.R.layout.simple_spinner_item // Diseño predeterminado para el Spinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLugar.setAdapter(adapter);


        spinnerLugar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtén la opción seleccionada por el usuario
                String selectedLugar = spinnerLugar.getSelectedItem().toString();
                // Puedes hacer lo que necesites con la opción seleccionada, como almacenarla en una variable
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // En caso de que no se seleccione nada
            }
        });
        etFecha = findViewById(R.id.etInput3);
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(); // Llama a la función para mostrar el selector de fecha
            }
        });

        ImageView addImage = findViewById(R.id.imageView25);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la nueva actividad aquí
                Intent intent = new Intent(ActualizarActivity.this, NuevoEvento.class);
                startActivity(intent);
            }
        });

        Button miBoton = findViewById(R.id.botonCorrecto);

        // Agrega un OnClickListener al botón
        miBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Muestra el Toast cuando se hace clic en el botón
                Toast.makeText(getApplicationContext(), "Se actualizó", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageViewsalir.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setTitle("Aviso")
                    .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        startActivity(intent1);
                    })
                    .setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();

        });

        Intent intent = getIntent();
        if (intent.hasExtra("titulo")) {
            String titulo = intent.getStringExtra("titulo");
            String descripcion = intent.getStringExtra("descripcion");
            String fecha = intent.getStringExtra("fecha");
            String lugar = intent.getStringExtra("lugar");
            String imageUrl = intent.getStringExtra("imagenUrl");

            // Configurar los campos de entrada con los datos recibidos
            TextInputEditText etTitulo = findViewById(R.id.etInput1);
            TextInputEditText etDescripcion = findViewById(R.id.etInput2);
            TextInputEditText etFecha = findViewById(R.id.etInput3);
            Spinner spinnerLugar = findViewById(R.id.spinnerLugar);
            ImageView imagenAct = findViewById(R.id.imagenact);

            etTitulo.setText(titulo);
            etDescripcion.setText(descripcion);
            etFecha.setText(fecha);
            // Configurar el spinner de lugar si es aplicable

            // Utiliza una biblioteca como Picasso para cargar la imagen desde la URL
            Picasso.get().load(imageUrl).into(imagenAct);
        }

        Button botonEliminar = findViewById(R.id.botonEliminar);
        Button botonSubirFoto = findViewById(R.id.botonsubirfoto);

        botonSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1); // Usar un código de solicitud (puedes elegir cualquier número)

            }
        });

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Lista selectedLista = (Lista) intent.getSerializableExtra("listaData");

                String titulo = intent.getStringExtra("titulo");
                String descripcion = intent.getStringExtra("descripcion");
                String fecha = intent.getStringExtra("fecha");
                String lugar = intent.getStringExtra("lugar");
                String imageUrl = intent.getStringExtra("imagenUrl");


                db.collection("listaeventos")
                        .whereEqualTo("titulo", titulo)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Obtiene el ID del documento actual
                                        String documentoActualId = document.getId();

                                        // Crea un mapa con los campos que deseas eliminar
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("imagen1", FieldValue.delete());

                                        // Actualiza el documento para eliminar el campo "imagen1"
                                        db.collection("listaeventos")
                                                .document(documentoActualId)
                                                .update(updates)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // El campo "imagen1" se eliminó con éxito
                                                        // Puedes realizar cualquier otra operación después de la eliminación
                                                        actualizarVistaConImagenPorDefecto();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Manejo de errores en caso de que la actualización falle
                                                    }
                                                });
                                    }
                                } else {
                                    // Manejo de errores
                                }
                            }
                        });

            }
        });


    }

    private void actualizarVistaConImagenPorDefecto() {
        // Supongamos que tienes un ImageView llamado imagenAct en tu layout
        ImageView imagenAct = findViewById(R.id.imagenact);

        // Obtén la ID de la imagen por defecto desde los recursos
        int imagenPorDefectoId = R.drawable.baseline_add_photo_alternate_24; // Reemplaza con la ID correcta

        // Establece la imagen por defecto en el ImageView
        imagenAct.setImageResource(imagenPorDefectoId);
    }


    public void irMensajeria(View view){
        Intent intent = new Intent(this, ListaDeChatsActivity.class);
        startActivity(intent);
    }

    public void abrirNotificaciones(View view){
        Intent intent = new Intent(this, NotificacionesActivity.class);
        startActivity(intent);
    }

    public void volverListaChats(View view){
        Intent intent = new Intent(this, ListaDeChatsActivity.class);
        startActivity(intent);
    }

    public void generarSidebar(){
        ImageView abrirSidebar = findViewById(R.id.imageView5);
        //ImageView cerrarSidebar = findViewById(R.id.cerrarSidebar);
        drawerLayout = findViewById(R.id.drawer_layout);
        abrirSidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });
    }



    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Actualiza el contenido del TextInputEditText de fecha con la fecha seleccionada
                etFecha.setText(datePicker.getHeaderText());
            }
        });
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
    }

    /*void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_eventos){
                    Intent intent = new Intent(ActualizarActivity.this, ListaActividadesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_finalizados){
                    Intent intent = new Intent(ActualizarActivity.this, ListaEventosApoyadosActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    Intent intent = new Intent(ActualizarActivity.this, ListaDeChatsActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donacionesdelact){
                    Intent intent = new Intent(ActualizarActivity.this, DonacionesActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    Intent intent = new Intent(ActualizarActivity.this, EditarPerfilActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Obtener la Uri de la imagen seleccionada
            Uri imageUri = data.getData();

            // Cargar la imagen seleccionada en el ImageView (imagenact)
            ImageView imagenAct = findViewById(R.id.imagenact);
            Picasso.get().load(imageUri).into(imagenAct);

            // También puedes guardar la Uri de la imagen seleccionada en una variable o en Firebase Storage, si es necesario.
        }
    }
*/



}