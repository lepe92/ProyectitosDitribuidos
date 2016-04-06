
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author edwin
 */
public class ServidorHilo extends Thread {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;

    public ServidorHilo(Socket socket) {
        this.socket = socket;

        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desconnectar() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        double[] latitud = new double[5];
        double[] longitud = new double[5];
        latitud[0] = 20.73236;
        latitud[1] = 20.73237;
        latitud[2] = 20.73238;
        latitud[3] = 20.73239;
        latitud[4] = 20.73240;
        longitud[0] = -103.35151;
        longitud[1] = -103.35152;
        longitud[2] = -103.35153;
        longitud[3] = -103.35154;
        longitud[4] = -103.35155;
        String accion = "";

        try {
            accion = dis.readUTF();
            System.out.println(accion);

            if (accion.contains("desuscripcion@")) {
                String temp[] = accion.split("@");
                int m = ruta.tokens.indexOf(temp[2]);
                ruta.suscripcion.remove(m);
                ruta.tokens.remove(m);
                if (temp[1].contains("142")) {
                    for (int i = 0; i < ruta.ruta142.size(); i++) {
                        if (ruta.ruta142.get(i).token.equals(temp[2])) {
                            ruta.ruta142.remove(i);
                            break;
                        }
                    }

                } else if (temp[1].contains("300")) {
                    for (int i = 0; i < ruta.ruta300.size(); i++) {
                        if (ruta.ruta300.get(i).token.equals(temp[2])) {
                            ruta.ruta300.remove(i);
                            break;
                        }
                    }
                } else if (temp[1].contains("602")) {
                    for (int i = 0; i < ruta.ruta602.size(); i++) {
                        if (ruta.ruta602.get(i).token.equals(temp[2])) {
                            ruta.ruta602.remove(i);
                            break;
                        }
                    }

                } else if (temp[1].contains("500")) {
                    for (int i = 0; i < ruta.ruta500.size(); i++) {
                        if (ruta.ruta500.get(i).token.equals(temp[2])) {
                            ruta.ruta500.remove(i);
                            break;
                        }
                    }

                } else if (temp[1].contains("396")) {
                    for (int i = 0; i < ruta.ruta396.size(); i++) {
                        if (ruta.ruta396.get(i).token.equals(temp[2])) {
                            ruta.ruta396.remove(i);
                            break;
                        }
                    }
                }
                System.out.println("142 " + ruta.ruta142.size());
                System.out.println("300 " + ruta.ruta300.size());
                System.out.println("602 " + ruta.ruta602.size());
                System.out.println("500 " + ruta.ruta500.size());
                System.out.println("396 " + ruta.ruta396.size());

//300, 602, 500, 396,142
            } else {
                parsearJSON(accion);
            }

            Random n = new Random();
            String respuesta = "{\"Camion\":[{\"capacidad\":\"34/40\",\"idcamion\":\"1\",\"nombre\":\"bus1\",\"lat\":" + latitud[n.nextInt(5)] + ",\"lng\":" + longitud[n.nextInt(5)] + "},{\"capacidad\":\"27/40\",\"idcamion\":\"2\",\"nombre\":\"bus2\",\"lat\":" + latitud[n.nextInt(5)] + ",\"lng\":" + longitud[n.nextInt(5)] + "}]}";
            //enviar cada cierto tiempo posiciones aleatorias
            dos.writeUTF(respuesta);

        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }

        desconnectar();

    }

    private void parsearJSON(String s) {
        s = "{\"Usuario\":[" + s + "]}";
        System.out.println(s);
        try {
            JSONObject jsnobject = new JSONObject(s);
            JSONArray jsonArray = jsnobject.getJSONArray("Usuario");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                Suscriptor nuevo = new Suscriptor(explrObject.getString("Ruta"), explrObject.getString("lng"), explrObject.getString("lat"), explrObject.getString("Token"));
                ruta.addSuscriptor(nuevo);
//300, 602, 500, 396,142
            }
        } catch (JSONException e) {
        }
    }
}
