package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityChatdelactBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chatdelact extends AppCompatActivity {

    ActivityChatdelactBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;

    List<ChatMessage> chatMessages;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatdelactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        cargarMensajes();

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        binding.recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewChat.setAdapter(chatAdapter);

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

        if (auth.getCurrentUser() != null) {
            String senderId = auth.getCurrentUser().getUid();

            // Obtener el nombre de la actividad desde "credenciales"
            DocumentReference credencialesRef = db.collection("credenciales").document(senderId);
            credencialesRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String nombreActividad = documentSnapshot.getString("actividadDesignada");

                    binding.enviarchat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String messageText = binding.inputenviar.getText().toString().trim();

                            if (!TextUtils.isEmpty(messageText)) {
                                // Obtener la sala de chat grupal
                                DocumentReference salaRef = db.collection("chatGrupal").document(nombreActividad);

                                String senderName = documentSnapshot.getString("nombre");
                                String imagen = documentSnapshot.getString("imagen");

                                // Crear un nuevo mensaje
                                Map<String, Object> message = new HashMap<>();
                                message.put("senderId", senderId);
                                message.put("message", messageText);
                                message.put("timestamp", FieldValue.serverTimestamp());
                                message.put("nombre", senderName);
                                message.put("imagen", imagen);

                                // Agregar el mensaje a la colección de mensajes de la sala del usuario
                                salaRef.collection("mensajes").add(message);

                                // Borrar el texto del campo de entrada después de enviar el mensaje
                                binding.inputenviar.setText("");
                            }
                        }
                    });
                }
            }).addOnFailureListener(e -> {
                Log.e("Chatdelact", "Error al obtener credenciales", e);
            });
        } else {
            Toast.makeText(getApplicationContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarMensajes() {
        auth.getCurrentUser();

        String senderId = auth.getCurrentUser().getUid();

        // Obtener el nombre de la actividad desde "credenciales"
        DocumentReference credencialesRef = db.collection("credenciales").document(senderId);
        credencialesRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String nombreActividad = documentSnapshot.getString("actividadDesignada");

                // Obtener la sala de chat grupal
                DocumentReference salaRef = db.collection("chatGrupal").document(nombreActividad);

                salaRef.collection("mensajes")
                        .orderBy("timestamp", Query.Direction.ASCENDING)
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                return;
                            }

                            chatMessages.clear();

                            for (QueryDocumentSnapshot document : value) {
                                ChatMessage chatMessage = document.toObject(ChatMessage.class);
                                chatMessages.add(chatMessage);
                            }

                            chatAdapter.notifyDataSetChanged();
                        });
            }
        }).addOnFailureListener(e -> {
            Log.e("Chatdelact", "Error al obtener credenciales", e);
        });
    }

    void generarBottomNavigationMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.navigation_lista_eventos) {
                    Intent intent = new Intent(Chatdelact.this, Delactprincipal.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.navigation_eventos_finalizados) {
                    Intent intent = new Intent(Chatdelact.this, EventoFinalizadoActivity.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.navigation_lista_chatsdelact) {
                    Intent intent = new Intent(Chatdelact.this, Chatdelact.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.navigation_perfildelact) {
                    Intent intent = new Intent(Chatdelact.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}
