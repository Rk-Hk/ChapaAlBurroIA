package com.rk_hk.nark.probandomapas;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rk_hk.nark.probandomapas.fixed.Contract;
import static com.rk_hk.nark.probandomapas.fixed.Contract.getSingletonContract;


public class EstudianteActivity extends AppCompatActivity implements OnMapReadyCallback,ValueEventListener {


    /**Instancia para el papa de nuestra activity
     * Coordenada de prueba : -12.186410, -76.980010
     *
     * Variables de la aplicaicon*/
    private GoogleMap myMap;
    private Contract c = getSingletonContract();
    private double fireLatitud ,fireLongitud, velocidadBus;

    /**Configuracion para la conexion a firebase
     * firebase trabaja como si fuera un arbol, por ello trabajamos con childs*/
    private final DatabaseReference dbApp = c.fireDB.getInstance().getReference();   //Referenciamos a la rama ubicacion, donde se guardan las coordenadas
   /** private final DatabaseReference dbCoordenadas = c.fireDB.getInstance().getReference().child("ubicacion");   //Referenciamos a la rama ubicacion, donde se guardan las coordenadas
    private final DatabaseReference dbEstadoBus = c.fireDB.getInstance().getReference().child("estado");        //Referenciamos a la rama estado, indica si el bus esta en movimiento o parado.
    private final DatabaseReference dbVelBus = c.fireDB.getInstance().getReference().child("velocidad");        //Referenciamos a la rama velocidad, indica si el bus esta en movimiento o parado.**/
    private MarkerOptions markerOptions;    //Marcador de ubicacion.

    /**Configuracion de las vistas**/

    TextView tv_distancia, tv_estado, tv_velocidad;

    /**
     *
     * private TextView infoTextView;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

     infoTextView = (TextView) findViewById(R.id.infoTextView);

     if (getIntent().getExtras() != null) {
     for (String key : getIntent().getExtras().keySet()) {
     String value = getIntent().getExtras().getString(key);
     infoTextView.append("\n" + key + ": " + value);
     }
     }

     String token = FirebaseInstanceId.getInstance().getToken();

     Log.d(TAG, "Token: " + token);
     }
     * ***/


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {   //Menú de la activity(Vista) estudiante
            switch (item.getItemId()) {
                case R.id.navigation_distance:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_time:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_algo:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {    //metodo que se ejecuta al abrir la activity estudiante
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Instanciamos al mapa de la vista
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ae_mapa);
        mapFragment.getMapAsync(this);

        //Escuchamos los cambios ocurridos en la rama dbcoordenadas de Firebase.
        dbApp.addValueEventListener(this);

        tv_distancia = (TextView) findViewById(R.id.abm_tv_distancia);
        tv_estado = (TextView) findViewById(R.id.abm_tv_estado);
        tv_velocidad = (TextView) findViewById(R.id.abm_tv_velocidad);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {   //Metodo callback que verifica si el FragmentMap (Mapa) esta disponible y le asigna sus caracteristicas que definimos dentro.
        myMap = googleMap;  //Iniciamos el mapa

        myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);   //Asignaos un tipo de mapa
        myMap.getUiSettings().setZoomControlsEnabled(true); //Iniciamos los botones de ZOOM
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {   //Metodo que esta a la escucha de los cambios de la rama dbCoordenada.
        myMap.clear();
        fireLatitud = Double.parseDouble(dataSnapshot.child("ubicacion").child("lat").getValue().toString()); //Guardamos la nueva latitud.
        fireLongitud =Double.parseDouble(dataSnapshot.child("ubicacion").child("lon").getValue().toString());  //Guaramos la nueva longitud

        String estadoBus = dataSnapshot.child("estado").getValue().toString();
        LatLng lastUbic = new LatLng(fireLatitud,fireLongitud); //LatLng representa a una coordenada

        String velocidad = dataSnapshot.child("velocidad").getValue().toString();
        Log.d("****VELOCIDAD",velocidad+" ");
        /**Mostramos los datos en los TextView**/

        tv_estado.setText("Estado:"+ estadoBus);
        tv_velocidad.setText("Velocidad:"+ velocidad+" km/h");
        tv_velocidad.setText("Distancia:");

        markerOptions = new MarkerOptions().position(lastUbic).title("Setear la actividad del bus");    //Creamos un marcador en la nueva coordenada
        myMap.addMarker(markerOptions); //Asignamos la nueva coordenada al mapa
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUbic,18));   //Movemos la camara a la nueva coordenada y pintamos el marker en el mapa
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
