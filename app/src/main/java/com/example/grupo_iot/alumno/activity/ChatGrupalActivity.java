package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.adapter.ListaActividadesAdapter;
import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.databinding.ActivityChatGrupalBinding;
import com.example.grupo_iot.delactividad.ChatAdapter;
import com.example.grupo_iot.delactividad.ChatMessage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatGrupalActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ActivityChatGrupalBinding binding;
    DrawerLayout drawerLayout;
    Alumno alumno = new Alumno();

    FirebaseAuth auth;

    List<ChatMessage> chatMessages;

    List<Actividad> listaActividadesCompleta;


    ChatGrupalAdapter chatAdapter;

    String bueno;
    String pues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatGrupalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        Intent intent = getIntent();
        alumno = (Alumno) intent.getSerializableExtra("alumno");
        String nombreActividad = intent.getStringExtra("nombreActividad");
        String nombreEvento = intent.getStringExtra("nombreEvento");

        TextView textView25 = findViewById(R.id.textView25);

        // Configurar el nuevo texto dinámicamente
        String nuevoTexto = "Grupo " + nombreActividad;
        textView25.setText(nuevoTexto);

        bueno = nombreActividad;
        pues = nombreEvento;
        buscarDatosAlumnos(alumno.getEmail());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatGrupalAdapter(chatMessages);
        cargarMensajes();
        binding.recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewChat.setAdapter(chatAdapter);


        generarBottomNavigationMenu();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_lista_chats);

        binding.imageView19.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, ListaDeChatsActivity.class);
            intent1.putExtra("alumno", alumno);
            startActivity(intent1);
        });

        binding.imageView6.setOnClickListener(view -> {
            cerrarSesion();
        });

        if (auth.getCurrentUser() != null) {
            String senderId = auth.getCurrentUser().getUid();

            // Obtener el nombre de la actividad desde "credenciales"
            DocumentReference credencialesRef = db.collection("credenciales").document(senderId);
            credencialesRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String codigoUsuario = documentSnapshot.getString("codigo"); // Obtener el código del usuario

                    // Verificar si el usuario pertenece a la actividad
                    DocumentReference integrantesRef = db.collection("actividades")
                            .document(nombreActividad)
                            .collection("listaEventos")
                            .document(nombreEvento)
                            .collection("integrantes")
                            .document(codigoUsuario);

                    integrantesRef.get().addOnSuccessListener(integrantesSnapshot -> {
                        if (integrantesSnapshot.exists()) {
                            // El usuario pertenece a la actividad

                            binding.enviarchat.setOnClickListener(view -> {
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

                                    salaRef.collection("mensajes").add(message);

                                    binding.inputenviar.setText("");
                                }
                            });

                        } else {

                                if (documentSnapshot.exists()) {
                                    String senderName = documentSnapshot.getString("nombre");
                                    String imagen = documentSnapshot.getString("imagen");

                                    // Obtener la sala de chat grupal
                                    DocumentReference salaRef = db.collection("chatGrupal").document(nombreActividad);

                                    binding.enviarchat.setOnClickListener(view -> {
                                        String messageText = binding.inputenviar.getText().toString().trim();

                                        if (!TextUtils.isEmpty(messageText)) {
                                            // Crear un nuevo mensaje
                                            Map<String, Object> message = new HashMap<>();
                                            message.put("senderId", senderId);
                                            message.put("message", messageText);
                                            message.put("timestamp", FieldValue.serverTimestamp());
                                            message.put("nombre", senderName);
                                            message.put("imagen", imagen);

                                            salaRef.collection("mensajes").add(message);

                                            binding.inputenviar.setText("");
                                        }
                                    });
                                }

                        }
                    }).addOnFailureListener(e -> {
                        Log.e("Chatdelact", "Error al verificar integrantes", e);
                    });
                }
            }).addOnFailureListener(e -> {
                Log.e("Chatdelact", "Error al obtener credenciales", e);
            });
        } else {
            Toast.makeText(getApplicationContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }


    }
/*
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



    private void cargarMensajes() {
        if (auth.getCurrentUser() != null) {
            String senderId = auth.getCurrentUser().getUid();

            // Obtener el nombre de la actividad desde "credenciales"
            DocumentReference credencialesRef = db.collection("credenciales").document(senderId);
            credencialesRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {


                    // Obtener la sala de chat grupal
                    DocumentReference salaRef = db.collection("chatGrupal").document(bueno);

                    salaRef.collection("mensajes")
                            .orderBy("timestamp", Query.Direction.ASCENDING)
                            .addSnapshotListener((value, error) -> {
                                if (error != null) {
                                    Log.e("Chatdelact", "Error al cargar mensajes", error);
                                    return;
                                }

                                if (value != null) {
                                    chatMessages.clear();

                                    for (QueryDocumentSnapshot document : value) {
                                        ChatMessage chatMessage = document.toObject(ChatMessage.class);
                                        chatMessages.add(chatMessage);
                                    }

                                    chatAdapter.notifyDataSetChanged();
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

    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_actividades){
                    Intent intent = new Intent(ChatGrupalActivity.this, ListaActividadesActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(ChatGrupalActivity.this, ListaEventosApoyadosActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    menuItem.setEnabled(false);
                    menuItem.setChecked(true);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(ChatGrupalActivity.this, DonacionesActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(ChatGrupalActivity.this, PerfilActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    public void cerrarSesion(){
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