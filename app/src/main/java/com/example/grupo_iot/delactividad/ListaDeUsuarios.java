package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.grupo_iot.R;

import java.util.ArrayList;
import java.util.List;

public class ListaDeUsuarios extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_usuarios);

        RecyclerView recyclerView = findViewById(R.id.vistaprevia);

        List<Usuario> dataList = new ArrayList<>();
        dataList.add(new Usuario(R.drawable.stuardo3, "Stuardo Lucho", "Condición: Egresado", "Barra"));
        dataList.add(new Usuario(R.drawable.angeloramos, "Angelo Ramos", "Condición: Alumno", "Barra"));
        dataList.add(new Usuario(R.drawable.carlos, "Carlos Ayala", "Condición: Alumno", "Barra"));
        dataList.add(new Usuario(R.drawable.stefhaniejaramillo, "Stefhanie Jaramillo", "Condición: Alumno", "Barra"));

        AdaptadorUsuario adapter = new AdaptadorUsuario(dataList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
