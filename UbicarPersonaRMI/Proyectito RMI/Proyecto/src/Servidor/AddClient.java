package Servidor;

import java.rmi.*;
import java.util.Scanner;

public class AddClient 
{
    public static void main(String args[])
    {
        try
        {   // URL :  protocolo://maquina:host/nombreDelServicio
            String addServerURL = "rmi://10.0.5.121:1099/AddServer";
            
            AddServerIntf addServerIntf = (AddServerIntf)Naming.lookup(addServerURL);
            
            String cad;
            double x1, x2;
            
            Scanner r = new Scanner(System.in);
            System.out.print("x1 << ");
            cad = r.nextLine();
            x1 = Double.valueOf(cad);
            System.out.println();
            System.out.print("x2 << ");
            cad = r.nextLine();
            
            x2 = Double.valueOf(cad);
            
            System.out.println("The sum is : " + addServerIntf.add(x1, x2));
            
        }catch(Exception e){System.err.println("Exception: " + e.getMessage());}
    }
}
