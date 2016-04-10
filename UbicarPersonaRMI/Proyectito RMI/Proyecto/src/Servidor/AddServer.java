package Servidor;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;  

public class AddServer 
{
    public static void main(String args[])
    {
        try
        {

//para cuando no se puede conectar
           System.setProperty("java.rmi.server.hostname","10.0.5.113");

            AddServerImpl addServerImpl = new AddServerImpl();
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("AddServer", addServerImpl);
            
            System.err.println("Server ready");
        }catch(Exception e){System.out.println("Exception: " + e.getMessage());}
    }
    
}
