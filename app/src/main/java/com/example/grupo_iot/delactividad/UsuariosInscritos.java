package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityActualizarBinding;
import com.example.grupo_iot.databinding.ActivityUsuariosInscritosBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class UsuariosInscritos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UsuariosInscritosAdaptador adapter;
    private List<UsuariosLista> listaDatos;

    ActivityUsuariosInscritosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuariosInscritosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        generarBottomNavigationMenu();

        //recyclerView = findViewById(R.id.usuarios1);
        listaDatos = obtenerDatos(); // Obtén tus datos de algún lugar (por ejemplo, una base de datos o una lista)

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UsuariosInscritosAdaptador(listaDatos);
        recyclerView.setAdapter(adapter);
    }

    // Implementa la lógica para obtener tus datos aquí
    private List<UsuariosLista> obtenerDatos() {
        // Aquí debes devolver una lista de objetos UsuariosLista con la información que quieras mostrar en la lista
        // Por ejemplo, puedes crear una lista de prueba como esta:
        List<UsuariosLista> datos = new ArrayList<>();
        datos.add(new UsuariosLista("Stuardo Lucho", "Condicion: Egresado","Barra", R.drawable.stuardo3));
        datos.add(new UsuariosLista("Angelo Ramos", "Condición: Alumno","Sin Función", R.drawable.angeloramos));
        datos.add(new UsuariosLista("Angie Alejandro", "Condición: Alumno","Barra", R.drawable.angiealejandro));
        datos.add(new UsuariosLista("Stefhabie Jaramillo", "Condición: Egresado","Sin Funcion", R.drawable.stefhaniejaramillo));
        datos.add(new UsuariosLista("Carlos Ayala", "Condición: Alumno","Barra", R.drawable.carlos));
        datos.add(new UsuariosLista("Camila Rios", "Condición: Egresado","Sin Funcion", R.drawable.chica));


        return datos;
    }

    void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.navigation_lista_eventos){
                    Intent intent = new Intent(UsuariosInscritos.this, Delactprincipal.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chatsdelact){
                    Intent intent = new Intent(UsuariosInscritos.this, Chatdelact.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfildelact){
                    Intent intent = new Intent(UsuariosInscritos.this, Perfildelact.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}
