package simulador;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public abstract class Ruta extends Thread {

    public ArrayList<Camion> unidades;
    public ArrayList<p> puntosRuta;
    public ArrayList<p> paradasRuta;
    public double[][] puntos;
    public String filetxtPath;
    public String filePathMapaxml;
    public String filePathDataxml;
    public static int turno;
    public int numRuta, numCamiones;
    public final String pathPrincipal = "C:/wamp/www/Ubus/";

 

    /*public void addSuscriptor(Suscriptor m) {
        suscriptores.add(m);
    }*/
    

    public Ruta() {
    }

    public void inicial() {
        int i, ind, d;
        Camion cmn;
        p p0;

        d = puntosRuta.size() / numCamiones;

        for (i = 0; i < numCamiones; i++) {
            ind = i * d;
            p0 = puntosRuta.get(ind);
            cmn = new Camion(p0, ind);
            unidades.add(cmn);
        }
    }

    public void leerArchivoTxt() {
        String cad = "";
        File file = new File(filetxtPath);
        RandomAccessFile fd;
        int fn, n, x, s, i;
        byte[] b;
        char[] c;

        try {
            fd = new RandomAccessFile(file, "rw");

            fn = (int) fd.length();
            b = new byte[fn];
            c = new char[fn];
            fd.read(b);

            for (i = 0; i < fn; i++) {
                c[i] = (char) b[i];
            }

            cad = String.valueOf(c);
            fd.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        StringTokenizer tokens;
        tokens = new StringTokenizer(cad, "{\":,}");
        n = tokens.countTokens();
        s = n / 4;

        puntos = new double[s][2];

        x = 0;
        for (i = 0; i < n; i++) {
            switch (i % 4) {
                case 0:
                case 2:
                    tokens.nextToken();
                    break;
                case 1:
                    puntos[x][0] = Double.valueOf(tokens.nextToken());
                    break;
                case 3:
                    puntos[x][1] = Double.valueOf(tokens.nextToken());
                    x++;
                    break;
            }
        }
        //write_File_mapaxml();
    }

    public void interpolar() {
        determinarParadas();

        int i, n, l;
        double s, d, dx, dy, x1, x2, y1, y2;
        double xaux, yaux;
        int contador;
        p coor, aux;

        n = puntos.length;

        System.out.println(" Interpolating ...");

        for (i = 1; i < n; i++) {
            x1 = puntos[i - 1][0];
            y1 = puntos[i - 1][1];
            x2 = puntos[i][0];
            y2 = puntos[i][1];

            d = distancia(x1, y1, x2, y2) * 1000;

            if (d < 20) {
                continue;
            } else {
                l = 10;
            }

            s = d / l;
            contador = (int) Math.floor(s);

            dx = x2 - x1;
            dy = y2 - y1;

            coor = new p(x1, y1);
            Iterator<p> ite = paradasRuta.iterator();
            while (ite.hasNext()) {
                aux = ite.next();
                if (aux.lat == coor.lat && aux.lng == coor.lng) {
                    coor.isStop = true;
                }
            }
            puntosRuta.add(coor);

            xaux = x1;
            yaux = y1;
            do {
                xaux += dx / s;
                yaux += dy / s;
                coor = new p(xaux, yaux);

                ite = paradasRuta.iterator();
                while (ite.hasNext()) {
                    aux = ite.next();
                    if (aux.lat == coor.lat && aux.lng == coor.lng) {
                        coor.isStop = true;
                    }
                }
                puntosRuta.add(coor);

                contador--;
            } while (contador > 0);
        }

    }

    public void write_File_mapaxml() {
        File f1 = new File(filePathMapaxml);
        if (f1.exists()) {
            f1.delete();
        }

        RandomAccessFile archivo;

        int n, i;
        byte[] salto = new byte[2];
        salto[0] = '\r';
        salto[1] = '\n';

        try {
            archivo = new RandomAccessFile(f1, "rw");

            System.out.println(" Writing " + filePathMapaxml + " ...");
            archivo.writeBytes("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            archivo.write(salto);
            archivo.writeBytes("<markers>");
            archivo.write(salto);
            n = puntos.length;

            for (i = 0; i < n; i++) {
                archivo.writeBytes("<marker lat=\"" + String.valueOf(puntos[i][0])
                        + "\" lng=\"" + String.valueOf(puntos[i][1]) + "\"/>");
                archivo.write(salto);
            }

            archivo.writeBytes("</markers>");
            archivo.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void write_File_dataxml() {
        //System.out.println("Writing data file : " + filePathDataxml);
        p x;
        String slat1, slon1;
        int n = puntosRuta.size();

        File f1 = new File(filePathDataxml);
        if (f1.exists()) {
            f1.delete();
        }

        RandomAccessFile archivo;

        int j;
        byte[] salto = new byte[2];
        salto[0] = '\r';
        salto[1] = '\n';

        try {
            archivo = new RandomAccessFile(f1, "rw");
            archivo.writeBytes("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            archivo.write(salto);
            archivo.writeBytes("<markers>");
            archivo.write(salto);
            /*
            for(j=0; j<paradasRuta.size(); j++)
            {
                x = paradasRuta.get(j);
                slat1 = String.valueOf(x.lat);
                slon1 = String.valueOf(x.lng);

                archivo.writeBytes("<marker status=\"station\" lat=\"" + slat1 + "\" lng=\"" + slon1 + "\" />"); archivo.write(salto);
            }*/

            for (j = 0; j < unidades.size(); j++) {
                Camion cmn = unidades.get(j);
                x = puntosRuta.get((cmn.indice + 1) % puntosRuta.size());

                if (x.isStop) {
                    int factor = (int) Math.rint(Math.random() * (20) - 10);
                    int disponible = 65 - cmn.pasajeros;
                    if (factor < 0) {
                        if (Math.abs(factor) >= cmn.pasajeros) {
                            cmn.abordando = 3 * cmn.pasajeros;
                            cmn.pasajeros = 0;
                        }
                    } else if (factor <= disponible) {
                        cmn.pasajeros += factor;
                        cmn.abordando = 3 * factor;
                    }
                    cmn.indice++;
                } else if (cmn.posicion.isStop) {
                    if (cmn.abordando > 0) {
                        cmn.abordando--;
                    } else {
                        cmn.indice++;
                    }
                } else {
                    cmn.indice++;
                }

                cmn.posicion = puntosRuta.get(cmn.indice % puntosRuta.size());

                x = cmn.posicion;
                slat1 = String.valueOf(x.lat);
                slon1 = String.valueOf(x.lng);
                archivo.writeBytes("<marker status=\"bus" + numRuta + "\" lat=\"" + slat1 + "\" lng=\"" + slon1 + "\" />");
                archivo.write(salto);
            }
            archivo.writeBytes("</markers>");
            archivo.close();
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void determinarParadas() {
        int i, j, n;
        double d, x1, x2, y1, y2;
        double acumulado = 0;
        p x, coor;
        //String slat1, slon1;

        System.out.println(" Calculating bus stop ...");
        /*String fname = "C:\\Users\\EndUser\\Documents\\Ubus\\paradas" + String.valueOf(numRuta) + ".xml";
        File f1 = new File(fname);
        if(f1.exists()) f1.delete();
        
        RandomAccessFile archivo;
        byte[] salto = new byte[2];
               salto[0] = '\r';
               salto[1] = '\n';*/

        n = puntos.length;

        for (i = 1; i < n; i++) {
            x1 = puntos[i - 1][0];
            y1 = puntos[i - 1][1];
            x2 = puntos[i][0];
            y2 = puntos[i][1];

            d = distancia(x1, y1, x2, y2) * 1000;

            if ((d + acumulado) < 200) {
                acumulado += d;
            } else {
                coor = new p(x2, y2);
                paradasRuta.add(coor);
                acumulado = 0;
            }
        }
        /*
        try
        {
            archivo = new RandomAccessFile(f1, "rw");
            archivo.writeBytes("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            archivo.write(salto);
            archivo.writeBytes("<markers>");
            archivo.write(salto);
            
            for(j=0; j<paradasRuta.size(); j++)
            {
                x = paradasRuta.get(j);
                slat1 = String.valueOf(x.lat);
                slon1 = String.valueOf(x.lng);

                archivo.writeBytes("<marker status=\"station\" lat=\"" + slat1 + "\" lng=\"" + slon1 + "\" />");
                archivo.write(salto);
            }
            archivo.writeBytes("</markers>");
            archivo.close();
        }catch(IOException e){ System.err.println(e.getMessage());}*/
    }

    public static double distancia(double Lat1, double Lon1, double Lat2, double Lon2) {
        double D;
        double PI = 3.14159265358979323846;
        Lat1 = Lat1 * PI / 180;
        Lon1 = Lon1 * PI / 180;
        Lat2 = Lat2 * PI / 180;
        Lon2 = Lon2 * PI / 180;
        D = 6378.137 * Math.acos(Math.cos(Lat1) * Math.cos(Lat2) * Math.cos(Lon2 - Lon1)
                + Math.sin(Lat1) * Math.sin(Lat2));
        return D;
    }

    @Override
    public synchronized void run() {
        inicial();
        long time = 1200;

        while (true) {
            write_File_dataxml();
            
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
            }
        }
    }
}
