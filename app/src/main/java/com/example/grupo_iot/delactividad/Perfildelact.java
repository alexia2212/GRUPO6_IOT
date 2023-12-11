package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityPerfildelactBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Perfildelact extends AppCompatActivity {

    ActivityPerfildelactBinding binding;
    FirebaseAuth auth;
    DrawerLayout drawerLayout;

    FirebaseFirestore db;
    FirebaseUser currentUser;

    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfildelactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        generarBottomNavigationMenu();

        // Check if the user is authenticated
        if (currentUser != null) {
            // Fetch user data from Firestore
            fetchUserData();
        }

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

        binding.btnCambiarFoto.setOnClickListener(view -> {
            // Lanzar el intent para seleccionar una imagen de la galería
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });


    }

    private void fetchUserData() {
        // Assuming you have a 'users' collection in Firestore and each user document has fields like 'name', 'email', 'code', etc.
        db.collection("credenciales")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get user data and update the views
                        String name = documentSnapshot.getString("nombre");
                        String surname = documentSnapshot.getString("apellido");
                        String rol = documentSnapshot.getString("rol");
                        String email = documentSnapshot.getString("email");
                        String code = documentSnapshot.getString("codigo");
                        String imageUrl = documentSnapshot.getString("imagen");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Picasso.get().load(imageUrl).into(binding.imageView12);
                        }
                        // Set the values to the corresponding views in your layout
                        binding.textViewNombre.setText(name + " " + surname);
                        binding.textViewCorreo.setText(email);
                        binding.textViewCodigoPUCP.setText(code);
                        binding.textViewCondicion.setText(rol.substring(0, 1).toUpperCase() + rol.substring(1));

                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Perfildelact", "Error fetching user data: " + e.getMessage());
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Obtener la Uri de la imagen seleccionada
            Uri selectedImageUri = data.getData();

            // Cargar la imagen en imageView12
            Picasso.get().load(selectedImageUri).into(binding.imageView12);

            // Subir la imagen a Firebase Storage y obtener la URL
            uploadImageToFirebase(selectedImageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Crear una referencia en Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("imagenes/" + currentUser.getUid());

        // Subir la imagen
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Obtener la URL de la imagen cargada
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Actualizar el campo "imagen" en Firestore con la URL de la nueva imagen
                        updateImageUrlInFirestore(uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    // Manejar errores al cargar la imagen
                    Log.e("Perfildelact", "Error uploading image: " + e.getMessage());
                });
    }

    private void updateImageUrlInFirestore(String imageUrl) {
        // Actualizar el campo "imagen" en Firestore con la nueva URL
        db.collection("credenciales")
                .document(currentUser.getUid())
                .update("imagen", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Éxito al actualizar la URL en Firestore
                    Toast.makeText(this, "Imagen actualizada correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Manejar errores al actualizar el campo en Firestore
                    Log.e("Perfildelact", "Error updating image URL in Firestore: " + e.getMessage());
                });
    }



    void generarBottomNavigationMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.navigation_lista_eventos) {
                    Intent intent = new Intent(Perfildelact.this, Delactprincipal.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.navigation_eventos_finalizados) {
                    Intent intent = new Intent(Perfildelact.this, EventoFinalizadoActivity.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.navigation_lista_chatsdelact) {
                    Intent intent = new Intent(Perfildelact.this, Chatdelact.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.navigation_perfildelact) {
                    Intent intent = new Intent(Perfildelact.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}
