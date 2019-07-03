package com.badillo.emanuel.myapplication;

public class MapsPojo {
    //los nombres deben ser iguales a los de firebase
    double latitud;
    double longitud;

    public MapsPojo() {

    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
