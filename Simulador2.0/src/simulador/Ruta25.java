package simulador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ruta25 extends Ruta {

    public static ArrayList<Suscriptor> suscriptores;

    public Ruta25() {
        dataXML = "data25.xml";
        numRuta = 25;
        numCamiones = 1;
        puntosRuta = new ArrayList<>();
        paradasRuta = new ArrayList<>();
        unidades = new ArrayList<>();
        filetxtPath = pathPrincipal + "Ruta 25.txt";
        filePathDataxml = pathPrincipal + "data25.xml";
        filePathMapaxml = pathPrincipal + "mapa25.xml";
        suscriptores = new ArrayList<>();
        leerArchivoTxt();
        interpolar();
        
         Runnable helloRunnable = new Runnable() {
            public void run() {
                //((ContactarServidor) new ContactarServidor()).start();
                System.out.println("notificar suscriptores 25");
                notificarSuscriptores(); 
           //     actualizarEnServidor();
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);
    }

    public void notificarSuscriptores() {
        //  String message = "&id=[\"edxAM5s6aSI:APA91bE9q0E7fwByitjgKyEwHZkxLgiuCEYqPhSBHOmUm4QkqMUpXoYL8gikHmSEVtJm0A6bBA_xgy_TH4S5dPpbzhcajQkMQs2Eot48HqMkr8qqKr3unDNkIj-qDLAxDjGKah6AFIB9\",\"ce_KeNyXuMQ:APA91bEk7J-9srI8Mc2Y65tQJo-_dZ4gIv6MoroIOoJJMcE2n_YxRyOAa-UZZdYygtGVN2qc0u-58auBqhzLmGpoPj3aS5gLTiLkKEpbxLyj_C3gAHusB-C-YbhSa1NuGkOEqp0MSFv0\"]";
        // String mensaje = "&mensaje={\"Camion\":[{\"nombre\":\"El mil vueltas\",\"capacidad\":\"34/40\",\"idcamion\":\"1\",\"etiqueta\":\"bus1\",\"lat\":\"20.7336\",\"lng\":\"-103.35151\"},{\"nombre\":\"Camioncito 16\",\"capacidad\":\"27/40\",\"idcamion\":\"2\",\"etiqueta\":\"bus2\",\"lat\":\"20.7339\",\"lng\":\"-103.35155\"}]}";
        String id = "&id=[";
        for (int i = 0; i < suscriptores.size(); i++) {
            if (i == suscriptores.size() - 1) {//el ultimo elemento no lleva  , concatenada
                id += "\"" + suscriptores.get(i).token + "\"";
            } else {
                id += "\"" + suscriptores.get(i).token + "\",";
            }
        }
        id += "]";

        String camiones = "&mensaje={\"Camion\":[";
        for (int i = 0; i < unidades.size(); i++) {
            if (i == unidades.size() - 1) {
                camiones += "{\"nombre\":\"El mil vueltas\",\"capacidad\":\"" + unidades.get(i).pasajeros + "/65\",\"idcamion\":\"1\",\"etiqueta\":\"bus1\",\"lat\":\"" + unidades.get(i).posicion.lat + "\",\"lng\":\"" + unidades.get(i).posicion.lng + "\"}";
            } else {
                camiones += "{\"nombre\":\"El mil vueltas\",\"capacidad\":\"" + unidades.get(i).pasajeros + "/65\",\"idcamion\":\"1\",\"etiqueta\":\"bus1\",\"lat\":\"" + unidades.get(i).posicion.lat + "\",\"lng\":\"" + unidades.get(i).posicion.lng + "\"},";
            }
        }
        camiones += "]}";
        try {
            // open a connection to the site
            URL url = new URL("http://localhost/Ubus/mandaGCM2.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print(id);
            ps.print(camiones);

            // we have to get the input stream in order to actually send the request
            InputStream n = con.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(n));
            String temp = "";
            while ((temp = bufferedReader.readLine()) != null) {
                System.out.println(temp);
            }
            // close the print stream
            ps.close();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        //enviar cada token separado por , y entrecomillado simple
        //{"Camion":[{"nombre":"El mil vueltas","capacidad":"34/40","idcamion":"1","etiqueta":"bus1","lat":"20.7336","lng":"-103.35151"},{"nombre":"Camioncito 16","capacidad":"27/40","idcamion":"2","etiqueta":"bus2","lat":"20.7339","lng":"-103.35155"}]}
    }

}
