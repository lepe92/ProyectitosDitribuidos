package com.example.edwin.ubicarpersona;

/**
 * Created by ilsejazmingomezperez on 17/04/16.
 */
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastServerThread extends Thread {
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    private long FIVE_SECONDS = 5000;

    private String nombre="";
    private String mac="";
    private String fecha="";
    private String ubicacion="";
    public MulticastServerThread(String nombre,String mac,String ubicacion,String fecha) throws IOException {
this.nombre=nombre;
        this.mac=mac;
        this.fecha=fecha;
        this.ubicacion=ubicacion;
        socket = new DatagramSocket(4326);
    }

    public void run() {

            try {
                Log.i("mensaje","enviando");
                byte[] buf = new byte[256];

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("nombre", nombre);
                    jsonObject.put("mac", mac);
                    jsonObject.put("ubicacion", ubicacion);
                    jsonObject.put("fecha", fecha);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String dString =  jsonObject.toString()+"@";//new Date().toString();

                buf = dString.getBytes();

                // send it
                InetAddress group = InetAddress.getByName("232.2.2.2");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4327);
                socket.send(packet);

                // sleep for a while
                try {
                    sleep((long)(Math.random() * FIVE_SECONDS));
                } catch (InterruptedException e) { }
            } catch (IOException e) {
                e.printStackTrace();
            }

        socket.close();
    }
}

