package com.example.edwin.ubicarpersona;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class dondeEsta extends AppCompatActivity {
  //  private static final String JSON_URL = "http://jimenezlepe.comuv.com/consultaubicacion.php";
//private static final String IP="10.0.5.113";
//private static final String IP="10.0.5.109";//ip server jalil
//private static final String IP="192.168.1.150";
    int resID;



    ImageView imageView;
    String nombre="";
    String mac="";
    String ubicacion="";
    String fecha="";
    String ubicacionBusca="";
    TextView t1;
    protected DataOutputStream dos;
    protected DataInputStream dis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donde_esta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        t1= (TextView) findViewById(R.id.textView);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {

        } else {
            nombre= extras.getString("nombre");
            mac= extras.getString("mac");
            ubicacion= extras.getString("ubicacion");
            fecha= extras.getString("fecha");
            ubicacionBusca= extras.getString("ubicacionBusca");

        Log.i("mensaje",nombre);
            String cad=nombre+" estaba en "+ubicacion +" en la fecha "+fecha+"\n";
            if(ubicacionBusca.equals("null") ||  ubicacionBusca.equals(ubicacion)){
                String mDrawableName = ubicacion;
                Log.i("mensaje", ubicacion);
                resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
            }
            else{
                if((ubicacion.equals("biblioteca") && ubicacionBusca.equals("comedor"))||ubicacion.equals("comedor") && ubicacionBusca.equals("biblioteca")){
                    String mDrawableName = "bc";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBusca;
                }
                else if((ubicacion.equals("biblioteca") && ubicacionBusca.equals("laboratorio"))||ubicacion.equals("laboratorio") && ubicacionBusca.equals("biblioteca")){
                    String mDrawableName = "bl";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBusca;
                }
                else if((ubicacion.equals("biblioteca") && ubicacionBusca.equals("salon"))||ubicacion.equals("salon") && ubicacionBusca.equals("biblioteca")){
                    String mDrawableName = "bs";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBusca;
                }
                else  if((ubicacion.equals("comedor") && ubicacionBusca.equals("laboratorio"))||ubicacion.equals("laboratorio") && ubicacionBusca.equals("comedor")){
                    String mDrawableName = "cl";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBusca;
                }
                else if((ubicacion.equals("comedor") && ubicacionBusca.equals("salon"))||ubicacion.equals("salon") && ubicacionBusca.equals("comedor")){
                    String mDrawableName = "cs";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBusca;
                }
                else if((ubicacion.equals("laboratorio") && ubicacionBusca.equals("salon"))||ubicacion.equals("salon") && ubicacionBusca.equals("laboratorio")){
                    String mDrawableName = "ls";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBusca;
                }
            }
            t1.setText(cad);
            imageView = (ImageView) findViewById(R.id.imagen);
            imageView.setImageResource(resID);

        }


    }




    public void procesarResultado(String jsonresult) {
        Toast.makeText(getApplicationContext(), jsonresult, Toast.LENGTH_LONG).show();
        JSONObject obj = null;
        String ubicacionBuscante="";
        try {
            obj = new JSONObject(jsonresult);
            JSONArray arr = obj.getJSONArray("result");
           String cad="", ubicacion="";
            for (int i = 0; i < arr.length(); i++) {
                JSONObject m = arr.getJSONObject(i);
                if(m.getString("ubicacion").equals("")){cad="No sabemos donde está";}
                else {
                    cad += m.getString("nombre") + " estuvo en " + m.getString("ubicacion") + "en la fecha " + m.getString("fecha");
                }//Toast.makeText(getApplicationContext(),arr.getString(i),Toast.LENGTH_LONG).show();
                ubicacion=m.getString("ubicacion");
                ubicacionBuscante=m.getString("ubicacionBuscante");
                Log.i("mensaje", m.getString("nombre"));
            }

            if(ubicacionBuscante.equals("null") ||  ubicacionBuscante.equals(ubicacion)){
                String mDrawableName = ubicacion;
                Log.i("mensaje", ubicacion);
                resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
            }
            else{
                if((ubicacion.equals("biblioteca") && ubicacionBuscante.equals("comedor"))||ubicacion.equals("comedor") && ubicacionBuscante.equals("biblioteca")){
                    String mDrawableName = "bc";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBuscante;
                }
                else if((ubicacion.equals("biblioteca") && ubicacionBuscante.equals("laboratorio"))||ubicacion.equals("laboratorio") && ubicacionBuscante.equals("biblioteca")){
                    String mDrawableName = "bl";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBuscante;
                }
                else if((ubicacion.equals("biblioteca") && ubicacionBuscante.equals("salon"))||ubicacion.equals("salon") && ubicacionBuscante.equals("biblioteca")){
                    String mDrawableName = "bs";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBuscante;
                }
                else  if((ubicacion.equals("comedor") && ubicacionBuscante.equals("laboratorio"))||ubicacion.equals("laboratorio") && ubicacionBuscante.equals("comedor")){
                    String mDrawableName = "cl";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBuscante;
                }
                else if((ubicacion.equals("comedor") && ubicacionBuscante.equals("salon"))||ubicacion.equals("salon") && ubicacionBuscante.equals("comedor")){
                    String mDrawableName = "cs";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBuscante;
                }
                else if((ubicacion.equals("laboratorio") && ubicacionBuscante.equals("salon"))||ubicacion.equals("salon") && ubicacionBuscante.equals("laboratorio")){
                    String mDrawableName = "ls";
                    Log.i("mensaje", ubicacion);
                    resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    cad+="Tu estás en "+ubicacionBuscante;
                }
            }
            t1.setText(cad);
            imageView = (ImageView) findViewById(R.id.imagen);
            imageView.setImageResource(resID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
