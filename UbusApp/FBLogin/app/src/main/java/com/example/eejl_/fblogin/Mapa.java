package com.example.eejl_.fblogin;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Mapa extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    WebView v1;
    Bitmap bitmap;
    Dialog builder;
    ImageView imageView;
    boolean ver=false;
    private GoogleMap mMap;
    LocationManager locationManager;
    String Ruta[];
    String provider;
    protected DataOutputStream dos;
    protected DataInputStream dis;
    ArrayList<LatLng> coordList = new ArrayList<LatLng>();
//se sustituirá por la del gps posteriormente
    protected LatLng ubicacionActual=new LatLng(20.732360000000003,-103.35151);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String temp = extras.getString("coordenada");
            Ruta = temp.split("/");
            Log.i("mensaje", Ruta[0]);
            Ruta[1] = "{\"Ruta\":[" + Ruta[1] + "]}";
            Log.i("mensaje", Ruta[1]);
        }
        // Get the location manager



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
       v1=(WebView) findViewById(R.id.webview);
        v1.setVisibility(View.INVISIBLE);

        Button solicita=(Button)findViewById(R.id.mapButtonSolicita);
        Button aborda=(Button)findViewById(R.id.mapButtonAborda);
        Button chofer=(Button)findViewById(R.id.mapButtonChofer);

        solicita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog loading = ProgressDialog.show(Mapa.this, "Pidiendo camión...", null, true, true);
            }
        });

        aborda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog loading = ProgressDialog.show(Mapa.this, "Subiendo al camión...", null, true, true);
            }
        });

        chofer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    builder = new Dialog(Mapa.this);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            //nothing;
                        }
                    });

                    imageView = new ImageView(Mapa.this);
                LoadImage m= new LoadImage();
                m.execute();
                //  imageView.setImageBitmap(getBitmapFromURL("http://jimenezlepe.comuv.com/Ubus/choferes/leo.jpg"));


/*                if(ver==false){
                v1.setVisibility(View.VISIBLE);
                    v1.loadUrl("http://jimenezlepe.comuv.com/Ubus/choferes/leo.jpg");

                ver=true;
                }
                else{
                    v1.setVisibility(View.INVISIBLE);
                    ver=false;
                }
                //ProgressDialog loading = ProgressDialog.show(Mapa.this, "Descargando foto del chofer...", null, true, true);
*/
            }
        });




        Timer timer = new Timer();
        //cuando cambie la ubicación se debe contactar al simulador cada cinco segundos
        TimerTask obtenCamiones = new contacta();
        timer.scheduleAtFixedRate(obtenCamiones, 1000, 5000);
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn2 = aURL.openConnection();
            conn2.connect();
            InputStream is = conn2.getInputStream();
            Log.d("mensaje", "descargando imagen");
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.d("mensaje", e+"");
        }
        return bm;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } // Author: silentnuke

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Toast.makeText(this, "lat" + lat + "lng" + lng, Toast.LENGTH_SHORT).show();
        Log.i("mensaje", "lat" + lat + "lng" + lng);

       // LatLng sydney=ubicacionActual;
       //descomentar cuando se requiera realmente la ubicacion del gps
        ubicacionActual = new LatLng(lat, lng);
        LatLng sydney=ubicacionActual;
        MarkerOptions m=new MarkerOptions().position(sydney).title("Mi ubicacion").draggable(true);
        //m.icon(BitmapDescriptorFactory.fromResource(R.mipmap.bus));
        mMap.addMarker(m);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));



    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        protected Bitmap doInBackground(String... args) {
            try {
                URL url=new URL("http://jimenezlepe.comuv.com/Ubus/choferes/leo.jpg");
                bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                imageView.setImageBitmap(image);
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                builder.show();
            }else{

                Toast.makeText(Mapa.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    class contacta extends TimerTask {
        public void run() {
            ContactarSimulador myClientTask= new ContactarSimulador();
            myClientTask.execute();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

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
                Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("mensaje", arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
//reenviar al simulador mi ubicación
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                ubicacionActual=arg0.getPosition();

            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });


        // Add a marker in Sydney and move the camera
        try {
            JSONObject jsnobject = new JSONObject(Ruta[1]);
            JSONArray jsonArray = jsnobject.getJSONArray("Ruta");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                coordList.add(new LatLng(Double.parseDouble(explrObject.getString("lat")), Double.parseDouble(explrObject.getString("lng"))));

            }

            PolylineOptions polylineOptions = new PolylineOptions();

// Create polyline options with existing LatLng ArrayList
            polylineOptions.addAll(coordList);
            polylineOptions
                    .width(5)
                    .color(Color.RED);

// Adding multiple points in map using polyline and arraylist
            mMap.addPolyline(polylineOptions);

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
                Log.i("mensaje","Location not available");

            }
        }catch(Exception eo){}
//colocar mi marcador de posicion

    }

    public class ContactarSimulador extends AsyncTask<String, Void, String> {
        String dstAddress="10.0.5.121";
        int dstPort=5000;
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... arg0) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                Log.i("mensaje", "conectado");
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());

                JSONObject jsonObject = new JSONObject();
                try {
                    //ubicacion y ruta
                    jsonObject.put("Ruta", Ruta[0]);
                    jsonObject.put("lat",ubicacionActual.latitude);
                    jsonObject.put("lng",ubicacionActual.longitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String message = jsonObject.toString();

                dos.writeUTF(message);


                    response = dis.readUTF();

                    Log.i("mensaje", response);

                return response;
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //procesar el JSON que se ha recibido
            super.onPostExecute(s);

            mMap.clear();



            // Add a marker in Sydney and move the camera

              PolylineOptions polylineOptions = new PolylineOptions();

// Create polyline options with existing LatLng ArrayList
                polylineOptions.addAll(coordList);
                polylineOptions
                        .width(5)
                        .color(Color.RED);

// Adding multiple points in map using polyline and arraylist
                mMap.addPolyline(polylineOptions);
            MarkerOptions m=new MarkerOptions().position(ubicacionActual).title("Mi ubicacion").draggable(true);
            //m.icon(BitmapDescriptorFactory.fromResource(R.mipmap.bus));
            mMap.addMarker(m);

                //dibujar maradorcitos de camiones

            try {
                JSONObject jsnobject = new JSONObject(s);
                JSONArray jsonArray = jsnobject.getJSONArray("Camion");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    //coordList.add(new LatLng(Double.parseDouble(explrObject.getString("lat")), Double.parseDouble(explrObject.getString("lng"))));

                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(explrObject.getString("lat")), Double.parseDouble(explrObject.getString("lng")))).title(explrObject.getString("etiqueta")+" capacidad"+explrObject.getString("capacidad")).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
                }
            }
            catch(Exception io){}
    }
    }
}
