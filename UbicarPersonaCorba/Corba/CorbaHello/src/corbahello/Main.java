/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corbahello;

import HelloApp.Hello;
import HelloApp.HelloHelper;
import HelloApp.HelloPOA;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

/**
 *
 * @author ilsejazmingomezperez
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            //Se crea e inicializa el ORB
            ORB orb = ORB.init(args,null);  
            
            //obtiene la referencia para rootpoa y aciva el POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            
            //crea el servidor y lo registra con ORB
            HelloServant helloObj = new HelloServant();
            helloObj.setORB(orb);
            
            //obtiene el objeto de referencia de el servidor
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloObj);
            Hello href = HelloHelper.narrow(ref);
            
            //obtiene el nombre root
            //NameServide invoca a name service
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            
            //Usa NamingContextExt el cual es parte de la interoperabilidad
            //Naming Service (INS) specification
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef); 
            
            //Enlaza el objeto referencia en naming
            String name = "Hello";
            NameComponent path[]=ncRef.to_name(name);
            ncRef.rebind(path, href);
            
            System.out.println("Hello Server ready and waitting...");
            
            //Espera la invocacion por el cliente
            while(true){
                orb.run();
            }
            
        }catch(Exception e){
            System.err.println("Error: "+ e);
            e.printStackTrace(System.out);
            
        }
        
         System.out.println("Hello Server Existing...");
    
    }
    
}


