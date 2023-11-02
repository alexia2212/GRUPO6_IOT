package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityVistaPreviaPerfilBinding;

public class VistaPreviaPerfil extends AppCompatActivity {

    ActivityVistaPreviaPerfilBinding binding;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_previa_perfil);
    }
}