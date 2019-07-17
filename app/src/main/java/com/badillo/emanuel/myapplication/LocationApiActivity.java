package com.badillo.emanuel.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.PolyUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Arrays;
import java.util.List;

public class LocationApiActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;
    private double longitudOrigen;
    private double latitudOrigen;
    private double longitudDest=-99.1440317;
    private double latitudDest=19.5181771;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    //JSONObject jso;
    String TAG = "placeautocomplete";
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion);
        txtView = findViewById(R.id.txtView);

        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyBH-QMYV58luAYAdnMXtMyQxSPIsCMP4Ec");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                txtView.setText(place.getName()+","+place.getId());
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                Toast.makeText(LocationApiActivity.this,"Place: " + place.getName() + ", " + place.getId(),Toast.LENGTH_SHORT).show();


                LatLng latLng = place.getLatLng();
                //se agrega el marcador y se mueve la vista hacia el marcador
                mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(14)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
                Toast.makeText(LocationApiActivity.this,"Ocurrio un error"+status,Toast.LENGTH_SHORT).show();
            }
        });


        // Obtenemos el SupportMapFragment y notificacion de que el mapa esta listo para ser usado.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

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

            ActivityCompat.requestPermissions(LocationApiActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                longitudOrigen=location.getLongitude();
                latitudOrigen=location.getLatitude();

            }
        });
        LatLng miPosicion=new LatLng(latitudOrigen,longitudOrigen);
        //mMap.addMarker(new MarkerOptions().position(miPosicion).title("Aqui estas"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(miPosicion)
                .zoom(14)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public void trazarRuta(JSONObject jso){
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try{
            jRoutes = jso.getJSONArray("routes");

            for (int i=0; i<jRoutes.length();i++){
                jLegs =((JSONObject)(jRoutes.get(i))).getJSONArray("legs");
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ((JSONObject)(jLegs.get(j))).getJSONArray("steps");
                    for(int k=0;k<jSteps.length();k++){

                        String polyline=""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end",""+polyline);
                        List<LatLng> list = PolyUtil.decode(polyline);
                        mMap.addPolyline(new PolylineOptions().addAll(list).color(Color.GRAY).width(5));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;

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
