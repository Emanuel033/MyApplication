package com.badillo.emanuel.myapplication;



import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;


public class LongLatActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private EditText long1;
    private EditText lat1;
    private EditText long2;
    private EditText lat2;
    private EditText long3;
    private EditText lat3;


    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longlat);



        // se obtiene el fragmento.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map2);
        mapFragment.getMapAsync(this);



        long1=findViewById(R.id.et1);
        lat1=findViewById(R.id.et2);
        long2=findViewById(R.id.et3);
        lat2=findViewById(R.id.et4);
        long3=findViewById(R.id.et5);
        lat3=findViewById(R.id.et6);
        final Button btnLongLat =  findViewById(R.id.btnColocarMarcadores);
        //Boton ppara colocar los marcadores
        btnLongLat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    if(validacionCampos(long1,lat1,long2,lat2,long3,lat3)) {
                        setMarkers(mMap, Double.parseDouble(String.valueOf(long1.getText()))
                                , Double.parseDouble(String.valueOf(lat1.getText()))
                                , Double.parseDouble(String.valueOf(long2.getText()))
                                , Double.parseDouble(String.valueOf(lat2.getText()))
                                , Double.parseDouble(String.valueOf(long3.getText()))
                                , Double.parseDouble(String.valueOf(lat3.getText()))
                        );
                    }

            }
        });
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


    }

        public boolean validacionCampos(EditText lat1,EditText long1,EditText lat2,EditText long2,EditText lat3,EditText long3){
        if(long1.getText().toString().isEmpty()){
            Toast.makeText(this,"Longitud 1 no colocada",Toast.LENGTH_SHORT).show();
            return false;
        }else if(lat1.getText().toString().isEmpty()){
            Toast.makeText(this,"Latitud 1 no colocada",Toast.LENGTH_SHORT).show();
            return false;
        }else if(long2.getText().toString().isEmpty()) {
            Toast.makeText(this, "Longitud 2 no colocada", Toast.LENGTH_SHORT).show();
            return false;
        }else if(lat2.getText().toString().isEmpty()){
            Toast.makeText(this,"Latitud 2 no colocada",Toast.LENGTH_SHORT).show();
            return false;
        }else if(long3.getText().toString().isEmpty()) {
            Toast.makeText(this, "Longitud 3 no colocada", Toast.LENGTH_SHORT).show();
            return false;
        }else if(lat3.getText().toString().isEmpty()){
            Toast.makeText(this,"Latitud 3 no colocada",Toast.LENGTH_SHORT).show();
            return false;
        }else { return true;}
    }


    //obtiene 3 coordenadas y coloca los marcadores
    public void setMarkers(GoogleMap googleMap,double lat1,double long1,double lat2,double long2,double lat3,double long3){
        mMap = googleMap;
        //se colocan los controles del mapa
        mMap.clear();

        //validacion de que los campos esten con datos

            //variables para las longitudes y latitudes de las coordenadas
            LatLng location1 = new LatLng(lat1, long1);
            LatLng location2 = new LatLng(lat2, long2);
            LatLng location3 = new LatLng(lat3, long3);
            //Se busca que los marcadores queden dentro de la visibilidad del mapa, pasando estos valores no se notan los marcadores.
            if (location1.longitude > 79 || location1.longitude < -78.5) {
                Toast toast = Toast.makeText(this, "Marcador 1 no válido", Toast.LENGTH_LONG);
                toast.show();
            } else {
                mMap.addMarker(new MarkerOptions().position(location1).title("location1"));

            }
            if (location2.longitude > 79 || location2.longitude < -78.5) {
                Toast toast1 = Toast.makeText(this, "Marcador 2 no válido", Toast.LENGTH_LONG);
                toast1.show();
            } else {
                mMap.addMarker(new MarkerOptions().position(location2).title("location2"));

            }
            if (location3.longitude > 79 || location3.longitude < -78.5) {
                Toast toast2 = Toast.makeText(this, "Marcador 3 no válido", Toast.LENGTH_LONG);
                toast2.show();
            } else {
                mMap.addMarker(new MarkerOptions().position(location3).title("location3"));
            }

            //se mueve la camara al marcador 1
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location1));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
