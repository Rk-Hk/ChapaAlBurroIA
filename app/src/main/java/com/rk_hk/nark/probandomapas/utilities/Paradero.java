package com.rk_hk.nark.probandomapas.utilities;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nark_ on 10/07/2017.
 */

public class Paradero {

    //private Location paradero;
    private LatLng ubicPar;
    private String nomParadero;

    public Paradero( String nomParadero,LatLng ubicPar) {
        this.ubicPar = ubicPar;
        this.nomParadero = nomParadero;
    }

    public LatLng getUbicPar() {
        return ubicPar;
    }

    public void setUbicPar(LatLng ubicPar) {
        this.ubicPar = ubicPar;
    }

    public String getNomParadero() {
        return nomParadero;
    }

    public void setNomParadero(String nomParadero) {
        this.nomParadero = nomParadero;
    }
}
