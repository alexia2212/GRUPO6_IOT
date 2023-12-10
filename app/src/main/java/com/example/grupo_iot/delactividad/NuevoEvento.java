package com.example.grupo_iot.delactividad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityNuevoEventoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NuevoEvento extends AppCompatActivity {

    private TextInputEditText etFecha;
    private ImageView imageViewSelected;
    private Uri selectedImageUri;
    private Button seleccionarImagenButton;

    private static final int GALLERY_REQUEST_CODE = 1;


    ActivityNuevoEventoBinding binding;
    private Spinner spinnerLugar;
    private FirebaseFirestore db;

    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevoEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        generarBottomNavigationMenu();
        auth = FirebaseAuth.getInstance();

        binding.guardarnuevoevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarNuevoEvento();
            }
        });

        spinnerLugar = findViewById(R.id.lugarnuevoevento);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.simple_items, // El array de opciones definido en strings.xml
                android.R.layout.simple_spinner_item // Diseño predeterminado para el Spinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLugar.setAdapter(adapter);

        spinnerLugar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        etFecha = findViewById(R.id.fechanuevoevento);
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(); // Llama a la función para mostrar el selector de fecha
            }
        });

        imageViewSelected = findViewById(R.id.imageViewSelected);
        seleccionarImagenButton = findViewById(R.id.seleccionarImagenButton);

        seleccionarImagenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagenDesdeGaleria();
            }
        });

        Button vistaprevia = findViewById(R.id.vervistaprevia);
        vistaprevia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NuevoEvento.this, VistaPreviaCreacion.class);
                startActivity(intent);
            }
        });
    }

    private void guardarNuevoEvento() {
        // Obtén los valores de los campos de entrada
        String titulo = binding.titulonuevoevento.getText().toString();
        String descripcion = binding.descripcionuevoevento.getText().toString();
        String fecha = binding.fechanuevoevento.getText().toString();
        String lugar = spinnerLugar.getSelectedItem().toString();
        String estado = "activo";

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
                                    EventoList evento = new EventoList(titulo, fecha, imageUrl, descripcion, lugar, estado);
                                    guardarEventoEnFirestore(evento);

                                    // Ahora, inicia la actividad VistaPreviaCreacion y pasa los datos
                                    Intent intent = new Intent(NuevoEvento.this, VistaPreviaCreacion.class);
                                    intent.putExtra("titulo", titulo);
                                    intent.putExtra("descripcion", descripcion);
                                    intent.putExtra("fecha", fecha);
                                    intent.putExtra("lugar", lugar);
                                    intent.putExtra("imageUrl", imageUrl);
                                    startActivity(intent);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NuevoEvento.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(NuevoEvento.this, "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
        }
    }


    private void guardarEventoEnFirestore(EventoList evento) {
        String userID = auth.getCurrentUser().getUid();

        db.collection("credenciales")
                .document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String idActividad = documentSnapshot.getString("actividadDesignada");

                        String tituloEvento = evento.getTitulo();
                        db.collection("actividades")
                                .document(idActividad)
                                .collection("listaEventos")
                                .document(tituloEvento)
                                .set(evento)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(NuevoEvento.this, "Evento guardado con éxito", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NuevoEvento.this, "Error al guardar el evento", Toast.LENGTH_SHORT).show();
                                    }
                                });
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

    private void seleccionarImagenDesdeGaleria() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageViewSelected.setImageURI(selectedImageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_eventos){
                    Intent intent = new Intent(NuevoEvento.this, Delactprincipal.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_finalizados){
                    Intent intent = new Intent(NuevoEvento.this, EventoFinalizadoActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    Intent intent = new Intent(NuevoEvento.this, Chatdelact.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    Intent intent = new Intent(NuevoEvento.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}
