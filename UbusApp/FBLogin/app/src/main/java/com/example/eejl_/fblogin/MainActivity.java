package com.example.eejl_.fblogin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;

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

public class MainActivity extends AppCompatActivity {
    TextView yacuenta;
    String mail = "", passw = "";
    AppCompatButton ingresar;
    EditText email, pass;
    SessionManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        yacuenta = (TextView) findViewById(R.id.link_signup);
        manager = new SessionManager();
        yacuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(MainActivity.this, SignUp.class);
                startActivity(m);
            }
        });

        email = (EditText) findViewById(R.id.input_email);
        pass = (EditText) findViewById(R.id.input_password);

        ingresar = (AppCompatButton) findViewById(R.id.btn_login);
//edittext input_email input_pass
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pass.getText().equals("") & !email.getText().equals("") & isValidEmail(email.getText())) {
                    mail = email.getText().toString();
                    passw = pass.getText().toString();
                    actualizar();
                } else {
                    Toast.makeText(getApplicationContext(), "No deje ningún campo vacío\nIngrese una cuenta de correo válida", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void actualizar() {
        class actualizarNombre extends AsyncTask<String, Void, String> {
            Boolean muestra = false;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(MainActivity.this, "Please Wait...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = "http://jimenezlepe.comuv.com/Ubus/UbusApp/login.php";
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                BufferedReader bufferedReader = null;
                try {
                    //constants
                    //http://jimenezlepe.comuv.com/solicita.php
                    URL url = new URL(uri);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("correo", mail);
                    jsonObject.put("pass", passw);


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
                        if (json.equals("logueo")) {
                            manager.setPreferences(MainActivity.this, "status", "1");
                            manager.setPreferences(MainActivity.this, "correo",mail);
                            Intent m = new Intent(MainActivity.this, RutasMenu.class);
                            startActivity(m);
                        } else {
                            muestra = true;
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
                //         loading.dismiss();
//                Log.i("mensaje", s);
                if (muestra) {
                    Toast.makeText(getApplicationContext(), "Correo o contraseña incorrecta", Toast.LENGTH_LONG).show();
                }
            }
        }
        actualizarNombre gj = new actualizarNombre();
        gj.execute();

        //Toast.makeText(getApplicationContext(),);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Profile p = Profile.getCurrentProfile();
        if (p != null) {
            Log.d("bienvenido", p.getName());
            Intent ventanitaRuta = new Intent(this, RutasMenu.class);
            startActivity(ventanitaRuta);
        }

        String status=manager.getPreferences(MainActivity.this, "status");
        Log.d("status",status);
        if (status.equals("1")){
            Intent i=new Intent(MainActivity.this,RutasMenu.class);
            startActivity(i);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Profile p = Profile.getCurrentProfile();
        if (p != null) {
            Log.d("bienvenido", p.getName());
            Intent ventanitaRuta = new Intent(this, RutasMenu.class);
            startActivity(ventanitaRuta);
        }
        String status=manager.getPreferences(MainActivity.this, "status");
        Log.d("status",status);
        if (status.equals("1")){
            Intent i=new Intent(MainActivity.this,RutasMenu.class);
            startActivity(i);
        }
    }
}
