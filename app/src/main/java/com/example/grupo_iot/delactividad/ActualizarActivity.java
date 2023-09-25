package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.grupo_iot.R;
import com.google.android.material.datepicker.MaterialDatePicker;

public class ActualizarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        // Obtén una referencia al AutoCompleteTextView desde tu diseño XML
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autocompleteLugar);

        // Crea un ArrayAdapter utilizando el array de strings definido en strings.xml
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line, // Diseño predeterminado para las opciones desplegables
                getResources().getStringArray(R.array.simple_items) // El array de strings
        );

        // Configura el AutoCompleteTextView con el adaptador
        autoCompleteTextView.setAdapter(adapter);

        findViewById(R.id.etInput3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showDatePicker();  ; // Llama a la función para mostrar el selector de fecha
            }
        });
    }

    private void showDatePicker() {
        // Crea y configura el selector de fecha
        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

        // Muestra el selector de fecha
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
    }
}