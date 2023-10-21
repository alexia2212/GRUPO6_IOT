package com.example.grupo_iot.delactividad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.grupo_iot.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

public class ActualizarActivity extends AppCompatActivity {

    private Spinner spinnerLugar;
    private TextInputEditText etFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        spinnerLugar = findViewById(R.id.spinnerLugar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.simple_items, // El array de opciones definido en strings.xml
                android.R.layout.simple_spinner_item // Diseño predeterminado para el Spinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLugar.setAdapter(adapter);


        spinnerLugar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtén la opción seleccionada por el usuario
                String selectedLugar = spinnerLugar.getSelectedItem().toString();
                // Puedes hacer lo que necesites con la opción seleccionada, como almacenarla en una variable
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // En caso de que no se seleccione nada
            }
        });
        etFecha = findViewById(R.id.etInput3);
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(); // Llama a la función para mostrar el selector de fecha
            }
        });

        ImageView addImage = findViewById(R.id.imageView25);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la nueva actividad aquí
                Intent intent = new Intent(ActualizarActivity.this, NuevoEvento.class);
                startActivity(intent);
            }
        });
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Actualiza el contenido del TextInputEditText de fecha con la fecha seleccionada
                etFecha.setText(datePicker.getHeaderText());
            }
        });
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
    }
}