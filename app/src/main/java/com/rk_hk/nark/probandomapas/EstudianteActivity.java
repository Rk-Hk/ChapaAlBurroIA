package com.rk_hk.nark.probandomapas;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rk_hk.nark.probandomapas.fixed.Contract;
import com.rk_hk.nark.probandomapas.utilities.Paradero;

import static com.rk_hk.nark.probandomapas.fixed.Contract.getSingletonContract;
import static com.rk_hk.nark.probandomapas.fixed.Contract.lsParaderos;
import static com.rk_hk.nark.probandomapas.fixed.Contract.lsPuntosRuta;
import static com.rk_hk.nark.probandomapas.fixed.Contract.myUbication;
import static com.rk_hk.nark.probandomapas.fixed.Contract.rutaCompleta;


public class EstudianteActivity extends AppCompatActivity implements OnMapReadyCallback,ValueEventListener {
/**
 * Datos para mostrat distancia y recomendacion de paradero
 * **/
    private int idParadero=-1;


    /**Instancia para el papa de nuestra activity
     * Coordenada de prueba : -12.186410, -76.980010
     *
     * Variables de la aplicaicon*/
    private GoogleMap myMap;
    private Contract c = getSingletonContract();
    private double fireLatitud ,fireLongitud, velocidadBus;
    private boolean tipo_mapa = true;

    /**Configuracion para la conexion a firebase
     * firebase trabaja como si fuera un arbol, por ello trabajamos con childs*/
    private final DatabaseReference dbApp = c.fireDB.getInstance().getReference();   //Referenciamos a la rama ubicacion, donde se guardan las coordenadas
   /** private final DatabaseReference dbCoordenadas = c.fireDB.getInstance().getReference().child("ubicacion");   //Referenciamos a la rama ubicacion, donde se guardan las coordenadas
    private final DatabaseReference dbEstadoBus = c.fireDB.getInstance().getReference().child("estado");        //Referenciamos a la rama estado, indica si el bus esta en movimiento o parado.
    private final DatabaseReference dbVelBus = c.fireDB.getInstance().getReference().child("velocidad");        //Referenciamos a la rama velocidad, indica si el bus esta en movimiento o parado.**/
    private MarkerOptions markerOptions;    //Marcador de ubicacion.

    TextView tv_distancia, tv_tiempo_medio;

    // Instantiates a new Polygon object and adds points to define a rectangle
    private PolygonOptions rutaBurro ;


    /**Configuracion de las vistas**/

    public Location obtenerLocationLatLon(LatLng latLng){
        Location auxLocation = new Location("Pruebas");
        auxLocation.setLongitude(latLng.longitude);
        auxLocation.setLatitude(latLng.latitude);
        return auxLocation;
    }

        public int distanciaAlParaderoMasCercano(Location location){
            int idParad=0;
            double distAux, distanciamin = location.distanceTo(obtenerLocationLatLon(lsParaderos.get(0).getUbicPar()));

            for(int i=1 ;i<lsParaderos.size();i++){
                distAux =location.distanceTo(obtenerLocationLatLon(lsParaderos.get(i).getUbicPar()));
                if(distAux<distanciamin){
                    distanciamin=distAux;
                    idParad=i;
                }
            }
            return idParad;
        }

        public void verificarSiPasoBurro(){
            int idParader = distanciaAlParaderoMasCercano(obtenerLocationLatLon(new LatLng(fireLatitud,fireLongitud)));
            double distanciatotal = 0;
            double tiempoMedio = 0;
            if(idParader>idParadero){
                for (int i =0;i<rutaCompleta.size();i++){
                    if(i<idParadero || i>idParader) {
                        distanciatotal = distanciatotal + rutaCompleta.get(i).getDistanciaEntrePuntos();
                        tiempoMedio = tiempoMedio + rutaCompleta.get(i).getTiempoMedio();
                    }
                }
            }else{
                if(idParader<idParadero){
                    for (int i =0;i<rutaCompleta.size();i++){
                        if(i>idParader && i<idParadero) {
                            distanciatotal = distanciatotal + rutaCompleta.get(i).getDistanciaEntrePuntos();
                            tiempoMedio =tiempoMedio + rutaCompleta.get(i).getTiempoMedio();
                        }
                    }
                }
            }

            tv_tiempo_medio.setText("Tiempo Medio: "+tiempoMedio/60+" min");
            tv_distancia.setText("Distancia: "+distanciatotal/100+" km");


        }

