package com.rk_hk.nark.probandomapas.utilities;

/**
 * Created by nark_ on 13/07/2017.
 */

public class EnlaceParadero {
    private Paradero pInicio, pSiguiente;
    private double tiempoMedio;
    private double distanciaEntrePuntos;

    public EnlaceParadero(Paradero pInicio, Paradero pSiguiente, double distanciaEntrePuntos , double tiempoMedio) {
        this.pInicio = pInicio;
        this.pSiguiente = pSiguiente;
        this.tiempoMedio = tiempoMedio;
        this.distanciaEntrePuntos = distanciaEntrePuntos;
    }

    public Paradero getpInicio() {
        return pInicio;
    }

    public void setpInicio(Paradero pInicio) {
        this.pInicio = pInicio;
    }

    public Paradero getpSiguiente() {
        return pSiguiente;
    }

    public void setpSiguiente(Paradero pSiguiente) {
        this.pSiguiente = pSiguiente;
    }

    public double getTiempoMedio() {
        return tiempoMedio;
    }

    public void setTiempoMedio(double tiempoMedio) {
        this.tiempoMedio = tiempoMedio;
    }

    public double getDistanciaEntrePuntos() {
        return distanciaEntrePuntos;
    }

    public void setDistanciaEntrePuntos(double distanciaEntrePuntos) {
        this.distanciaEntrePuntos = distanciaEntrePuntos;
    }
}
