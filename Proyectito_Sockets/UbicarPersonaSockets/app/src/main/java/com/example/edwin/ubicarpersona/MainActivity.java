package com.example.edwin.ubicarpersona;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String JSON_URL = "http://jimenezlepe.comuv.com/solicitajson.php";
    private static final String JSON_URL2 = "http://www.jimenezlepe.comuv.com/insertarnombre.php";
    private static final String IP="10.0.5.113";

    String jsonresult;
    ListView ls;
    final Context context = this;
    ArrayAdapter<String> itemsAdapter;
    BluetoothAdapter mB;
    ArrayList<Integer> btList= new ArrayList<>();
    private static final int REQUEST_ENABLE_BT = 1;
    String macpropia, macajena, nombred, nombre;
    int rssi1;

    protected DataOutputStream dos;
    protected DataInputStream dis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ls = (ListView) findViewById(R.id.listView);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this, dondeEsta.class);
                String strName = ls.getItemAtPosition(position).toString();
                i.putExtra("nombre", strName);
                startActivity(i);


            }
        });


Button b= (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJSON(JSON_URL);
            }
        });


        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mB = BluetoothAdapter.getDefaultAdapter();
        if (mB == null) {
            Toast.makeText(getApplicationContext(), "No tiene bluetooth", Toast.LENGTH_LONG).show();
        } else {
            macpropia = mB.getAddress();
            nombred=mB.getName();
            Toast.makeText(getApplicationContext(), "Si tiene bluetooth\nNombre: " + mB.getName() + "\nDireccion: " + mB.getAddress(), Toast.LENGTH_LONG).show();
            if (mB.isEnabled() == true)
                Toast.makeText(getApplicationContext(), "Bluetooth activado", Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(getApplicationContext(), "Bluetooth desactivado, pero se activara en breve", Toast.LENGTH_LONG).show();

                if (mB.enable() == true)
                    Toast.makeText(getApplicationContext(), "Bluetooth activado", Toast.LENGTH_LONG).show();
            }

        }

        //antes de obtener el listado, consultar la mac en el sistema y si no existe insertarla


        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //http://www.jimenezlepe.comuv.com/insertarnombre.php
                                nombre= userInput.getText().toString();
                                actualizar(JSON_URL2);

                            }
                        });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();



        //getJSON(JSON_URL);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(ActionFoundReceiver, filter); // Don't forget to unregister during onDestroy


        Timer tim;
        tim = new Timer();

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                CheckBTState();
            }
        };
        // tim.schedule(tarea,5, 10000);
        tim.schedule(tarea, 50, 10000);
    }

    public class SolicitarActualizar extends AsyncTask<Void, Void, Void> {

        // String dstAddress="192.168.1.78";
        String dstAddress=IP;
        int dstPort=5000;
        String response = "";

        /*MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }*/

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                Log.i("mensaje", "conectado");
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("nombre", nombre);
                    jsonObject.put("nombred", nombred);
                    jsonObject.put("mac", macpropia);
                    jsonObject.put("opcionactualizar", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //jsonObject.put("rssi", rssi1);

                String message = jsonObject.toString();

                dos.writeUTF(message);
                response = dis.readUTF();
                Log.i("mensaje", response);



                // Your database code here

/*                socket = new Socket(dstAddress, dstPort);

                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

    /*
     * notice:
     * inputStream.read() will block if no data return
     *//*
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                } */

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
        protected void onPostExecute(Void result) {
            getJSON(JSON_URL);
            //textResponse.setText(response);
            //super.onPostExecute(result);
        }

    }


    public class SolicitarJSON extends AsyncTask<String, Void, String> {
        ProgressDialog loading;

        // String dstAddress="192.168.1.78";
        String dstAddress=IP;
        int dstPort=5000;
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this, "Please Wait...", null, true, true);
        }


        @Override
        protected String doInBackground(String... arg0) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                Log.i("mensaje", "conectado");
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());


                dos.writeUTF("solicitaJSON");

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
            //Log.i("mensaje", s);
            jsonresult = s;
            procesarResultado();

        }

    }

    private void actualizar(String url) {
        SolicitarActualizar myClientTask= new SolicitarActualizar();
        myClientTask.execute();
        /*
        class actualizarNombre extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(MainActivity.this, "Please Wait...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
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
                    jsonObject.put("nombred", nombred);
                    jsonObject.put("mac", macpropia);
                    //jsonObject.put("rssi", rssi1);

                    String message = jsonObject.toString();

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 );
                    conn.setConnectTimeout(15000 );
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
              //  super.onPostExecute(s);
       //         loading.dismiss();
//                Log.i("mensaje", s);

                getJSON(JSON_URL);
            }
        }
        actualizarNombre gj = new actualizarNombre();
        gj.execute(url);

        //Toast.makeText(getApplicationContext(),);
    */
                }

    private void getJSON(String url) {
        SolicitarJSON myClientTask= new SolicitarJSON();
        myClientTask.execute();
       /* class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
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
                jsonresult = s;
                procesarResultado();
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
*/
        //Toast.makeText(getApplicationContext(),);
    }


    public class insertar extends AsyncTask<String, Void, String> {
        ProgressDialog loading;

        // String dstAddress="192.168.1.78";
        String dstAddress=IP;
        int dstPort=5000;
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this, "Please Wait...", null, true, true);
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
                    jsonObject.put("macd", macajena);
                    jsonObject.put("macp", macpropia);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String message = jsonObject.toString();

                dos.writeUTF(message);

                response = dis.readUTF();

                if(!response.equals("nada")){
                Intent i = new Intent(MainActivity.this, Opciones.class);
                i.putExtra("opciones",response);
                startActivity(i);
                }

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
            //Log.i("mensaje", s);
            //jsonresult = s;
            //procesarResultado();

        }

    }

    public void procesarResultado() {
        Toast.makeText(getApplicationContext(), jsonresult, Toast.LENGTH_LONG).show();
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonresult);
            JSONArray arr = obj.getJSONArray("result");
            String[] temp = new String[arr.length()];
            for (int i = 0; i < arr.length(); i++) {
                JSONObject m = arr.getJSONObject(i);
                temp[i] = m.getString("nombre");
                //Toast.makeText(getApplicationContext(),arr.getString(i),Toast.LENGTH_LONG).show();
                Log.i("mensaje", m.getString("nombre"));
            }

            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, temp);
            ls.setAdapter(itemsAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void insertarDatos() {
        insertar myClientTask= new insertar();
        myClientTask.execute();

        /* elementos a enviar al servidor
        *  JSONObject jsonObject = new JSONObject();
                    jsonObject.put("macd", macajena);
                    jsonObject.put("macp", macpropia);
                    String message = jsonObject.toString();
        * */
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL("http://jimenezlepe.comuv.com/solicita.php");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("macd", macajena);
                    jsonObject.put("macp", macpropia);

                    String message = jsonObject.toString();

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 );
                    conn.setConnectTimeout(15000 );
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(message.getBytes().length);


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

                    Log.i("mensaje2",macajena);
                    //Insercion correcta
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        Log.i("mensaje",json);
                        Intent i = new Intent(MainActivity.this, Opciones.class);
                        i.putExtra("opciones",json);
                        startActivity(i);
                    }


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
            }
        }).start();*/
    }

    /* This routine is called when an activity completes.*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {

            CheckBTState();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mB != null) {
            mB.cancelDiscovery();
        }
        unregisterReceiver(ActionFoundReceiver);
    }

    private void CheckBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // If it isn't request to turn it on
        // List paired devices
        // Emulator doesn't support Bluetooth and will return null
        if (mB == null) {
            //  out.append("\nBluetooth NOT supported. Aborting.");
            return;
        } else {
            if (mB.isEnabled()) {
                //   out.append("\nBluetooth is enabled...");

                // Starting the device discovery
                mB.startDiscovery();


            } else {
                Intent enableBtIntent = new Intent(mB.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }
// sirve para encontrar dispositivos cercanos

    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            itemsAdapter.clear();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) { //CADA QUE ENCUENTRA UN DISPOSITIVO
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                rssi1 = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                Log.i("Detectado", device.getName() + "    " + device.getAddress());
                itemsAdapter.add(device.getAddress()+"@"+device.getName() );
                btList.add(rssi1);
               }
            //   if(){}
//hasta que tengamos el mayor insertamos

            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
              //  ls.setAdapter(itemsAdapter);
                Log.i("mensaje", itemsAdapter.getCount() + "!" + btList.size());


                //hacer comparacion pero intentar la insercion de mayor a menor distancia hasta que alguna se inserte
                macajena="";


                if(btList.size()!=0) {
                    while(btList.size()!=0){
                        int index=0;
                        int mayor=-1000;
                        for (int i = 0; i < btList.size(); i++) {
                            if (btList.get(i) > mayor) {
                                mayor = btList.get(i);
                                index = i;
                            }
                        }

                        String ins = itemsAdapter.getItem(index);
                        Log.i("mensaje", "elemento mayor" + ins + "@" + mayor);
                        String temp[] = ins.split("@");
                        macajena = macajena+ temp[0] + "@";
                        Log.i("mensaje", macajena);

                        itemsAdapter.remove(ins);
                        btList.remove(index);
                    }

                    insertarDatos();
                }
                //si despues de insertar, correcto es falso se debe volver a intentar

                itemsAdapter.clear();
                btList.clear();
                //Log.i("mensaje",itemsAdapter.getCount()+"!"+btList.size());
                //insertarDatos();
            }
        }
    };

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
}
