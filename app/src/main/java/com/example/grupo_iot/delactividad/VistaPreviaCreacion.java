package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.databinding.ActivityUsuariosInscritosBinding;
import com.example.grupo_iot.databinding.ActivityVistaPreviaCreacionBinding;
import com.example.grupo_iot.databinding.ActivityVistaPreviaEventoBinding;

import java.util.ArrayList;
import java.util.List;

public class VistaPreviaCreacion extends AppCompatActivity {

    ActivityVistaPreviaCreacionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaPreviaCreacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}