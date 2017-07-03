package com.rk_hk.nark.probandomapas;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.iid.FirebaseInstanceId;

public class NotificationActivity extends AppCompatActivity {

    public static final String TAG = "NOTICIAS";
   // private TextView infoTextView;
    private ImageView imagenNotificacion;           //Aqui cargamos la imagen del anuncio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //infoTextView = (TextView) findViewById(R.id.contenedor_info_notificacion);
        imagenNotificacion = (ImageView) findViewById(R.id.contenedor_img_notificacion);
        /**Aqui recibimos la informaciond de la notificacion
         * */
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                System.out.println("Info ->>>>>>>>>>>>>>>>>> "+value);
                if(key.equalsIgnoreCase("imagen"))
                    Glide.with(this)
                            .load(value).apply(RequestOptions.noTransformation())
                            .into(imagenNotificacion);
                else if (key.equalsIgnoreCase("de"))
                    setTitle(value);
                /*
                else
                    infoTextView.append("\n" + key + ": " + value);*/
            }
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Evento Guardado Correctamente", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token: " + token);
    }
}
