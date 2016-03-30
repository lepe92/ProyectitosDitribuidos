
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
        double [] latitud=new double[5];
        double [] longitud=new double[5];
        latitud[0]=20.73236;
        latitud[1]=20.73237;
        latitud[2]=20.73238;
        latitud[3]=20.73239;
        latitud[4]=20.73240;
        longitud[0]=-103.35151;
        longitud[1]=-103.35152;
        longitud[2]=-103.35153;
        longitud[3]=-103.35154;
        longitud[4]=-103.35155;
        String accion = "";
        
        try {
            accion = dis.readUTF();
            System.out.println(accion);
               Random n= new Random();
            String respuesta="{\"Camion\":[{\"capacidad\":\"34/40\",\"idcamion\":\"1\",\"etiqueta\":\"bus1\",\"lat\":"+latitud[n.nextInt(5)]+",\"lng\":"+longitud[n.nextInt(5)]+"},{\"capacidad\":\"27/40\",\"idcamion\":\"2\",\"etiqueta\":\"bus2\",\"lat\":"+latitud[n.nextInt(5)]+",\"lng\":"+longitud[n.nextInt(5)]+"}]}";
            //enviar cada cierto tiempo posiciones aleatorias
            dos.writeUTF(respuesta);
            
          
            
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        desconnectar();
        
    }
}
