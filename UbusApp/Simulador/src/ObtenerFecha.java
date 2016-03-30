import java.net.InetAddress;
import java.text.SimpleDateFormat;
//Importamos las librerias de Apache Commons

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import java.util.Date;

public class ObtenerFecha {
 
    //Declaramos el servidor de donde obtendremos la fecha

    String servidor = "0.north-america.pool.ntp.org";
    public Date getNTPDate() {
         //Se le da un valor nulo por defecto a la variable

        Date fechaRecibida = null;
        //Se crea un objeto de tipo NTPUDPClient Clase de la libreria Commons

        NTPUDPClient cliente = new NTPUDPClient();
       //Tiempo de Espera Antes De Mandar Error.

        cliente.setDefaultTimeout(5000);
        try {
            //Obtenemos la direccion IP por medio de el host previamente Asignado

            InetAddress hostAddr = InetAddress.getByName(servidor);
            //Solicitamos la fecha al servidor

            TimeInfo fecha = cliente.getTime(hostAddr);
            //Recibimos y convertimos la fecha a formato DATE

            fechaRecibida = new Date(fecha.getMessage().getTransmitTimeStamp().getTime());
        } catch (Exception e) {
            System.err.println("Error "+e.getMessage());
        }
        //Cerramos la comunicación con el cliente

        cliente.close();
        //Retornamos la fecha ya convertida si no es nula , de ser nula regresa la fecha Local

        return fechaRecibida == null ? new Date() : fechaRecibida ;
     
    }
    public static void main(String[] args) {
         //Generamos un objeto de la clase ObtenerFecha.

        ObtenerFecha objFecha=new ObtenerFecha();
        //Generamos otro objeto de la clase SimpleDateFormat para darle formato a la fecha

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        //Creamos una variable de tipo String para almacenar la fecha

        String fecha ="";
        //Asignamos a la varible el valor recibido al instaciar el metodo getNTPDate por medio del objeto

        fecha=formato.format(objFecha.getNTPDate());
        //Imprimimos la fecha en consola.

        System.out.println(fecha);       
    }
    
} 