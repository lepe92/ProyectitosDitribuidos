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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class dondeEsta extends AppCompatActivity {

    //private static final String JSON_URL = "http://jimenezlepe.comuv.com/consultaubicacion.php";
    //private static final String JSON_URL2 = "http://distribuidos.net23.net/consultaubicacion.php";
    String JSON_URL="";
    String nombre="";
    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donde_esta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        t1= (TextView) findViewById(R.id.textView);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            nombre= null;
            JSON_URL=null;
        } else {
            nombre= extras.getString("nombre");
            JSON_URL=extras.getString("url");
        Log.i("mensaje",nombre);
        }

        getJSON(JSON_URL);
    }

    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(dondeEsta.this, "Please Wait...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                OutputStream os = null;
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("nombre", nombre);
                    String message = jsonObject.toString();

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout( 7000 /*milliseconds*/ );
                    con.setConnectTimeout( 7500 /* milliseconds */ );
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setFixedLengthStreamingMode(message.getBytes().length);

                    //make some HTTP header nicety
                    con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    con.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    con.connect();

                    //setup send

                    os = new BufferedOutputStream(con.getOutputStream());
                    os.write(message.getBytes());
                    //clean up
                    os.flush();

                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    Log.i("mensaje","el sitio no responde");
                   return "ERROR";
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.i("mensaje", s);

                if(!s.equals("ERROR")){
               procesarResultado(s);}
                else{Toast.makeText(getApplicationContext(),"Tenemos problemas técnicos, vuelva en un instante por favor.", Toast.LENGTH_LONG).show();}
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);

        //Toast.makeText(getApplicationContext(),);
    }

/*
    private void getJSON2(String url) {
        class GetJSON2 extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(dondeEsta.this, "Please Wait...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                OutputStream os = null;
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("nombre", nombre);
                    String message = jsonObject.toString();

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout( 7000  );
                    con.setConnectTimeout( 7500  );
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setFixedLengthStreamingMode(message.getBytes().length);

                    //make some HTTP header nicety
                    con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    con.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    con.connect();

                    //setup send

                    os = new BufferedOutputStream(con.getOutputStream());
                    os.write(message.getBytes());
                    //clean up
                    os.flush();

                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    Log.i("mensaje","el sitio no responde");
                    return "ERROR";
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.i("mensaje", s);

                if(!s.equals("ERROR")){
                    procesarResultado(s);}
                else{Toast.makeText(getApplicationContext(),"Ningun servidor está disponible. Inténtelo más tarde.",Toast.LENGTH_LONG).show();}
            }
        }
        GetJSON2 gj = new GetJSON2();
        gj.execute(url);

        //Toast.makeText(getApplicationContext(),);
    }
*/
    public void procesarResultado(String jsonresult) {
        Toast.makeText(getApplicationContext(), jsonresult, Toast.LENGTH_LONG).show();
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonresult);
            JSONArray arr = obj.getJSONArray("result");
           String cad="";
            for (int i = 0; i < arr.length(); i++) {
                JSONObject m = arr.getJSONObject(i);
                if(m.getString("ubicacion").equals("null")){cad="No sabemos donde está";}
                else{
                cad+= m.getString("nombre")+" estuvo en "+m.getString("ubicacion") +"en la fecha "+m.getString("fecha");}
                //Toast.makeText(getApplicationContext(),arr.getString(i),Toast.LENGTH_LONG).show();
                Log.i("mensaje", m.getString("nombre"));
            }
            t1.setText(cad);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
