package com.rk_hk.nark.probandomapas;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rk_hk.nark.probandomapas.fixed.Contract;


import android.content.IntentSender;
import android.util.Log;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import android.Manifest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.rk_hk.nark.probandomapas.fixed.Contract.getSingletonContract;

public class BusMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleMap myMap;

    private Contract c = getSingletonContract();

    /**Configuracion para la conexion a firebase*/
    private final DatabaseReference dbCoordenadas = c.fireDB.getInstance().getReference().child("ubicacion");
    private final DatabaseReference dbEstadoBus = c.fireDB.getInstance().getReference().child("estado");
    private final DatabaseReference dbVelBus = c.fireDB.getInstance().getReference().child("velocidad");
    private MarkerOptions markerOptions;

    private double prev_Latitud, prev_Longitud, fireLatitud ,fireLongitud;

    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Date fireDate;  // = new Date(location.getTime());
    //String formatted = format.format(date);

    private LocationRequest locRequest;

    private TextView lblLatitud;
    private TextView lblLongitud;
    private TextView lblHora;
    private Switch btnActualizar;


    public static GoogleApiClient apiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /**Iniciamos los widgets , con sus respectivos ID*/
        lblLatitud = (TextView) findViewById(R.id.lblLatitud);
        lblLongitud = (TextView) findViewById(R.id.lblLongitud);
        lblHora = (TextView) findViewById(R.id.lblHora);
        btnActualizar = (Switch) findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLocationUpdates(btnActualizar.isChecked());
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.cm_mapa);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //Construcción cliente API Google
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void toggleLocationUpdates(boolean enable) {
        if (enable) {
            enableLocationUpdates();
        } else {
            disableLocationUpdates();
        }
    }



    /**Metodo para veificar si la configuracion del celular , es correcta para usar la localizacion*/
    private void enableLocationUpdates() {

        locRequest = new LocationRequest();
        locRequest.setInterval(2500);
        locRequest.setFastestInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest locSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locRequest)
                        .build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        apiClient, locSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(c.LOGTAG, "Configuración correcta");
                        startLocationUpdates();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.i(c.LOGTAG, "Se requiere actuación del usuario");
                            status.startResolutionForResult(BusMapActivity.this, c.PETICION_CONFIG_UBICACION);
                        } catch (IntentSender.SendIntentException e) {
                            btnActualizar.setChecked(false);
                            Log.i(c.LOGTAG, "Error al intentar solucionar configuración de ubicación");
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(c.LOGTAG, "No se puede cumplir la configuración de ubicación necesaria");
                        btnActualizar.setChecked(false);
                        break;
                }
            }
        });
    }

    /**Metodo para apagar el servicio de localizacion**/
    private void disableLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(BusMapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
            //Sería recomendable implementar la posible petición en caso de no tenerlo.

            Log.i(c.LOGTAG, "Inicio de recepción de ubicaciones");

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    apiClient, locRequest, BusMapActivity.this);
        }
    }


    /**Aqui comenzamos Con los metodos para el mapa*/

    //Metodo callback que verifica si el FragmentMap (Mapa) esta disponible y le asigna sus caracteristicas que definimos dentro.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        // Add a marker in Sydney and move the camera

        myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(c.LOGTAG, "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    c.PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);   //Guardamos la ultima ubicacion registrada

            updateUI(lastLocation);
        }
    }

    /**METODO PRINCIPAL : Verifica si la ubicacion a cambiado**/
    @Override
    public void onLocationChanged(Location location) {
        updateUI(location);     //Mostramos la nueva ubicación recibida
    }


    /** tomamos la ultima coordenada y la mostramos en el mapa
     *
     * **/

    private void updateUI(Location loc) {
        if (loc != null) {
            myMap.clear();//Limpiamos el mapa de Markets antiguos

            /**Capturamos la nueva coordenada */
            fireLatitud = Double.parseDouble(String.valueOf(loc.getLatitude()));    //Guardamos la latitud tomada del GPS
            fireLongitud = Double.parseDouble(String.valueOf(loc.getLongitude()));  //Guardamos la longitud tomada del GPS
            fireDate = new Date(loc.getTime()); //Guaramos la hora del ultimo registro
            //String cadenaHora = format.format(fireDate);

            /**Enviamos a Firebase*/
            dbCoordenadas.child("lat").setValue(String.valueOf(loc.getLatitude()));
            dbCoordenadas.child("lon").setValue(String.valueOf(loc.getLongitude()));
            //dbCoordenadas.child("timestamp").setValue(cadenaHora);

            /**capturamos la velocidda **/
            Double velocidad = loc.getSpeed()*3.6;
            dbVelBus.setValue(velocidad);


            /**Msotramos en la vista la informacion*/

            lblLatitud.setText("LATITUD \n" + String.valueOf(loc.getLatitude()));
            lblLongitud.setText("LONGITUD \n" + String.valueOf(loc.getLongitude()));
            lblHora.setText("VELOCIDAD \n" + String.valueOf(velocidad)+" km/h");
            //lblHora.setText("HORA \n"+ cadenaHora);

            /**Ahora pintamos ubicacion en mapa*/

            LatLng lastUbic = new LatLng(fireLatitud,fireLongitud);

            if(velocidad<=5){
                markerOptions = new MarkerOptions().position(lastUbic).title("ESPERANDO").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus));
                dbEstadoBus.setValue("Esperando");
            }
            else{
                markerOptions = new MarkerOptions().position(lastUbic).title("MOVIENDOSE").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus));
                dbEstadoBus.setValue("Avanzando");
            }
            myMap.addMarker(markerOptions);
            //Pintamos el nuevo Marker en el mapa
            //myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUbic,18));
            myMap.moveCamera(CameraUpdateFactory.newLatLng(lastUbic));


        } else {
            lblLatitud.setText("Latitud: (desconocida)");
            lblLongitud.setText("Longitud: (desconocida)");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {//Se ha interrumpido la conexión con Google Play Services

        Log.e(c.LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == c.PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(c.LOGTAG, "Permiso denegado");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 210:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(c.LOGTAG, "El usuario no ha realizado los cambios de configuración necesarios");
                        btnActualizar.setChecked(false);
                        break;
                }
                break;
        }
    }

}
