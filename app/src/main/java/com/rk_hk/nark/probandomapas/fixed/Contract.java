package com.rk_hk.nark.probandomapas.fixed;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.rk_hk.nark.probandomapas.utilities.EnlaceParadero;
import com.rk_hk.nark.probandomapas.utilities.Paradero;

import java.util.ArrayList;

/**
 * Created by nark_ on 12/06/2017.
 */

public class Contract {

    /**Aqui Guardamos las constantes del sistema, para eso usamos un singleton
     *Solo se creara una vez y solo existira uno **/

    public static Contract contract;
    public static final FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
    public static final String LOGTAG = "android-localizacion";

    public static final int PETICION_PERMISO_LOCALIZACION = 101;
    public static final int PETICION_CONFIG_UBICACION = 201;

    public static final int TIEMPO_ENTRE_PARADEROS= 10;

    public static boolean STATUS_NOTIFICACION = false;

    /**Paraderos de UNMSM**/


    public  static   final Paradero p1 = new Paradero("Pueta 2",new LatLng(-12.05956313,-77.07962617));
    public  static   final Paradero p2 = new Paradero("Pueta 3", new LatLng(-12.05724962,-77.08023503));
    public  static   final Paradero pa0 = new Paradero("", new LatLng(-12.05640762,-77.08070844));
    public  static   final Paradero p3 = new Paradero("Clinica, Autoseguro", new LatLng(-12.055482, -77.081958));
    public  static   final Paradero pa01 = new Paradero("", new LatLng(-12.05528496, -77.08246797));
    public  static   final Paradero pa02 = new Paradero("", new LatLng(-12.05547906, -77.0827952));
    public  static   final Paradero pa03 = new Paradero("", new LatLng(-12.05543185, -77.08327264));
    public  static   final Paradero pa04 = new Paradero("", new LatLng(-12.05473936, -77.08365351));

    public  static   final Paradero p4 = new Paradero("Residencia, Puerta 7", new LatLng(-12.05418852,-77.08450243));
    public  static   final Paradero pa05 = new Paradero("", new LatLng(-12.05376883,-77.08514482));

    public  static   final Paradero p5 = new Paradero("Sistemas, Odontologia", new LatLng(-12.05368751,-77.08576038));
    public  static   final Paradero pa06 = new Paradero( "",new LatLng(-12.05351176,-77.08637327));

    public  static   final Paradero p6 = new Paradero("Electronica", new LatLng(-12.05449804,-77.08650738));
    public  static   final Paradero pa1 = new Paradero("", new LatLng(-12.05476559,-77.0865503));
    public  static   final Paradero pa2 = new Paradero("", new LatLng(-12.0549597,-77.08525211));
    public  static   final Paradero pa3 = new Paradero("", new LatLng(-12.05538463,-77.08479613));

    public  static   final Paradero p7 = new Paradero("Biblioteca Central, Rectorado", new LatLng(-12.05627909,-77.08508983));
    public  static   final Paradero pa4 = new Paradero("", new LatLng(-12.05799718,-77.08578318));
    public  static  final Paradero pa5 = new Paradero("", new LatLng(-12.05899918,-77.08459765));

    public  static  final Paradero p8 = new Paradero("Gimnasio, Educacion Fisica", new LatLng(-12.06014544,-77.08437368));
    public  static  final Paradero pa6 = new Paradero("", new LatLng(-12.06090349,-77.08423823));
    public  static  final Paradero p9= new Paradero("Comedor", new LatLng(-12.06078283,-77.08280459));
    public  static  final Paradero p10= new Paradero("Industrial", new LatLng(-12.06033954,-77.08065614));
    public  static  final Paradero pa7= new Paradero("", new LatLng(-12.05997494,-77.07968384));

    public  static  final ArrayList<Paradero> lsParaderos = new ArrayList<>();
    public  static  final ArrayList<Paradero> lsPuntosRuta = new ArrayList<>();

