package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityChatdelactBinding;
import com.example.grupo_iot.databinding.ActivityDelactprincipalBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Chatdelact extends AppCompatActivity {

    ActivityChatdelactBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatdelactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();


        generarBottomNavigationMenu();

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

        String senderId = "ID_DEL_USUARIO_QUE_ENVIA_EL_MENSAJE";

        binding.enviarchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtén el texto del mensaje desde el campo de entrada
                String messageText = binding.inputenviar.getText().toString().trim();

                if (!TextUtils.isEmpty(messageText)) {
                    // Crea un nuevo mensaje
                    Map<String, Object> message = new HashMap<>();
                    message.put("senderId", senderId);
                    message.put("message", messageText);
                    message.put("timestamp", FieldValue.serverTimestamp());

                    // Agrega el mensaje a la colección "chatGrupal" en Firestore
                    db.collection("chatGrupal").add(message);

                    // Borra el texto del campo de entrada después de enviar el mensaje
                    binding.inputenviar.setText("");
                }
            }
        });

    }

    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_eventos){
                    Intent intent = new Intent(Chatdelact.this, Delactprincipal.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    Intent intent = new Intent(Chatdelact.this, Chatdelact.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    Intent intent = new Intent(Chatdelact.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });


    }

}