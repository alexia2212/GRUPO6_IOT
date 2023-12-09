package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.grupo_iot.databinding.ActivityActividadesEditarBinding;
import com.example.grupo_iot.R;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.delegadoGeneral.entity.DataHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarActivity extends AppCompatActivity {

    ActivityActividadesEditarBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String nombreActividad;
    String delegado;
    String idActividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActividadesEditarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent1 = getIntent();
        if (intent1.hasExtra("listaData")) {
            Actividad selectedLista = (Actividad) intent1.getSerializableExtra("listaData");

            // Accede a los datos recibidos
            nombreActividad = selectedLista.getNombreActividad();
            String descripcionActividad = selectedLista.getDescripcionActividad();
            idActividad = selectedLista.getId();
            Log.e("EditarActivity", "Nombre de la actividad recibido: " + nombreActividad);
            Log.e("EditarActivity", "Nombre id: " + idActividad);
            EditText nombreTextView = findViewById(R.id.editarTitulo);
            EditText descriTextView = findViewById(R.id.editarDescripcion);
            nombreTextView.setText(nombreActividad);
            descriTextView.setText(descripcionActividad);
        }
        Button btnGuardarCambios = findViewById(R.id.buttonActualizar);
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarDatos(idActividad);
                Intent intent = new Intent(EditarActivity.this, ActividadesActivity.class);
                intent.putExtra("nombreActividad", idActividad);
                startActivity(intent);
            }
        });

    }
    private void actualizarDatos(String nombreActi) {
        EditText nombreEditText = findViewById(R.id.editarTitulo);
        EditText descriEditText = findViewById(R.id.editarDescripcion);

        String nuevoNombre = nombreEditText.getText().toString();
        String nuevaDescripcion = descriEditText.getText().toString();

        if (!nuevoNombre.isEmpty() && !nuevaDescripcion.isEmpty()) {
            // Actualizar los datos en Firestore
            CollectionReference actividadesCollection = db.collection("actividades");
            DocumentReference actividadDocument = actividadesCollection.document(nombreActi);
            actividadDocument
                    .update("nombreActividad", nuevoNombre, "descripcionActividad", nuevaDescripcion)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Éxito al actualizar en Firestore
                            Toast.makeText(EditarActivity.this, "Cambios guardados con éxito", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error al actualizar en Firestore
                            Toast.makeText(EditarActivity.this, "Error al guardar cambios", Toast.LENGTH_SHORT).show();
                            Log.e("EditarActivity", "Error al guardar cambios en Firestore", e); // Imprimir el error en el log

                        }
                    });
        } else {
            Toast.makeText(EditarActivity.this, "Por favor, complete ambos campos", Toast.LENGTH_SHORT).show();
        }
    }

}
