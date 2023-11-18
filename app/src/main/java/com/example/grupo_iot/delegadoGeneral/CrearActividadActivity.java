package com.example.grupo_iot.delegadoGeneral;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_iot.R;
import com.example.grupo_iot.delactividad.NuevoEvento;
import com.example.grupo_iot.delegadoGeneral.entity.ActividadData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

public class CrearActividadActivity extends AppCompatActivity {

    private EditText crearTitulo, crearDescripcion;
    private FirebaseFirestore firebaseDatabase;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades_crear);

        crearTitulo = findViewById(R.id.crearTitulo);
        crearDescripcion = findViewById(R.id.crearDescripcion);
        firebaseDatabase = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                String nombreActividad = crearTitulo.getText().toString();
                String descripcionActividad = crearDescripcion.getText().toString();
                ActividadData act = new ActividadData();
                act.setNombreActividad(nombreActividad);
                act.setDescripcionActividad(descripcionActividad);
                guardar(act);
            }
        });


    }


    private void guardar(ActividadData actividadData){
        String nombreActividad = crearTitulo.getText().toString();
        String descripcionActividad = crearDescripcion.getText().toString();

        if(nombreActividad.equals("") || descripcionActividad.equals("")){
            validacion();
        }else{
            firebaseDatabase.collection("actividadesG")
                    .add(actividadData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(CrearActividadActivity.this, "Actividad Creada", Toast.LENGTH_SHORT).show();
                            limpiar();
                        }
                    });
        }
    }

    private void limpiar() {
        crearTitulo.setText("");
        crearDescripcion.setText("");
    }

    private void validacion() {
        String nombreActividad = crearTitulo.getText().toString();
        String descripcionActividad = crearDescripcion.getText().toString();
        if (nombreActividad.equals("")){
            crearTitulo.setError("Required");
        }else if (descripcionActividad.equals("")){
            crearDescripcion.setError("Required");
        }
    }

}
