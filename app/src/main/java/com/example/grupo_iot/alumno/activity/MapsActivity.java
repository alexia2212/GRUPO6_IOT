package com.example.grupo_iot.alumno.activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.grupo_iot.R;
import com.example.grupo_iot.alumno.entity.Alumno;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    Alumno alumnoIngresado;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Intent intent = getIntent();
        alumnoIngresado = (Alumno) intent.getSerializableExtra("alumno");

        // Obtén el mapa de Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        generarBottomNavigationMenu();

        Button btnMiUbicacion = findViewById(R.id.btnMiUbicacion);
        btnMiUbicacion.setOnClickListener(view -> mostrarUbicacionActual());
    }
    private void mostrarUbicacionActual() {
        try {
            // Verifica si los servicios de ubicación están habilitados
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                // Obtiene la última ubicación conocida
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, location -> {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
                            }
                        });
            } else {
                // Si los servicios de ubicación no están habilitados, muestra un mensaje al usuario
                Toast.makeText(this, "Habilita los servicios de ubicación", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        Antut(googleMap);
        //mostrarUbicacionActual();
    }


    public void Antut(GoogleMap googleMap){
        mMap = googleMap;
        final LatLng polideportivo = new LatLng(-12.066563783051707, -77.08034188931863);
        final LatLng pabellonv = new LatLng(-12.073052142865027,-77.08198769436298);
        final LatLng canchaminas = new LatLng(-12.07218069313465,-77.08193290503175);
        final LatLng bati = new LatLng(-12.073253462961755, -77.08163320224719);
        final LatLng digimundo = new LatLng(-12.073168701621984,-77.0811371912099);

        mMap.addMarker(new MarkerOptions().position(polideportivo).title("Polideportivo").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.addMarker(new MarkerOptions().position(pabellonv).title("Pabellon V").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.addMarker(new MarkerOptions().position(canchaminas).title("Cancha Minas").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.addMarker(new MarkerOptions().position(bati).title("Bati").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions().position(digimundo).title("Digimundo").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Verifica si el permiso fue otorgado
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso otorgado, muestra la ubicación actual en el mapa
                mostrarUbicacionActual();
            }
        }
    }
    public void generarBottomNavigationMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.navigation_lista_actividades){
                    Intent intent = new Intent(MapsActivity.this, ListaActividadesActivity.class);
                    intent.putExtra("alumno", alumnoIngresado);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_eventos_apoyados){
                    Intent intent = new Intent(MapsActivity.this, ListaEventosApoyadosActivity.class);
                    intent.putExtra("alumno", alumnoIngresado);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_lista_chats){
                    Intent intent = new Intent(MapsActivity.this, ListaDeChatsActivity.class);
                    intent.putExtra("alumno", alumnoIngresado);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_donaciones){
                    Intent intent = new Intent(MapsActivity.this, DonacionesActivity.class);
                    intent.putExtra("alumno", alumnoIngresado);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.navigation_perfil){
                    menuItem.setEnabled(false);
                    menuItem.setChecked(true);
                }
                return true;
            }
        });
    }
}