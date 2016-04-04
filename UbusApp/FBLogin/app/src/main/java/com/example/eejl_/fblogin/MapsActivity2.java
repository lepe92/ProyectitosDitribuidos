package com.example.eejl_.fblogin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    LocationManager locationManager;
    private GoogleMap mMap;
    private ArrayList<String> ruta;
    ArrayList<LatLng> coordList = new ArrayList<LatLng>();
    private ArrayList<String> coordenada;
    private ArrayList<String> coordenada2 = new ArrayList<>();
    private ArrayList<String> polyid=new ArrayList<>();
    String provider;
    protected LatLng ubicacionActual = new LatLng(20.732360000000003, -103.35151);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ruta = new ArrayList<>();
        coordenada = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        String[] m = extras.getStringArray("coordenada");
        for (int i = 0; i < m.length; i++) {
            Log.i("mensaje", m[i]);
            String[] temp = m[i].split("/");
            ruta.add(temp[0]);
            coordenada.add("{\"Ruta\":[" + temp[1] + "]}");
            coordenada2.add(temp[1]);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Toast.makeText(this, "lat" + lat + "lng" + lng, Toast.LENGTH_SHORT).show();
        Log.i("mensaje", "lat" + lat + "lng" + lng);

        // LatLng sydney=ubicacionActual;
        //descomentar cuando se requiera realmente la ubicacion del gps
        ubicacionActual = new LatLng(lat, lng);
        LatLng sydney = ubicacionActual;
        MarkerOptions m = new MarkerOptions().position(sydney).title("Mi ubicacion").draggable(true);
        //m.icon(BitmapDescriptorFactory.fromResource(R.mipmap.bus));
        mMap.addMarker(m);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("mensaje", arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
//reenviar al simulador mi ubicación
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                ubicacionActual = arg0.getPosition();

            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);

            // Initialize the location fields
                if (location != null) {
                    System.out.println("Provider " + provider + " has been selected.");
                    onLocationChanged(location);


                } else {
                    Log.i("mensaje", "Location not available");

            }
dibujarMapa();
    }



    public void dibujarMapa(){
        mMap.clear();
        MarkerOptions m=new MarkerOptions().position(ubicacionActual).title("Mi ubicacion").draggable(true);
        //m.icon(BitmapDescriptorFactory.fromResource(R.mipmap.bus));
        mMap.addMarker(m);


        JSONObject jsnobject = null;
        try {
            for(int j=0; j<coordenada.size();j++){
                coordList.clear();
            jsnobject = new JSONObject(coordenada.get(j));
            JSONArray jsonArray = jsnobject.getJSONArray("Ruta");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                coordList.add(new LatLng(Double.parseDouble(explrObject.getString("lat")), Double.parseDouble(explrObject.getString("lng"))));
//Log.i("mensaje",explrObject.getString("lat"));
            }

            PolylineOptions polylineOptions = new PolylineOptions();
                Random rnd = new Random();
                int color2 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
// Create polyline options with existing LatLng ArrayList
            polylineOptions.addAll(coordList);
            polylineOptions
                    .width(5)
                    .color(color2).clickable(true);

// Adding multiple points in map using polyline and arraylist
            String indexpoly=mMap.addPolyline(polylineOptions).getId();

            polyid.add(indexpoly);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            public void onPolylineClick(Polyline polyline) {
                //int strokeColor = polyline.getColor() ^ 0x0000CC00;
               // polyline.setColor(Color.BLUE);
                int index=polyid.indexOf(polyline.getId());
                Toast.makeText(getApplicationContext(), "Ruta: " + ruta.get(polyid.indexOf(polyline.getId())), Toast.LENGTH_LONG).show();

                Intent m = new Intent(MapsActivity2.this, Mapa.class);
                m.putExtra("coordenada",ruta.get(index)+"/"+coordenada2.get(index));
                startActivity(m);
            }
        });
    }
}

