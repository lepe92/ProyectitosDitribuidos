package corbahelloclient;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eejl_
 */
public class Server {
    ServerSocket ss;
    
    //ArrayList<Socket> clientes= new ArrayList<>();
    public Server(int puerto){
        System.out.print("Inicializando servidor... ");
        try {
            ss = new ServerSocket(puerto);
            System.out.println("\t[OK]");
            
            while (true) {
                Socket socket;
                socket = ss.accept();
                
                System.out.println("Nueva conexión entrante: "+socket);
                ((ServidorHilo) new ServidorHilo(socket)).start();
                
                }
            }
         catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String args[]){
    Server s= new Server(5000);
    }
}
