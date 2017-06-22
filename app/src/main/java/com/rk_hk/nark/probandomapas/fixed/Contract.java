package com.rk_hk.nark.probandomapas.fixed;

import com.google.firebase.database.FirebaseDatabase;

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

    public Contract() {
    }

    public static Contract getSingletonContract(){
        if(contract == null){
            contract = new Contract();
        }
        return contract;
    }
}