        public void pintarMiUbicacion(){
            MarkerOptions marker = new MarkerOptions().position(myUbication).title("Tu Ubicaion :3").icon(BitmapDescriptorFactory.fromResource(R.drawable.position));
            if(myMap!=null){
            myMap.addMarker(marker);}
        }

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
        tv_tiempo_medio = (TextView) findViewById(R.id.abm_tv_tiempomedio);

        rutaBurro = new PolygonOptions();
        PintarRutaBurro();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {   //Menú de la activity(Vista) estudiante
            switch (item.getItemId()) {
                case R.id.navigation_distance:
                    idParadero = distanciaAlParaderoMasCercano(obtenerLocationLatLon(myUbication));
                    pintarMiUbicacion();
                    verificarSiPasoBurro();

                    return true;
                case R.id.navigation_time:
                    if(myMap!=null){
                        if(tipo_mapa){
                            item.setTitle(R.string.title_map_hibrido);
                            myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            tipo_mapa=false;
                        }else{
                            item.setTitle(R.string.title_map_hibrido);
                            myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            tipo_mapa=true;
                        }
                    }
                    return true;
                case R.id.navigation_algo:
                    Toast.makeText(EstudianteActivity.this, "Proximamente, en la nueva version", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }

    };

    public void PintarRutaBurro(){
        for(int i =0;i<lsPuntosRuta.size();i++){
            rutaBurro.add(lsPuntosRuta.get(i).getUbicPar());
        }
        rutaBurro.add(lsPuntosRuta.get(0).getUbicPar());
    }

    public void PintarParaderos(){
        MarkerOptions marker;
        for(int i = 0 ;i< lsParaderos.size();i++){
            if(idParadero == -1  ){
                marker = new MarkerOptions().position(lsParaderos.get(i).getUbicPar()).title(lsParaderos.get(i).getNomParadero()).icon(BitmapDescriptorFactory.fromResource(R.drawable.paradero));
            }else{
                if (i == idParadero){
                    marker = new MarkerOptions().position(lsParaderos.get(i).getUbicPar()).title(lsParaderos.get(i).getNomParadero().toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.paradero64));
                }else{
                    marker = new MarkerOptions().position(lsParaderos.get(i).getUbicPar()).title(lsParaderos.get(i).getNomParadero().toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.paradero));
                }
            }

            // markerOptions = new MarkerOptions().position(lastUbic).title("Setear la actividad del bus");    //Creamos un marcador en la nueva coordenada
            myMap.addMarker(marker);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {   //Metodo callback que verifica si el FragmentMap (Mapa) esta disponible y le asigna sus caracteristicas que definimos dentro.
        myMap = googleMap;  //Iniciamos el mapa


        PintarParaderos();
        myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);   //Asignaos un tipo de mapa
        myMap.getUiSettings().setZoomControlsEnabled(true); //Iniciamos los botones de ZOOM
        myMap.addPolygon(rutaBurro);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {   //Metodo que esta a la escucha de los cambios de la rama dbCoordenada.
        myMap.clear();
        PintarParaderos();
        pintarMiUbicacion();
        myMap.addPolygon(rutaBurro);
        fireLatitud = Double.parseDouble(dataSnapshot.child("ubicacion").child("lat").getValue().toString()); //Guardamos la nueva latitud.
        fireLongitud =Double.parseDouble(dataSnapshot.child("ubicacion").child("lon").getValue().toString());  //Guaramos la nueva longitud

        String estadoBus = dataSnapshot.child("estado").getValue().toString();
        LatLng lastUbic = new LatLng(fireLatitud,fireLongitud); //LatLng representa a una coordenada

        String velocidad = dataSnapshot.child("velocidad").getValue().toString();
        Log.d("****VELOCIDAD",velocidad+" ");
        /**Mostramos los datos en los TextView**/


        markerOptions = new MarkerOptions().position(lastUbic).title(":3").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus));
       // markerOptions = new MarkerOptions().position(lastUbic).title("Setear la actividad del bus");    //Creamos un marcador en la nueva coordenada
        myMap.addMarker(markerOptions); //Asignamos la nueva coordenada al mapa
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUbic,18));   //Movemos la camara a la nueva coordenada y pintamos el marker en el mapa
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
