package com.example.grupo_iot.delegadoGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_iot.R;

public class InfoUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuarios_datos);

    }
    public void editarUsuario(View view){
        Intent intent=new Intent(this, EditaUsuarioActivity.class);
        startActivity(intent);
    }





}
