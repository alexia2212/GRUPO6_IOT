package com.example.grupo_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.grupo_iot.R;

public class MenuEventosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_eventos);
    }

    public void irEvento(View view){
        Intent intent = new Intent(this, EventoActivity.class);
        startActivity(intent);
    }
}