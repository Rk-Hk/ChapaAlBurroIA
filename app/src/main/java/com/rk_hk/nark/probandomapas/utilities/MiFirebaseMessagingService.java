package com.rk_hk.nark.probandomapas.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rk_hk.nark.probandomapas.LoginConductorActivity;
import com.rk_hk.nark.probandomapas.NotificationActivity;
import com.rk_hk.nark.probandomapas.SeleccionPerfilActivity;

/**
 * Created by Daniel Alvarez on 8/25/16.
 * Copyright © 2016 Alvarez.tech. All rights reserved.
 */
public class MiFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "NOTICIAS";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String from = remoteMessage.getFrom();
        Log.d(TAG, "Mensaje recibido de: " + from);

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notificación: " + remoteMessage.getNotification().getBody());

           mostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
        }
    }


    /**
     * Mediante este metodo , ENVIAMOS LA NOTIFICACION A NUESTRA ACTIVITY DE NOTIFICACIONES
     */
    private void mostrarNotificacion(String title, String body) {

        Intent intent = new Intent(this, SeleccionPerfilActivity.class);   //Especificamo a que intent ENVIAMOS LA INFORMACION RECIBIDA
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);    //sonido a la notificacion

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)   //Construimos una notificacion
                //.setSmallIcon(R.drawable.distance)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }

}
