package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_iot.R;

public class ValidacionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_validaciones);

    }
    public void validacion(View view){
        Intent intent=new Intent(this, ValidacionActivity.class);
        startActivity(intent);
    }
}
