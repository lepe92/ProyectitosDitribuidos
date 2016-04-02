package com.example.eejl_.fblogin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
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
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Mapa extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
String token="";
    WebView v1;
    String resultado="";
    String idbus="";
    Bitmap bitmap;
    Dialog builder;
    ImageView imageView;
    ArrayList<String> idcamiones;
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
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(Mapa.this);
                builderSingle.setIcon(R.drawable.bus);
                builderSingle.setTitle("Select One Name:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        Mapa.this,
                        android.R.layout.select_dialog_singlechoice);
               // arrayAdapter.add("Hardik");
                if(!idcamiones.isEmpty()) {
                    //solamente si hay camioncitos del simulador comienza a realizar esto
                    for (int i = 0; i < idcamiones.size(); i++) {
                        arrayAdapter.add(idcamiones.get(i));
                    }

                    builderSingle.setNegativeButton(
                            "cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    builderSingle.setAdapter(
                            arrayAdapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String strName = arrayAdapter.getItem(which);

                                    idbus=strName;
                                    Log.i("mensaje", idbus);

                                    //obtenerURL();
                                    LoadImage m = new LoadImage();
                                    m.execute();

                                    /*AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                            Mapa.this);
                                    builderInner.setMessage(strName);
                                    builderInner.setTitle("Your Selected Item is");
                                    builderInner.setPositiveButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    builderInner.show();
*/

//////////////////mostrar la imagen

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


////////////////////////fin mostrar imagen
                                }
                            });
                    builderSingle.show();
                }//fin del if que contiene id de camiones

            }
        });


        ContactarSimulador myClientTask= new ContactarSimulador();
        myClientTask.execute();

        //Timer timer = new Timer();
        //cuando cambie la ubicación se debe contactar al simulador cada cinco segundos
        //TimerTask obtenCamiones = new contacta();
        //timer.scheduleAtFixedRate(obtenCamiones, 1000, 5000);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                   // mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };
        //mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }





        this.registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
        this.registerReceiver(mMessageReceiver, new IntentFilter("unique_name2"));
    }//end onCreate


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
        ProgressDialog loading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(Mapa.this, "Descargando imagen...", null, true, true);


        }
        protected Bitmap doInBackground(String... args) {
//obtener url de imagen
                    OutputStream os = null;
                    InputStream is = null;
                    HttpURLConnection conn = null;
                    BufferedReader bufferedReader = null;
                    try {
                        Log.i("mensaje", "legaste al hilo de solicitud");
                        //constants
                        //http://jimenezlepe.comuv.com/solicita.php
                        URL url = new URL("http://jimenezlepe.comuv.com/Ubus/UbusApp/consultaurl.php");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("idbus", idbus);

                        //jsonObject.put("rssi", rssi1);

                        String message = jsonObject.toString();

                        conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000 /*milliseconds*/);
                        conn.setConnectTimeout(15000 /* milliseconds */);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setFixedLengthStreamingMode(message.getBytes().length);

                        //make some HTTP header nicety
                        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                        //open
                        conn.connect();

                        //setup send

                        os = new BufferedOutputStream(conn.getOutputStream());
                        os.write(message.getBytes());
                        //clean up
                        os.flush();

                        //do somehting with response
                        is = conn.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(is));

                        resultado="";
                        //Insercion correcta
                        String json;
                        while ((json = bufferedReader.readLine()) != null) {
                            Log.i("mensaje", json);
                            resultado= json;
                        }

                        //  Toast.makeText(getApplicationContext(), is.toString(), Toast.LENGTH_LONG).show();
                        //Log.i("respuesta", is.toString());
                        //String contentAsString = readIt(is,len);
                    } catch (IOException e) {
                        Log.i("mensaje", "falla de E/S");
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("mensaje", "falla de json");
                    } finally {
                        //clean up
                        try {
                            os.close();
                            is.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        conn.disconnect();
                    }




            try {
                //URL url=new URL("http://jimenezlepe.comuv.com/Ubus/choferes/leo.jpg");
                URL url=new URL(resultado);
                bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            loading.dismiss();
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

    public void obtenerURL() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                BufferedReader bufferedReader = null;
                try {
                    Log.i("mensaje", "legaste al hilo de solicitud");
                    //constants
                    //http://jimenezlepe.comuv.com/solicita.php
                    URL url = new URL("http://jimenezlepe.comuv.com/Ubus/UbusApp/consultaurl.php");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("idbus", idbus);

                    //jsonObject.put("rssi", rssi1);

                    String message = jsonObject.toString();

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /*milliseconds*/);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(message.getBytes().length);

                    //make some HTTP header nicety
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    conn.connect();

                    //setup send

                    os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(message.getBytes());
                    //clean up
                    os.flush();

                    //do somehting with response
                    is = conn.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(is));

                    resultado="";
                    //Insercion correcta
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        Log.i("mensaje", json);
                        resultado= json;
                    }

                    //  Toast.makeText(getApplicationContext(), is.toString(), Toast.LENGTH_LONG).show();
                    //Log.i("respuesta", is.toString());
                    //String contentAsString = readIt(is,len);
                } catch (IOException e) {
                    Log.i("mensaje", "falla de E/S");
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("mensaje", "falla de json");
                } finally {
                    //clean up
                    try {
                        os.close();
                        is.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    conn.disconnect();
                }
            }
        }).start();
    }



    public class ContactarSimulador extends AsyncTask<String, Void, String> {
        //String dstAddress="10.0.5.121";
        //String dstAddress="10.0.5.241";
        String dstAddress="10.0.5.115";
        //String dstAddress="192.168.1.70";
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
dibujarCamiones(s);
    }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        this.registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();

        this.unregisterReceiver(mMessageReceiver);
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;

        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("mensaje", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            if(message.contains("token@")){
                String []temp= message.split("@");
                token=temp[1];
                Log.i("mensaje Token ",token);
            }
            else{Log.i("mensaje GCM ",message);
            dibujarCamiones(message);}
            //do other stuff here
        }
    };

    public void dibujarCamiones(String s){
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

        idcamiones= new ArrayList<>();

        try {
            JSONObject jsnobject = new JSONObject(s);
            JSONArray jsonArray = jsnobject.getJSONArray("Camion");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                //coordList.add(new LatLng(Double.parseDouble(explrObject.getString("lat")), Double.parseDouble(explrObject.getString("lng"))));
                idcamiones.add(explrObject.getString("idcamion"));
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(explrObject.getString("lat")), Double.parseDouble(explrObject.getString("lng")))).title(explrObject.getString("etiqueta")+" capacidad"+explrObject.getString("capacidad")).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
            }
        }

//aqui meterle datos al arraylist
        catch(Exception io){}
    }
}

