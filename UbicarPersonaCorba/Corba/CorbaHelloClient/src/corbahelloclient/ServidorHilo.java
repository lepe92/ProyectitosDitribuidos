package corbahelloclient;


import HelloApp.Hello;
import HelloApp.HelloHelper;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

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
        String accion = "";
        
        try {
            accion = dis.readUTF();
            
            System.out.println("Recibi "+accion);
            
           String[] args = new String[4];
            args[0]="-ORBInitialHost";
            //args[1]="10.0.5.121";
            args[1]="10.0.5.196";
            args[2]="-ORBInitialPort";
            args[3]="1050";
            
            ORB orb = ORB.init(args, null);
            
            //obtiene el root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            
            //Use namingContextExt instead of namingcontext. this is part of the interoperable naming service
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef); 
             
            //Resolve the object reference in naming
            String name = "Hello"; 
            Hello helloObj = HelloHelper.narrow(ncRef.resolve_str(name));
            
            System.out.println("Welcome to system");
            
            String r = helloObj.sayHello(accion);
            
            System.out.println("Respuesta server: "+ r);
            System.out.println("----------------------------------");
            
            dos.writeUTF(r);
                
            
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception e){
            System.err.println("Error: "+ e);
            e.printStackTrace(System.out);
        }
        
        desconnectar();
        
    }
}
