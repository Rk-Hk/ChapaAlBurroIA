package com.rk_hk.nark.probandomapas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;

public class ProbandoActivity extends AppCompatActivity {


    public static final String TAG = "NOTICIAS";
    private TextView infoTextView;
    private ImageView imagenNotificacion;
    private String rutaImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoTextView = (TextView) findViewById(R.id.infoTextView);
        imagenNotificacion = (ImageView) findViewById(R.id.imagenNotificacion);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                if(key.equalsIgnoreCase("imagen")){
                    System.out.println("Ruta de imagen ->>>>>>>>>>> "+value);
                    Glide.with(this).load(value).into(imagenNotificacion);
                }
                else
                    infoTextView.append("\n" + key + ": " + value);
            }
        }

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Token: " + token);
    }
}
