package com.example.grupo_iot.delactividad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.grupo_iot.alumno.activity.PerfilActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ActualizarActivity extends AppCompatActivity {

    private Spinner spinnerLugar;
    FirebaseFirestore db;

    FirebaseAuth auth;
    private TextInputEditText etFecha;

    private TextInputEditText etTitulo;

    private TextInputEditText etDescripcion;

    private String imageUrl;

    private Intent intent;

    DrawerLayout drawerLayout;

    ActivityActualizarBinding binding;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActualizarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();

        spinnerLugar = findViewById(R.id.spinnerLugar);

        generarBottomNavigationMenu();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.simple_items, // El array de opciones definido en strings.xml
                android.R.layout.simple_spinner_item // Diseño predeterminado para el Spinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLugar.setAdapter(adapter);

        intent = getIntent();



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
        etDescripcion = findViewById(R.id.etInput2);
        etTitulo = findViewById(R.id.etInput1);
        etTitulo.setFocusable(false); // Hace que el campo de texto no sea editable
        etTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Puedes agregar algún código aquí si necesitas manejar clics en el título
                // o simplemente dejarlo vacío si no es necesario.
            }
        });


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
                // Obtener los nuevos valores de los campos de entrada
                String nuevoTitulo = etTitulo.getText().toString();
                String nuevaDescripcion = etDescripcion.getText().toString();
                String nuevaFecha = etFecha.getText().toString();
                String nuevoLugar = spinnerLugar.getSelectedItem().toString();

                // Verificar si la imagen actual es la imagen por defecto
                boolean esImagenPorDefecto = isImagenPorDefecto();

                // Actualizar los valores en la base de datos
                if (intent.hasExtra("nombre")) {
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
                                            .whereEqualTo("nombre", intent.getStringExtra("nombre"))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            // Obtiene el ID del documento actual
                                                            String documentoActualId = document.getId();

                                                            // Crea un mapa con los campos que deseas actualizar
                                                            Map<String, Object> updates = new HashMap<>();
                                                            updates.put("nombre", nuevoTitulo);
                                                            updates.put("descripcion", nuevaDescripcion);
                                                            updates.put("fecha", nuevaFecha);
                                                            updates.put("lugar", nuevoLugar);

                                                            if (esImagenPorDefecto) {
                                                                // Si es la imagen por defecto, guarda la URL de la imagen por defecto
                                                                updates.put("imagen1", "https://firebasestorage.googleapis.com/v0/b/proyecto-iot-65516.appspot.com/o/imagenes%2Fimagenpordefecto.jpg?alt=media&token=3c4cde4f-096b-469b-9559-8ee080e43a45");
                                                            } else {
                                                                // Si no es la imagen por defecto, elimina el campo "imagen1"
                                                                updates.put("imagen1", imageUrl);
                                                            }

                                                            // Actualiza el documento en la base de datos
                                                            db.collection("actividades")
                                                                    .document(idActividad)
                                                                    .collection("listaEventos")
                                                                    .document(documentoActualId)
                                                                    .update(updates)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            // Los cambios se guardaron correctamente
                                                                            Toast.makeText(getApplicationContext(), "Cambios guardados", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            // Manejo de errores en caso de que la actualización falle
                                                                            Toast.makeText(getApplicationContext(), "Error al guardar los cambios", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }
                                                    } else {
                                                        // Manejo de errores al obtener la colección de eventos
                                                        Toast.makeText(getApplicationContext(), "Error al obtener la colección de eventos", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // Manejo de errores si no se encuentra la actividad designada para el usuario
                                    Log.e("Firestore", "No se encontró la actividad designada para el usuario");
                                }
                            })
                            .addOnFailureListener(e -> {
                                // Manejo de errores al obtener la credencial del usuario
                                Log.e("Firestore", "Error al obtener la credencial del usuario", e);
                            });
                }
            }
        });


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

        Intent intent = getIntent();
        if (intent.hasExtra("nombre")) {
            String titulo = intent.getStringExtra("nombre");
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
                // Restaura la imagen por defecto
                actualizarVistaConImagenPorDefecto();

                // Verifica si la imagen actual es la imagen por defecto y guarda la URL correspondiente
                boolean esImagenPorDefecto = isImagenPorDefecto();

                // ... el resto de tu lógica, por ejemplo, puedes llamar a actualizarImagenEnBaseDeDatos con la URL adecuada
                if (intent.hasExtra("nombre")) {
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
                                            .whereEqualTo("nombre", intent.getStringExtra("nombre"))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            // Obtiene el ID del documento actual
                                                            String documentoActualId = document.getId();

                                                            // Crea un mapa con los campos que deseas eliminar o actualizar
                                                            Map<String, Object> updates = new HashMap<>();
                                                            if (esImagenPorDefecto) {
                                                                updates.put("imagen1", "URL_IMAGEN_POR_DEFECTO");
                                                            } else {
                                                                updates.put("imagen1", FieldValue.delete());
                                                            }

                                                            // Actualiza el documento para eliminar o actualizar el campo "imagen1"
                                                            db.collection("actividades")
                                                                    .document(idActividad)
                                                                    .collection("listaEventos")
                                                                    .document(documentoActualId)
                                                                    .update(updates)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            // Realiza cualquier operación adicional después de la eliminación o actualización
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
            }
        });




    }

    private void actualizarVistaConImagenPorDefecto() {
        // Supongamos que tienes un ImageView llamado imagenAct en tu layout
        ImageView imagenAct = findViewById(R.id.imagenact);

        // Obtén la ID de la imagen por defecto desde los recursos
        int imagenPorDefectoId = R.drawable.imagenpordefecto; // Reemplaza con la ID correcta

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
                        Intent intent = new Intent(ActualizarActivity.this, PerfilActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Obtener la Uri de la imagen seleccionada
            Uri imageUri = data.getData();

            // Cargar la imagen seleccionada en el ImageView (imagenact)
            ImageView imagenAct = findViewById(R.id.imagenact);
            Picasso.get().load(imageUri).into(imagenAct);
            guardarImagenEnFirebaseStorage(imageUri);
        }
    }
    private void guardarImagenEnFirebaseStorage(Uri imageUri) {
        // Crear una referencia al almacenamiento de Firebase donde se guardará la imagen
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Crear una referencia única para la imagen (puedes utilizar el título o ID del evento)
        StorageReference imageRef = storageRef.child("eventos/" + intent.getStringExtra("nombre") + ".jpg");

        // Subir la imagen a Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // La imagen se ha subido correctamente, ahora obtén su URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrl = uri.toString(); // Update imageUrl here

                        // Now that you have the URL of the image, save it in the database
                        actualizarImagenEnBaseDeDatos(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Manejo de errores en caso de que la carga de la imagen falle
                    Toast.makeText(getApplicationContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                });
    }

    private void actualizarImagenEnBaseDeDatos(String imageUrl) {
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
                                .whereEqualTo("nombre", intent.getStringExtra("nombre"))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String documentoActualId = document.getId();
                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("imagen1", imageUrl); // Actualiza el campo "imagen1" con la URL

                                                db.collection("actividades")
                                                        .document(idActividad)
                                                        .collection("listaEventos")
                                                        .document(documentoActualId)
                                                        .update(updates)
                                                        .addOnSuccessListener(aVoid -> {
                                                            // Los cambios se guardaron correctamente
                                                            Toast.makeText(getApplicationContext(), "Imagen guardada", Toast.LENGTH_SHORT).show();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Manejo de errores en caso de que la actualización falle
                                                            Toast.makeText(getApplicationContext(), "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                                                        });
                                            }
                                        } else {
                                            // Manejo de errores
                                            Toast.makeText(getApplicationContext(), "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
    }

    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_eventos){
                    Intent intent = new Intent(ActualizarActivity.this, Delactprincipal.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_finalizados){
                    Intent intent = new Intent(ActualizarActivity.this, EventoFinalizadoActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    Intent intent = new Intent(ActualizarActivity.this, Chatdelact.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    Intent intent = new Intent(ActualizarActivity.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private boolean isImagenPorDefecto() {
        ImageView imagenAct = findViewById(R.id.imagenact);
        Drawable imagenActual = imagenAct.getDrawable();

        // Obtén la ID de la imagen por defecto desde los recursos
        int imagenPorDefectoId = R.drawable.imagenpordefecto; // Reemplaza con la ID correcta

        // Compara la ID de la imagen actual con la ID de la imagen por defecto
        return (imagenActual != null && imagenActual.getConstantState() != null &&
                imagenActual.getConstantState().equals(getResources().getDrawable(imagenPorDefectoId).getConstantState()));
    }








}