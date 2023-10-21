package com.example.grupo_iot.delegadoGeneral;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_iot.alumno.entity.Actividad;
import com.example.grupo_iot.databinding.ActivityMenuActividadesBinding;

import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.grupo_iot.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActividadesActivity extends AppCompatActivity {

    ActivityMenuActividadesBinding binding;
    private DrawerLayout drawerLayout;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_actividades);

       // binding = ActivityMenuActividadesBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
       // db = FirebaseFirestore.getInstance();

       // ArrayList<String> eventos = new ArrayList<>();
       // eventos.add("Evento");

      //  cargarDataActividades();
    }

    public void masinfo(View view){
        Intent intent=new Intent(this, EventosActivity.class);
        startActivity(intent);
    }
    public void crearActividad(View view){
        Intent intent=new Intent(this, CrearActividadActivity.class);
        startActivity(intent);
    }

    public void cargarDataActividades (){
        db.collection("actividadesG")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Actividad> actividadList = new ArrayList<>();
                        for (QueryDocumentSnapshot actividad : task.getResult()) {
                            Actividad activ = actividad.toObject(Actividad.class);
                            actividadList.add(activ);
                        }
                    }
                });
    }

}