    /**Creando los enlaces de la ruta**/


    public static  void agregarPuntosRuta(){
        lsPuntosRuta.add(p1);
        lsPuntosRuta.add(p2);
        lsPuntosRuta.add(pa0);
        lsPuntosRuta.add(p3);
        lsPuntosRuta.add(pa01);
        lsPuntosRuta.add(pa02);
        lsPuntosRuta.add(pa03);
        lsPuntosRuta.add(pa04);
        lsPuntosRuta.add(p4);
        lsPuntosRuta.add(pa05);
        lsPuntosRuta.add(p5);
        lsPuntosRuta.add(pa06);
        lsPuntosRuta.add(p6);
        lsPuntosRuta.add(pa1);
        lsPuntosRuta.add(pa2);
        lsPuntosRuta.add(pa3);
        lsPuntosRuta.add(p7);
        lsPuntosRuta.add(pa4);
        lsPuntosRuta.add(pa5);
        lsPuntosRuta.add(p8);
        lsPuntosRuta.add(pa6);
        lsPuntosRuta.add(p9);
        lsPuntosRuta.add(p10);
        lsPuntosRuta.add(pa7);
    }

    public static void agregarParaderos(){
        lsParaderos.add(p1);
        lsParaderos.add(p2);
        lsParaderos.add(p3);
        lsParaderos.add(p4);
        lsParaderos.add(p5);
        lsParaderos.add(p6);
        lsParaderos.add(p7);
        lsParaderos.add(p8);
        lsParaderos.add(p9);
        lsParaderos.add(p10);
    }

    public  static final EnlaceParadero nep1p2 = new EnlaceParadero(p1,p2,266,62);
    public  static final EnlaceParadero nep2p3 = new EnlaceParadero(p2,p3,312,87);
    public  static final EnlaceParadero nep3p4 = new EnlaceParadero(p3,p4,355,67);
    public  static final EnlaceParadero nep4p5 = new EnlaceParadero(p4,p5,184,39);
    public  static final EnlaceParadero nep5p6 = new EnlaceParadero(p5,p6,194,52);
    public  static final EnlaceParadero nep6p7 = new EnlaceParadero(p6,p7,379,100);
    public  static final EnlaceParadero nep7p8 = new EnlaceParadero(p7,p8,510,38);
    public  static final EnlaceParadero nep8p9 = new EnlaceParadero(p8,p9,1187,261);
    public  static final EnlaceParadero nep9p10 = new EnlaceParadero(p9,p10,239,39);
    public  static final EnlaceParadero nep10p11 = new EnlaceParadero(p10,p1,160,31);

    public static final ArrayList<EnlaceParadero> rutaCompleta = new ArrayList<>();

    public static void armandoRutaCompleta(){
        rutaCompleta.add(nep1p2);
        rutaCompleta.add(nep2p3);
        rutaCompleta.add(nep3p4);
        rutaCompleta.add(nep4p5);
        rutaCompleta.add(nep5p6);
        rutaCompleta.add(nep6p7);
        rutaCompleta.add(nep7p8);
        rutaCompleta.add(nep8p9);
        rutaCompleta.add(nep9p10);
        rutaCompleta.add(nep10p11);
    }


    public static  LatLng myUbication;


    public Contract() {
    }

    public static Contract getSingletonContract(){
        if(contract == null){
            agregarParaderos();
            agregarPuntosRuta();
armandoRutaCompleta();
            myUbication = new LatLng(-12.053410,-77.085505);


            contract = new Contract();
        }
        return contract;
    }

    /**Constantes para medir distancia de punto a punto**/

    public static final double MAX_TIME_ESPERA = 1.5; //MINUTOS
    public static final double MIN_TIME_ESPERA = 0.5;

    public static final double VELOCIDAD=40;

    public static final int CONSTANTE_TIEMPO = 7;
    public static final double CONSTANTE_RADIO = -0.000057;

}
