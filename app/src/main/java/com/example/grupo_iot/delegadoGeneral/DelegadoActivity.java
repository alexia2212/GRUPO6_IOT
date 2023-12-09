package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityMenuActividadesAsignardelegadoBinding;
import com.example.grupo_iot.delegadoGeneral.adapter.AsignarDelegadoAdapter;
import com.example.grupo_iot.delegadoGeneral.entity.Actividad;
import com.example.grupo_iot.delegadoGeneral.entity.DataHolder;
import com.example.grupo_iot.delegadoGeneral.entity.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DelegadoActivity extends AppCompatActivity {
    ActivityMenuActividadesAsignardelegadoBinding binding;
    private AsignarDelegadoAdapter asignarDelegadoAdapter;
    FirebaseFirestore db;
    FirebaseAuth auth;
    private List<Usuarios> delegadoList;

    private List<Actividad> actividadList;
    String nombreActividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuActividadesAsignardelegadoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        delegadoList = new ArrayList<>();
        actividadList = new ArrayList<>();
        asignarDelegadoAdapter = new AsignarDelegadoAdapter(delegadoList, actividadList);
        RecyclerView recyclerView = findViewById(R.id.delegadosLista);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(asignarDelegadoAdapter);
        Intent intent = getIntent();

        if (intent.hasExtra("nombreActividad")) {
            nombreActividad = intent.getStringExtra("nombreActividad");
            Log.e("DelegadoActivity", "Nombre de la actividad recibido: " + nombreActividad);
            TextView nombreTextView = findViewById(R.id.nameActividadDele);
            nombreTextView.setText(nombreActividad);
            DataHolder.getInstance().setNombreActividad(nombreActividad);
        }
        datos();
    }
    private void datos() {
            db.collection("credenciales")
                    .whereEqualTo("actividadDesignada", "")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        delegadoList.clear();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String documentoId = document.getId();
                            Usuarios user = document.toObject(Usuarios.class);
                            String nombre = user.getNombre();
                            String apellido = user.getApellido();
                            String codigo = user.getCodigo();
                            String email = user.getEmail();
                            String rol = user.getRol();
                            String condicion = user.getCondicion();
                            delegadoList.add(new Usuarios(nombre, codigo, documentoId, apellido, email, rol, condicion));
                        }
                        asignarDelegadoAdapter.setDelegadoList(delegadoList);
                    })


                    .addOnFailureListener(e -> {
                        Log.e("DelegadoActivity", "Error en validacion", e);
                    });


        }
    }

