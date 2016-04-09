package Servidor;

import java.rmi.*;

public interface AddServerIntf extends Remote
{
    String mensaje(String accion) throws RemoteException;
}
