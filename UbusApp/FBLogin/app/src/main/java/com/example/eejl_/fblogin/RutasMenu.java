package com.example.eejl_.fblogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RutasMenu extends AppCompatActivity {
    private static final String URL = "http://jimenezlepe.comuv.com/Ubus/UbusApp/ConsultaRutas.php";
    ListView ls;
    String resultado[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayAdapter<String> itemsAdapter;
        Button b1= (Button) findViewById(R.id.button);

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent i = new Intent(RutasMenu.this, MainActivity.class);
// set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                //finish();
            }
        });
        ls = (ListView) findViewById(R.id.listView);
        getJSON(URL);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//mandar resultado en la posicion position
                if(position==resultado.length){
                    //mandar al activity de mapa2
                    Intent m = new Intent(RutasMenu.this, MapsActivity2.class);
                    m.putExtra("coordenada",resultado);
                    startActivity(m);
                }
                else{
                Intent m = new Intent(RutasMenu.this, Mapa.class);
                m.putExtra("coordenada",resultado[position]);
                startActivity(m);}
            }
        });
    }

    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RutasMenu.this, "Consiguiendo rutas...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    java.net.URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
                ParseMessage(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);


    }
    

    public void ParseMessage(String s){

        String mensaje[]= s.split("@");
        Log.i("mensaje",mensaje.length+"");
        String temp[]= new String[mensaje.length];
        resultado= new String[mensaje.length-1];//resultado guarad el nombre de ruta y coordenadas
        for(int i=0; i<mensaje.length-1;i++){
            resultado[i]=mensaje[i];
            String mesa[]=mensaje[i].split("/");
            Log.i("mensaje",mesa[0]);
            temp[i]=mesa[0];
        }
        temp[temp.length-1]="Ver todas las rutas";
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, temp);
        ls.setAdapter(itemsAdapter);
    }
}
