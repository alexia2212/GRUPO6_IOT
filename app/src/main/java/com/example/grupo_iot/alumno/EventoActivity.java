package com.example.grupo_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.grupo_iot.R;

import java.util.ArrayList;
import java.util.List;

public class EventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        String[] listaOpciones = {"Apoyar evento", "Participante", "Apoyo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, listaOpciones);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
    }

}