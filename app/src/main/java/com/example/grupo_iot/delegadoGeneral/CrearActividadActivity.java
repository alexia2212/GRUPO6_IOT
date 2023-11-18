package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityNuevoEventoBinding;
import com.example.grupo_iot.delactividad.NuevoEvento;
import com.example.grupo_iot.delegadoGeneral.entity.ActividadData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.grupo_iot.databinding.ActivityActividadesCrearBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

public class CrearActividadActivity extends AppCompatActivity {
    ActivityActividadesCrearBinding binding;
    private FirebaseFirestore firebaseDatabase;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActividadesCrearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        firebaseDatabase = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
       // generarBottomNavigationMenu();

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });
        


    }


    private void guardar(){
        String nombreActividad = binding.crearTitulo.getText().toString();
        String descripcionActividad = binding.crearDescripcion.getText().toString();

        if(nombreActividad.equals("") || descripcionActividad.equals("")){
            validacion();
        }else{
            ActividadData act = new ActividadData();
            act.setNombreActividad(nombreActividad);
            act.setDescripcionActividad(descripcionActividad);
            firebaseDatabase.collection("actividades")
                    .document(nombreActividad)
                    .set(act)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CrearActividadActivity.this, "Actividad Creada", Toast.LENGTH_SHORT).show();
                            limpiar();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CrearActividadActivity.this, "Error en la creación", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        //ActividadData act = new ActividadData(nombreActividad, descripcionActividad);
        //guardarEnFirestore(act);
    }

    private void guardarEnFirestore(ActividadData act) {
        firebaseDatabase.collection("actividades")
                .add(act)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CrearActividadActivity.this, "Actividad Creada", Toast.LENGTH_SHORT).show();
                        limpiar();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CrearActividadActivity.this, "Error en la creación", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void limpiar() {
        binding.crearTitulo.setText("");
        binding.crearDescripcion.setText("");
    }

    private void validacion() {
        String nombreActividad = binding.crearTitulo.getText().toString();
        String descripcionActividad = binding.crearDescripcion.getText().toString();
        if (nombreActividad.equals("")){
            binding.crearTitulo.setError("Required");
        }else if (descripcionActividad.equals("")){
            binding.crearDescripcion.setError("Required");
        }
    }
   /* private void generarBottomNavigationMenu() {
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation3);
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {

            if(menuItem.getItemId()==R.id.navigation_estadistica){
                Intent intent = new Intent(CrearActividadActivity.this, CrearActividadActivity.class);
                startActivity(intent);
            }
            if(menuItem.getItemId()==R.id.navigation_validaciones){
                Intent intent = new Intent(CrearActividadActivity.this, CrearActividadActivity.class);
                startActivity(intent);
            }
            if(menuItem.getItemId()==R.id.navigation_usuarios){
                Intent intent = new Intent(CrearActividadActivity.this, CrearActividadActivity.class);
                startActivity(intent);
            }
            if(menuItem.getItemId()==R.id.navigation_actividades){
                Intent intent = new Intent(CrearActividadActivity.this, CrearActividadActivity.class);
                startActivity(intent);
            }
            return true;
        }
    });
}*/

}
