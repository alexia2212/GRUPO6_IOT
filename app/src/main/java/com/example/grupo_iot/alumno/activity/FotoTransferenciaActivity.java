package com.example.grupo_iot.alumno.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.Donaciones;
import com.example.grupo_iot.databinding.ActivityFotoTransferenciaBinding;
import com.example.grupo_iot.delactividad.EventoList;
import com.example.grupo_iot.delactividad.NuevoEvento;
import com.example.grupo_iot.delactividad.VistaPreviaCreacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class FotoTransferenciaActivity extends AppCompatActivity {
    ActivityFotoTransferenciaBinding binding;
    DrawerLayout drawerLayout;
    FirebaseFirestore db;
    Alumno alumno;
    private Intent intent;
    Uri imageUri;
    private  ImageView imageViewSelect;
    private Uri selectedImageUri;
    private StorageReference storageReference;
    private static final int GALLERY_REQUEST_CODE = 1;
    private boolean imagenSeleccionada = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFotoTransferenciaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        intent = getIntent();

        alumno = (Alumno) intent.getSerializableExtra("alumno");
        buscarDatosAlumnos(alumno.getEmail());
        generarBottomNavigationMenu();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_donaciones);

        binding.imageView6.setOnClickListener(view -> {
            cerrarSesion();
        });

        binding.textView4.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        binding.guardarImagen.setOnClickListener(view -> {
            String inputText = binding.inputMonto.getText().toString().trim();
            if (inputText.isEmpty()) {
                binding.inputMonto.setError("El campo no puede estar vacío");
                binding.inputMonto.requestFocus();
                return;
            }
            if (!inputText.matches("[0-9]+")) {
                binding.inputMonto.setError("El campo solo debe contener números");
                binding.inputMonto.requestFocus();
                return;
            }
            if(imageUri==null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Debe seleccionar una imagen.")
                        .setTitle("Aviso")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }else{
                guardarImagenEnFirebaseStorage(imageUri);
                guardarMontoEnFirestore(inputText);
                Intent intent2 = new Intent(this, ConfirmacionTransferenciaActivity.class);
                intent2.putExtra("alumno",alumno);
                startActivity(intent2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ImageView imageView16 = binding.imageView16;
            ImageView imageView = binding.imageView24;
            Picasso.get().load(imageUri).into(imageView);
            imageView16.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    private void guardarMontoEnFirestore(String monto){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm 'Hrs.'", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String fechaHoraActual = dateFormat.format(new Date());

        CollectionReference donacionesCollection = db.collection("donaciones");
        DocumentReference condicionDocument = donacionesCollection.document(alumno.getCondicion());
        CollectionReference donacionCollection = condicionDocument.collection(alumno.getCodigo());
        DocumentReference donacionDocument = donacionCollection.document(fechaHoraActual);

        Map<String, Object> datosDonacion = new HashMap<>();
        datosDonacion.put("monto", "S/. "+monto);

        donacionDocument.set(datosDonacion);
    }
    private void guardarImagenEnFirebaseStorage(Uri imageUri) {
        long timestamp = System.currentTimeMillis();
        String fechaYHora = obtenerFechaYHora(timestamp);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("capturas_transferencias/"+alumno.getNombre()+"_"+alumno.getApellido()+"   "+fechaYHora+".jpg");

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("Autor", alumno.getNombre()+" "+alumno.getApellido())
                .setCustomMetadata("Fecha-Hora", fechaYHora)
                .build();

        imageRef.putFile(imageUri, metadata)
                .addOnSuccessListener(taskSnapshot -> {
                    /*
                    // La imagen se ha subido correctamente, ahora obtén su URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        actualizarImagenEnBaseDeDatos(imageUrl);
                    });
                     */
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                });
    }

    private void actualizarImagenEnBaseDeDatos(String imageUrl) {
        if (intent.hasExtra("titulo")) {
            String tituloOriginal = intent.getStringExtra("titulo");
            db.collection("donaciones")
                    .whereEqualTo("titulo", tituloOriginal)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String documentoActualId = document.getId();
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("imagen", imageUrl); // Actualiza el campo "imagen1" con la URL
                                    db.collection("listaeventos")
                                            .document(documentoActualId)
                                            .update(updates)
                                            .addOnSuccessListener(aVoid -> {
                                                // Los cambios se guardaron correctamente
                                                Toast.makeText(getApplicationContext(), "Imagen guardada en la base de datos", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                // Manejo de errores en caso de que la actualización falle
                                                Toast.makeText(getApplicationContext(), "Error al guardar la imagen en la base de datos", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                // Manejo de errores
                                Toast.makeText(getApplicationContext(), "Error al guardar la imagen en la base de datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void guardarImagenFB(){
        if (selectedImageUri != null) {
            // Subir la imagen a Firebase Storage y obtener el enlace
            StorageReference imageRef = storageReference.child("event_images/" + System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));
            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();

                                    Donaciones donaciones = new Donaciones(imageUrl);
                                    guardarEventoEnFirestore(donaciones);

                                    Intent intent = new Intent(FotoTransferenciaActivity.this, ConfirmacionTransferenciaActivity.class);
                                    intent.putExtra("imageUrl", imageUrl);
                                    startActivity(intent);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FotoTransferenciaActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(FotoTransferenciaActivity.this, "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
        }


    }
    private void guardarEventoEnFirestore(Donaciones donaciones) {
        db.collection("donaciones")
                .add(donaciones)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(FotoTransferenciaActivity.this, "Imagen subida con éxito", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FotoTransferenciaActivity.this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                    }
                });
    }
/*
    public void subirFoto(View view){
        Intent intent = new Intent(this, ConfirmacionTransferenciaActivity.class);
        intent.putExtra("alumno", alumno);
        startActivity(intent);
    }

 */

    public void generarSidebar(){
        ImageView abrirSidebar = findViewById(R.id.imageView5);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        abrirSidebar.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView usuario = headerView.findViewById(R.id.textView6);
        TextView estado = headerView.findViewById(R.id.estado);
        ImageView fotoPerfil = headerView.findViewById(R.id.imageViewFotoPerfil);

        usuario.setText(alumno.getNombre()+" "+alumno.getApellido());
        estado.setText(alumno.getCondicion());

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imgRef = firebaseStorage.getReference().child("img_perfiles/"+alumno.getNombre()+" "+alumno.getApellido()+".jpg");
        Glide.with(this)
                .load(imgRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(fotoPerfil);

        binding.logoutContainer.setOnClickListener(view -> {
            Intent intent = new Intent(this, NotificacionesActivity.class);
            intent.putExtra("alumno", alumno);
            startActivity(intent);
        });
    }

    public void buscarDatosAlumnos(String correo){
        db.collection("alumnos")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot user : task.getResult()) {
                            Alumno a = user.toObject(Alumno.class);
                            if(a.getEmail().equals(correo)){
                                alumno = a;
                                generarSidebar();
                                break;
                            }
                        }
                    }
                });
    }

    public void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.navigation_lista_actividades){
                    Intent intent = new Intent(FotoTransferenciaActivity.this, ListaActividadesActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(FotoTransferenciaActivity.this, ListaEventosApoyadosActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    Intent intent = new Intent(FotoTransferenciaActivity.this, ListaDeChatsActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    menuItem.setEnabled(false);
                    menuItem.setChecked(true);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(FotoTransferenciaActivity.this, PerfilActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    private String obtenerFechaYHora(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm 'Hrs'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(timestamp);
        return sdf.format(date);
    }
    private void cerrarSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setTitle("Aviso")
                .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                    Intent intent1 = new Intent(this, LoginActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                })
                .setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}