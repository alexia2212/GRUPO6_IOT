package com.example.grupo_iot.alumno.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.EventoApoyado;
import com.example.grupo_iot.databinding.ActivityEventoBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class EventoActivity extends AppCompatActivity {
    ActivityEventoBinding binding;
    private DrawerLayout drawerLayout;
    Alumno alumno;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String nombreActividad = intent.getStringExtra("nombreActividad");
        String nombreEvento = intent.getStringExtra("nombreEvento");
        String descripcionEvento = intent.getStringExtra("descripcionEvento");
        String fechaEvento = intent.getStringExtra("fechaEvento");
        String horaEvento = intent.getStringExtra("horaEvento");
        String lugarEvento = intent.getStringExtra("lugarEvento");
        String nombreImagen  = intent.getStringExtra("idImagenEvento");
        alumno = (Alumno) intent.getSerializableExtra("alumno");
        buscarDatosAlumnos(alumno.getEmail());
        generarBottomNavigationMenu();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_lista_actividades);

        binding.textView40.setText(nombreActividad);
        binding.textView7.setText(nombreEvento);
        binding.textView13.setText(descripcionEvento);
        binding.textView35.setText("Fecha: "+fechaEvento);
        binding.textView36.setText("Hora: "+horaEvento+" Hrs.");
        binding.textView37.setText("Lugar: "+lugarEvento);
        ImageView imageViewEvento = findViewById(R.id.imageView26);

        // Carga la imagen basada en el nombre del recurso
        int resourceId = getResources().getIdentifier(nombreImagen , "drawable", getPackageName());
        if (resourceId != 0) {
            imageViewEvento.setImageResource(resourceId);
        }

        //SECCION APOYAR EVENTO
        String[] listaOpciones = {"Apoyar evento", "Barra", "Participante"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner_apoyo_evento, listaOpciones);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        binding.textViewGuardarCambios.setOnClickListener(view -> {
            String opcionSeleccionada = binding.spinner.getSelectedItem().toString();
            if ("Apoyar evento".equals(opcionSeleccionada)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Debes seleccionar una de las opciones: Barra o Participante.")
                        .setTitle("Aviso")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if ("Barra".equals(opcionSeleccionada) || "Participante".equals(opcionSeleccionada)) {
                EventoApoyado eventoApoyado = new EventoApoyado();
                eventoApoyado.setActividad(nombreActividad);
                eventoApoyado.setEvento(nombreEvento);
                eventoApoyado.setApoyo(opcionSeleccionada);

                CollectionReference alumnosCollection = db.collection("alumnos");
                DocumentReference actividadDocument = alumnosCollection.document(alumno.getCodigo());
                CollectionReference listaEventosApoyadosCollection = actividadDocument.collection("listaEventosApoyados");

                listaEventosApoyadosCollection
                        .document(nombreEvento+nombreActividad)
                        .set(eventoApoyado)
                        .addOnSuccessListener(unused -> {
                            Log.d("msg-test","Data guardada exitosamente");
                        })
                        .addOnFailureListener(e -> e.printStackTrace());

                Intent intent1 = new Intent(this, ConfirmacionApoyoActivity.class);
                intent1.putExtra("nombreActividad", nombreActividad);
                intent1.putExtra("nombreEvento", nombreEvento);
                intent1.putExtra("descripcionEvento", descripcionEvento);
                intent1.putExtra("lugarEvento", lugarEvento);
                intent1.putExtra("idImagenEvento",nombreImagen);
                intent1.putExtra("fechaEvento", fechaEvento);
                intent1.putExtra("horaEvento", horaEvento);
                intent1.putExtra("apoyo", opcionSeleccionada);
                intent1.putExtra("alumno", alumno);
                startActivity(intent1);
            }
        });

        binding.imageView6.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setTitle("Aviso")
                    .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        startActivity(intent1);
                    })
                    .setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();

        });
    }

    public void irMensajeria(View view){
        Intent intent = new Intent(this, ListaDeChatsActivity.class);
        startActivity(intent);
    }

    public void abrirNotificaciones(View view){
        Intent intent = new Intent(this, NotificacionesActivity.class);
        startActivity(intent);
    }

    public void confirmarApoyo(View view){
        Intent intent = new Intent(this, ConfirmacionApoyoActivity.class);
        intent.putExtra("alumno",alumno);
        startActivity(intent);
    }

    public void verRutaMasCorta(View view){
        Intent intent = new Intent(this, RutaMasCortaActivity.class);
        intent.putExtra("alumno",alumno);
        startActivity(intent);
    }

    public void guardarCambios(View view){
        Intent intent = new Intent(this, ConfirmacionApoyoActivity.class);
        intent.putExtra("alumno",alumno);
        startActivity(intent);
    }

    public void generarSidebar(){
        ImageView abrirSidebar = findViewById(R.id.imageView5);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        abrirSidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        View headerView = navigationView.getHeaderView(0);
        //ImageView imageView12 = headerView.findViewById(R.id.imageView12);
        TextView usuario = headerView.findViewById(R.id.textView6);
        TextView estado = headerView.findViewById(R.id.estado);

        //imageView12.setImageResource(R.mipmap.perfil1);
        usuario.setText(alumno.getNombre()+" "+alumno.getApellido());
        estado.setText(alumno.getCondicion());

        binding.logoutContainer.setOnClickListener(view -> {
            Intent intent = new Intent(this, NotificacionesActivity.class);
            intent.putExtra("alumno", alumno);
            startActivity(intent);
        });
    }

    public void buscarDatosAlumnos(String correo){
        db.collection("alumnos")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot user : task.getResult()) {
                            Alumno a = user.toObject(Alumno.class);
                            if(a.getEmail().equals(correo)){
                                alumno = a;
                                generarSidebar();
                                break;
                            }
                        }
                    }
                });
    }

    public void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.navigation_lista_actividades){
                    menuItem.setEnabled(false);
                    menuItem.setChecked(true);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(EventoActivity.this, ListaEventosApoyadosActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    Intent intent = new Intent(EventoActivity.this, ListaDeChatsActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(EventoActivity.this, DonacionesActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    Intent intent = new Intent(EventoActivity.this, PerfilActivity.class);
                    intent.putExtra("alumno", alumno);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}