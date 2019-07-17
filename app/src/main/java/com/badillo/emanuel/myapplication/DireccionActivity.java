package com.badillo.emanuel.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import android.widget.SearchView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import java.util.List;

public class DireccionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;
    private double longitudOrigen;
    private double latitudOrigen;
    private double longitudDest;
    private double latitudDest;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationapi);




        searchView = findViewById(R.id.sv_location);
        // Obtenemos el SupportMapFragment y notificacion de que el mapa esta listo para ser usado.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        // Codigo de searchview
        //obtenemos el searchview y colocamos el comportamiento cuando se ingrese texto.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                //se verifica que el texto no este vacio
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(DireccionActivity.this);
                    try {
                        //Con ayuda del geocoder se obtiene la primer direccion encontrada del texto ingresado y se coloca en la lista de direcciones
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //se obtiene la longitud y latitud de la direccion.
                    Address address = addressList.get(0);
                    latitudDest=address.getLatitude();
                    longitudDest=address.getLongitude();
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    //se agrega el marcador y se mueve la vista hacia el marcador
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)
                            .zoom(14)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));




                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }

    //Funcion llamada cuando el mapa este disponible
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;

        mMap.clear();
        //controles del mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        //acercamientos minimo y maximo definidos

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DireccionActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }
        mMap.setMyLocationEnabled(true);

        LatLng miPosicion=new LatLng(latitudOrigen,longitudOrigen);
        //mMap.addMarker(new MarkerOptions().position(miPosicion).title("Aqui estas"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(miPosicion)
                .zoom(14)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
