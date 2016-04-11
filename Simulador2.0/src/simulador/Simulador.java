package simulador;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Simulador implements Runnable
{    
    public static void main(String[] args) 
    {
        Ruta500 r500 = new Ruta500();
        Ruta602 r602 = new Ruta602();
        Ruta25 r25 = new Ruta25();
        Ruta300 r300 = new Ruta300();
        Ruta142 r142 = new Ruta142();
        
        r500.start();
        r602.start();
        r25.start();
        r300.start();
        r142.start();
        
          (new Thread(new Simulador())).start();
          
          Runnable helloRunnable = new Runnable() {
    public void run() {
        System.out.println("notificando suscriptores");
        r142.notificarSuscriptores();
        r500.notificarSuscriptores();
        r602.notificarSuscriptores();
        r25.notificarSuscriptores();
        r300.notificarSuscriptores();
    }
};

ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);
    }

    public void run() {
        Server servidor = new Server(5000);
    }
    
 }
