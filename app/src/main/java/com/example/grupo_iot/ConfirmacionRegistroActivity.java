package com.example.grupo_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.grupo_iot.databinding.ActivityConfirmacionRegistroBinding;

public class ConfirmacionRegistroActivity extends AppCompatActivity {

    ActivityConfirmacionRegistroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmacionRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}