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
import android.os.Handler;
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
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ListView ls;
    final Context context = this;
    ArrayAdapter<String> itemsAdapter;
    ArrayList<Usuario> usuarios;
    BluetoothAdapter mB;
    ArrayList<Integer> btList= new ArrayList<>();
    ArrayList<Ubicacion> ubicaciones;
    private static final int REQUEST_ENABLE_BT = 1;
    String macpropia, nombred, nombre, macajena;
    int rssi1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombre="prueba";

        ubicaciones= new ArrayList<Ubicacion>();

      /*  ubicaciones.add(new Ubicacion("30:D6:C9:10:76:66","biblioteca"));
        ubicaciones.add(new Ubicacion("F8:0C:F3:A9:CC:E9","comedor"));
        ubicaciones.add(new Ubicacion("30:76:6F:47:B7:BE","laboratorio"));
        ubicaciones.add(new Ubicacion("2C:8A:72:18:F0:49","salon"));
*/
        ubicaciones.add(new Ubicacion("D2:B8:E6:6D:58:F1", "biblioteca"));
        ubicaciones.add(new Ubicacion("DB:1B:C8:B8:0D:5C", "comedor"));
        ubicaciones.add(new Ubicacion("54:27:1E:FD:29:27","laboratorio"));
        ubicaciones.add(new Ubicacion("D6:4A:38:3E:98:DC","salon"));
        usuarios= new ArrayList<Usuario>();
        ls = (ListView) findViewById(R.id.listView);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this, dondeEsta.class);
                String strName = ls.getItemAtPosition(position).toString();
                i.putExtra("nombre", strName);
                i.putExtra("mac", macpropia);
                startActivity(i);


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


      //  HttpAsyncTask m= new HttpAsyncTask();
      //  m.execute();
        setRepeatingAsyncTask();


    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String received = "";


            try {


    MulticastSocket socket = new MulticastSocket(4327);
    InetAddress group = InetAddress.getByName("232.2.2.2");
    socket.joinGroup(group);

    DatagramPacket packet;

    byte[] buf = new byte[256];
    packet = new DatagramPacket(buf, buf.length);
    socket.setBroadcast(true);
    socket.receive(packet);

    received = new String(packet.getData());
    String temp[] = received.split("@");
    Log.i("mensaje", "Recibi: " + temp[0]);

    received = temp[0];
    socket.leaveGroup(group);
    socket.close();



            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return received;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.equals("")){
              procesar(s);
            }
        }
    }

    private void setRepeatingAsyncTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            HttpAsyncTask performBackgroundTask = new HttpAsyncTask();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000); //execute in every 50000 ms
    }

    public void procesar(String s){
        String nombre="", mac="", ubicacion="", fecha="";
        JSONObject obj = null;
        try {
            obj = new JSONObject(s);
            nombre=obj.getString("nombre");
            mac=obj.getString("mac");
            ubicacion=obj.getString("ubicacion");
            fecha=obj.getString("fecha");

            Log.i("mensaje", obj.getString("nombre"));
            Log.i("mensaje", obj.getString("mac"));
            Log.i("mensaje", obj.getString("ubicacion"));
            Log.i("mensaje", obj.getString("fecha"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        int contador=0;
        for(int i=0; i<usuarios.size();i++){
            if(usuarios.get(i).mac.equals(mac)){
                usuarios.get(i).setFecha(fecha);
                usuarios.get(i).setNombre(nombre);
                usuarios.get(i).setUbicacion(ubicacion);
                contador++;
            }
        }
        if(contador==0){
            usuarios.add(new Usuario(nombre,mac,ubicacion,fecha));
        }
        ArrayList<String> temp= new ArrayList<>();
        for(int i=0; i<usuarios.size();i++){
            temp.add(usuarios.get(i).nombre);
            Log.i("mensaje","aladiendo usuarios");
        }
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, temp);
        ls.setAdapter(itemsAdapter);
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
              Log.i("mensaje", itemsAdapter.getCount() + "!" + btList.size());
                macajena="";

                ArrayList<String> detectados= new ArrayList<String>();

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
                        detectados.add(temp[0]);
                        itemsAdapter.remove(ins);
                        btList.remove(index);
                    }
                }
                itemsAdapter.clear();
                btList.clear();

                for(int i=0; i<detectados.size();i++){
                    for(int j=0; j<ubicaciones.size();j++){
                        if(ubicaciones.get(j).mac.equals(detectados.get(i))){
                            //si es igual la mac a una ubicacion decir que estoy en tal lugar
                            Log.i("mensaje","Estoy en "+ubicaciones.get(j).lugar);
                            try {
                                new MulticastServerThread(nombre,macpropia,ubicaciones.get(j).lugar,"mayo").start();
                            }catch(IOException exio) {
                                Log.i("mensaje","error en el multicast");
                            }
                            break;
                        }
                    }
                }
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
