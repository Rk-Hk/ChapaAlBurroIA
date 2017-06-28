package com.rk_hk.nark.probandomapas.utilities;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Daniel Alvarez on 8/25/16.
 * Copyright © 2016 Alvarez.tech. All rights reserved.
 */
public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = "NOTICIAS";

    /**
     * Este metodo nos avisara cuando firebase nos asignan un token o cuando el token cambia.
     * **/
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "------------------------------------Token: " + token);

        //enviarTokenAlServidor(token);
    }

    private void enviarTokenAlServidor(String token) {
        // Enviar token al servidor
    }
}
