package simulador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Simulador implements Runnable {

    public static ArrayList<Camion2> camiones500 = new ArrayList();
    public static ArrayList<Camion2> camiones602 = new ArrayList();
    public static ArrayList<Camion2> camiones25 = new ArrayList();
    public static ArrayList<Camion2> camiones300 = new ArrayList();
    public static ArrayList<Camion2> camiones142 = new ArrayList();

    public static void main(String[] args) {
        envia2();
        Ruta500 r500 = new Ruta500(camiones500);
        Ruta602 r602 = new Ruta602(camiones602);
        Ruta25 r25 = new Ruta25(camiones25);
        Ruta300 r300 = new Ruta300(camiones300);
        Ruta142 r142 = new Ruta142(camiones142);

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
                ((ContactarServidor) new ContactarServidor(r25.envio, r25.dataXML, r142.envio, r142.dataXML, r500.envio, r500.dataXML, r602.envio, r602.dataXML, r300.envio, r300.dataXML)).start();
                //((ContactarServidor) new ContactarServidor(r142.envio,r142.dataXML)).start();
                //((ContactarServidor) new ContactarServidor(r500.envio,r500.dataXML)).start();
                //((ContactarServidor) new ContactarServidor(r602.envio,r602.dataXML)).start();
                //((ContactarServidor) new ContactarServidor(r300.envio,r300.dataXML)).start();

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

        ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);
        executor2.scheduleAtFixedRate(helloRunnable2, 0, 5, TimeUnit.SECONDS);
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

    public static void envia2() {
        try {
            String mensaje = "";
            // open a connection to the site
            URL url = new URL("http://jimenezlepe.comuv.com/Ubus/Simulador/obtenerCamiones.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site

            ps.print(mensaje);

            // we have to get the input stream in order to actually send the request
            InputStream n = con.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(n));
            String temp = "";
            temp = bufferedReader.readLine();
            System.out.println(temp);
            procesarJson(temp);
            // close the print stream
            ps.close();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void procesarJson(String temp) {
        try {
            JSONObject jsnobject = new JSONObject(temp);
            JSONArray jsonArray = jsnobject.getJSONArray("Camion");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                Camion2 tempo = new Camion2(explrObject.getString("ruta"), explrObject.getInt("id") + "", explrObject.getString("nombre"), explrObject.getInt("capacidad") + "", explrObject.getString("chofer"));
                if (explrObject.getString("ruta").contains("142")) {
                    camiones142.add(tempo);
                } else if (explrObject.getString("ruta").contains("300")) {
                    camiones300.add(tempo);
                } else if (explrObject.getString("ruta").contains("602")) {
                    camiones602.add(tempo);
                } else if (explrObject.getString("ruta").contains("500")) {
                    camiones500.add(tempo);
                } else {
                    camiones25.add(tempo);
                }

            }
            System.out.println("camiones detectados" + camiones500.size());
        } catch (JSONException e) {
        }

    }

}
