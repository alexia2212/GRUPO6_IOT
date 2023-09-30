package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.grupo_iot.R;

import java.util.ArrayList;
import java.util.List;

public class UsuariosInscritos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UsuariosInscritosAdaptador adapter;
    private List<UsuariosLista> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_inscritos);

        recyclerView = findViewById(R.id.usuarios1);
        listaDatos = obtenerDatos(); // Obtén tus datos de algún lugar (por ejemplo, una base de datos o una lista)

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UsuariosInscritosAdaptador(listaDatos); // Reemplaza TuAdapter con el nombre de tu Adapter
        recyclerView.setAdapter(adapter);
    }

    // Implementa la lógica para obtener tus datos aquí
    private List<UsuariosLista> obtenerDatos() {
        // Aquí debes devolver una lista de objetos UsuariosLista con la información que quieras mostrar en la lista
        // Por ejemplo, puedes crear una lista de prueba como esta:
        List<UsuariosLista> datos = new ArrayList<>();
        datos.add(new UsuariosLista("Nombre1", "Texto1","Barra", R.drawable.voley4));
        datos.add(new UsuariosLista("Nombre2", "Texto2","Barra", R.drawable.voley4));
        datos.add(new UsuariosLista("Nombre3", "Texto3","Barra", R.drawable.voley4));


        return datos;
    }
}
