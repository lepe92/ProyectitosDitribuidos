package javarmi;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;  

public class AddServer 
{
    public static void main(String args[])
    {
        try
        {
            AddServerImpl addServerImpl = new AddServerImpl();
            
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("AddServer", addServerImpl);
            
            System.err.println("Server ready");
        }catch(Exception e){System.out.println("Exception: " + e.getMessage());}
    }
    
}
