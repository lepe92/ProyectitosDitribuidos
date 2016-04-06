package com.example.eejl_.fblogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Handler;

public class SignUp extends AppCompatActivity {
    SessionManager manager;
    EditText enombre, ecorreo, epass;
AppCompatButton registrar;
    String nombre, correo, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        manager = new SessionManager();
        TextView t1= (TextView)findViewById(R.id.link_login);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(SignUp.this, MainActivity.class);
                //eliminar el stack de activities
                m.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(m);
            }
        });

        nombre=""; correo=""; pass="";
        enombre= (EditText)findViewById(R.id.input_name);
        ecorreo= (EditText)findViewById(R.id.input_email);
        epass= (EditText)findViewById(R.id.input_password);
        registrar= (AppCompatButton)findViewById(R.id.btn_signup);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!enombre.getText().equals("") & !ecorreo.getText().equals("") & isValidEmail(ecorreo.getText()) & !registrar.getText().equals("")){
                   nombre= enombre.getText().toString();
                    correo= ecorreo.getText().toString();
                    pass=epass.getText().toString();
                    actualizar();
                }else{
                    Toast.makeText(getApplicationContext(),"No deje ningún campo vacío\nIngrese una cuenta de correo válida", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void actualizar() {
        class actualizarNombre extends AsyncTask<String, Void, String> {
Boolean muestra= false;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(MainActivity.this, "Please Wait...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = "http://jimenezlepe.comuv.com/Ubus/UbusApp/registrar.php";
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                BufferedReader bufferedReader = null;
                try {
                    //constants
                    //http://jimenezlepe.comuv.com/solicita.php
                    URL url = new URL(uri);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("nombre", nombre);
                    jsonObject.put("correo", correo);
                    jsonObject.put("pass", pass);


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

//                    Log.i("mensaje2", macajena);
                    //Insercion correcta
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        Log.i("mensaje", json);
                        if(json.equals("insertado")){
                            manager.setPreferences(SignUp.this, "status", "1");

                            Intent m = new Intent(SignUp.this, RutasMenu.class);
                            startActivity(m);
                        }
                        else{
muestra= true;
                            //Toast.makeText(getApplicationContext(),"Esa cuenta ya se encuentra registrada\nO hubo un error en el servidor", Toast.LENGTH_LONG).show();
                        }
                    }

                    //  Toast.makeText(getApplicationContext(), is.toString(), Toast.LENGTH_LONG).show();
                    //Log.i("respuesta", is.toString());
                    //String contentAsString = readIt(is,len);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
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
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                  super.onPostExecute(s);
                if(muestra){
                    Toast.makeText(getApplicationContext(),"Esa cuenta ya se encuentra registrada\nO hubo un error en el servidor", Toast.LENGTH_LONG).show();
                }
                //         loading.dismiss();
//                Log.i("mensaje", s);

            }
        }
        actualizarNombre gj = new actualizarNombre();
        gj.execute();

        //Toast.makeText(getApplicationContext(),);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String status=manager.getPreferences(SignUp.this, "status");
        Log.d("status", status);
        if (status.equals("1")){
            Intent i=new Intent(SignUp.this,RutasMenu.class);
            startActivity(i);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String status=manager.getPreferences(SignUp.this, "status");
        Log.d("status", status);
        if (status.equals("1")){
            Intent i=new Intent(SignUp.this,RutasMenu.class);
            startActivity(i);
        }
    }
}
