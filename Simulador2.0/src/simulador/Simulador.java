package simulador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class Simulador implements Runnable {

    public static void main(String[] args) {
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

        /*
                actualizarEnServidor("mapa25.xml");
                actualizarEnServidor("mapa142.xml");
                actualizarEnServidor("mapa300.xml");
                actualizarEnServidor("mapa500.xml");
                actualizarEnServidor("mapa602.xml");
                
                actualizarEnServidor("paradas25.xml");
                actualizarEnServidor("paradas142.xml");
                actualizarEnServidor("paradas300.xml");
                actualizarEnServidor("paradas500.xml");
                actualizarEnServidor("paradas602.xml");
         */
        (new Thread(new Simulador())).start();

        Runnable helloRunnable = new Runnable() {
            public void run() {
               // System.out.println("notificando suscriptores 142");
                //r142.notificarSuscriptores();
              /*  System.out.println("notificando suscriptores 500");
                r500.notificarSuscriptores();
                System.out.println("notificando suscriptores 602");
                r602.notificarSuscriptores();
                System.out.println("notificando suscriptores 25");
                r25.notificarSuscriptores();
                System.out.println("notificando suscriptores 300");
                r300.notificarSuscriptores();
*/                
//((ContactarServidor)new ContactarServidor()).start();
                //actualizarEnServidor("data25.xml");
                //actualizarEnServidor("data142.xml");
                //actualizarEnServidor("data300.xml");
                //actualizarEnServidor("data500.xml");
                //actualizarEnServidor("data602.xml");  

            }
        };

        Runnable helloRunnable2 = new Runnable() {
            public void run() {
                //((ContactarServidor) new ContactarServidor()).start();
           /*     r25.actualizarEnServidor();
                r142.actualizarEnServidor();
                r500.actualizarEnServidor();
                r602.actualizarEnServidor();
                r300.actualizarEnServidor();
*/
            }
        };
        //ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        //executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);

        //ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);
        //executor2.scheduleAtFixedRate(helloRunnable2, 0, 5, TimeUnit.SECONDS);
    }

    public void run() {
        Server servidor = new Server(5000);

        //((ServidorHilo) new ServidorHilo(socket)).start();
    }

    public static void actualizarEnServidor(String file) {
        String server = "jimenezlepe.comuv.com";
        int port = 21;
        String user = "a2811468";
        String pass = "ornitorrinco8";

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #2: uploads second file using an OutputStream
            File secondLocalFile = new File("C:/wamp/www/Ubus/" + file);
            String secondRemoteFile = "public_html/Ubus/Administrador/Choferes/" + file;
            InputStream inputStream = new FileInputStream(secondLocalFile);

            // ftpClient.deleteFile("public_html/Ubus/Administrador/info/data.xml);
            System.out.println("Start uploading file");
            OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
            byte[] bytesIn = new byte[4096];
            int read = 0;

            while ((read = inputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
            inputStream.close();
            outputStream.close();

            boolean completed = ftpClient.completePendingCommand();
            if (completed) {
                System.out.println("The file is uploaded successfully.");
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
