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
    private static final String JSON_URL = "http://jimenezlepe.comuv.com/consultaubicacion.php";
//private static final String IP="10.0.5.113";
private static final String IP="10.0.5.108";
    String nombre="";
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
            nombre= null;
        } else {
            nombre= extras.getString("nombre");
        Log.i("mensaje",nombre);
        }

        getJSON(JSON_URL);
    }

    public class SolicitarJSON extends AsyncTask<String, Void, String> {
        ProgressDialog loading;

        // String dstAddress="192.168.1.78";
        //String dstAddress="10.0.5.169";
        String dstAddress=IP;
        int dstPort=5000;
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(dondeEsta.this, "Please Wait...", null, true, true);
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
                    jsonObject.put("nombre", nombre);
                    jsonObject.put("solicitapersona", "solicitapersona");
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
            super.onPostExecute(s);
            loading.dismiss();
            procesarResultado(s);
            //Log.i("mensaje", s);
            //jsonresult = s;
           // procesarResultado();

        }

    }


    private void getJSON(String url) {
        SolicitarJSON myClientTask= new SolicitarJSON();
        myClientTask.execute();
        /*
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
                    con.setReadTimeout( 10000 );
                    con.setConnectTimeout( 15000 );
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
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.i("mensaje", s);

               procesarResultado(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
*/
        //Toast.makeText(getApplicationContext(),);
    }


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
                else {
                    cad += m.getString("nombre") + " estuvo en " + m.getString("ubicacion") + "en la fecha " + m.getString("fecha");
                }//Toast.makeText(getApplicationContext(),arr.getString(i),Toast.LENGTH_LONG).show();
                Log.i("mensaje", m.getString("nombre"));
            }
            t1.setText(cad);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
