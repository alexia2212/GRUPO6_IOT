package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityActualizarBinding;
import com.example.grupo_iot.databinding.ActivityNuevoEventoBinding;

import java.util.ArrayList;
import java.util.List;

public class NuevoEvento extends AppCompatActivity {

    ActivityNuevoEventoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevoEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }


}