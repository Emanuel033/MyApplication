package com.badillo.emanuel.myapplication;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AutoUpdatePositionActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private DatabaseReference mDatabase;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> RealTimeMarkers = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    private CountDownTimer realTimeTimer;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_update_position);

        //subir unicacion a firebase
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // se obtiene el fragmento.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_m);
        mapFragment.getMapAsync(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (mDatabase != null) {
            actualizarLatLongFirebase();
        }else{
            subirLatLongFirebase();
        }
        mapFragment.getMapAsync(this);






    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //actualizarLatLongFirebase();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

            }
        });
        mDatabase.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(Marker marker:RealTimeMarkers){
                    marker.remove();
                }
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    MapsPojo mp = dataSnapshot.getValue(MapsPojo.class);
                    Double latitud = mp.getLatitud();
                    Double longitud = mp.getLongitud();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitud, longitud));
                    tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitud, longitud)));
                }
                RealTimeMarkers.clear();
                RealTimeMarkers.addAll(tmpRealTimeMarkers);
                countDownTimer();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void subirLatLongFirebase() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AutoUpdatePositionActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            /*LatLng l1=new LatLng(location.getLatitude(),location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(l1).title("Mi ubicacion Actual"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(l1));*/
                            Map<String,Object> latLong = new HashMap<>();
                            latLong.put("latitud",location.getLatitude());
                            latLong.put("longitud",location.getLongitude());
                            mDatabase.child("Usuarios").push().setValue(latLong);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
                            markerOptions.flat(true);
                            mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                        }
                    }
                });

    }
    private void actualizarLatLongFirebase() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AutoUpdatePositionActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            /*LatLng l1=new LatLng(location.getLatitude(),location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(l1).title("Mi ubicacion Actual"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(l1));*/
                            Map<String,Object> latLong = new HashMap<>();
                            latLong.put("latitud",location.getLatitude());
                            latLong.put("longitud",location.getLongitude());


                            mDatabase.child("Usuarios").updateChildren(latLong);
                        }
                    }
                });

    }

    private void countDownTimer(){
        if (realTimeTimer != null) {
            realTimeTimer.cancel();
        }
        realTimeTimer =new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Toast.makeText(AutoUpdatePositionActivity.this,"Ubicacion Actualizada",Toast.LENGTH_SHORT).show();
                actualizarLatLongFirebase();
                mMap.clear();
                onMapReady(mMap);
            }
        }.start();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
        this.finishAfterTransition();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {

        super.onPause();
        //System.exit(0);
        this.finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        //System.exit(0);
    }

    @Override
    public void finishAfterTransition() {
        super.finishAfterTransition();
        System.exit(0);
    }
}
