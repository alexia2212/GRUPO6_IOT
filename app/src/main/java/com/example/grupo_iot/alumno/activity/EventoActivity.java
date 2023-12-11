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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.grupo_iot.LoginActivity;
import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.example.grupo_iot.alumno.entity.Evento;
import com.example.grupo_iot.alumno.entity.EventoApoyado;
import com.example.grupo_iot.databinding.ActivityEventoBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

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

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imgRef = firebaseStorage.getReference().child("img_actividades/"+nombreImagen+".png");

        Glide.with(this)
                .load(imgRef)
                .into(imageViewEvento);
/*
        // Carga la imagen basada en el nombre del recurso
        int resourceId = getResources().getIdentifier(nombreImagen , "drawable", getPackageName());
        if (resourceId != 0) {
            imageViewEvento.setImageResource(resourceId);
        }

 */

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

                CollectionReference actividadesCollection = db.collection("actividades");
                DocumentReference actDocument = actividadesCollection.document(nombreActividad);
                CollectionReference integrantesPorActividadCollection = actDocument.collection("integrantesActividad");

                integrantesPorActividadCollection
                        .document(alumno.getCodigo())
                        .set(alumno)
                        .addOnSuccessListener(unused -> {
                            Log.d("msg-test","Data guardada exitosamente");
                        })
                        .addOnFailureListener(e -> e.printStackTrace());

                CollectionReference listaEventosCollection = actDocument.collection("listaEventos");

                listaEventosCollection
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot evento : task.getResult()){
                                    Evento evento1 = evento.toObject(Evento.class);
                                    if (evento1.getNombre().equals(nombreEvento)){
                                        DocumentReference doc = listaEventosCollection.document(evento.getId());
                                        CollectionReference integrantesPorEventoCollection = doc.collection("integrantes");

                                        Map<String, Object> datosAlumno = new HashMap<>();
                                        datosAlumno.put("nombre", alumno.getNombre());
                                        datosAlumno.put("apellido", alumno.getApellido());
                                        datosAlumno.put("codigo", alumno.getCodigo());
                                        datosAlumno.put("condicion", alumno.getCondicion());
                                        datosAlumno.put("email", alumno.getEmail());
                                        datosAlumno.put("password", alumno.getPassword());
                                        //datosAlumno.put("funcion", "Sin Funcion");
                                        //datosAlumno.put("foto", "https://firebasestorage.googleapis.com/v0/b/proyecto-iot-7c425.appspot.com/o/imagenes%2Fvoley6.jpg?alt=media&token=76591671-8212-4f9c-8e00-9b5da8f29a2a");

                                        integrantesPorEventoCollection
                                                .document(alumno.getCodigo())
                                                .set(datosAlumno)
                                                .addOnSuccessListener(unused -> {
                                                    Log.d("msg-test","Data guardada exitosamente");
                                                })
                                                .addOnFailureListener(e -> e.printStackTrace());
                                    }
                                }
                            }
                        });

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
           cerrarSesion();
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
        Intent intent = new Intent(this, MapsActivity.class);
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
        abrirSidebar.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView usuario = headerView.findViewById(R.id.textView6);
        TextView estado = headerView.findViewById(R.id.estado);
        ImageView fotoPerfil = headerView.findViewById(R.id.imageViewFotoPerfil);

        usuario.setText(alumno.getNombre()+" "+alumno.getApellido());
        estado.setText(alumno.getCondicion());

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imgRef = firebaseStorage.getReference().child("img_perfiles/"+alumno.getNombre()+" "+alumno.getApellido()+".jpg");
        Glide.with(this)
                .load(imgRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(fotoPerfil);

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

    public void cerrarSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setTitle("Aviso")
                .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                    Intent intent1 = new Intent(this, LoginActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                })
                .setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}