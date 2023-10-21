package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityMenuActividadesBinding;

public class EventosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_actividades_evento);

    }
    public void editarActividad(View view){
        Intent intent=new Intent(this, EditarActivity.class);
        startActivity(intent);
    }

}
