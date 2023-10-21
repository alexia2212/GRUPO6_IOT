package com.example.grupo_iot.delegadoGeneral;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityIniciarSesionBinding;
import com.example.grupo_iot.databinding.ActivityMenuDelegadoGeneralBinding;

public class MenuDelegadoGeneralActivity extends AppCompatActivity {

    ActivityMenuDelegadoGeneralBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuDelegadoGeneralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}