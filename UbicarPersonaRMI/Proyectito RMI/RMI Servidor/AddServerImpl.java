package javarmi;

import java.rmi.*;
import java.rmi.server.*;

public class AddServerImpl extends UnicastRemoteObject implements AddServerIntf
{
    public AddServerImpl() throws RemoteException { }
    
    public double add(double d1, double d2) throws RemoteException
    {
        System.out.println(d1 + " + " + d2);
        return d1 + d2;
    }
}
