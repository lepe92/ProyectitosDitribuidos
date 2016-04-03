

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;
import java.util.ArrayList;


public class ruta 
{
    public static double[][] puntos;
    public static ArrayList<p> interp = new ArrayList<p>();
    
    public static void interpolar()
    {
        int i, n, l;
        double s, d, dx , dy, x1, x2, y1, y2;
        double xaux, yaux;
        int contador;
        p coor;
        
        n = puntos.length;
        
        for(i=1; i<n; i++)
        {
            x1 = puntos[i-1][0];
            y1 = puntos[i-1][1];
            x2 = puntos[i][0];
            y2 = puntos[i][1];
            
            d = distancia(x1, y1, x2, y2)*1000;
            
            if(d < 20) continue;
            else if(d < 100) l = 15;
            else if(d < 300) l = 24;
            else if(d < 500) l = 36;
            else l = 45;
            
            s = d/l;
            contador = (int)Math.floor(s);
            
            dx = x2 - x1;
            dy = y2 - y1;
            
            coor = new p(x1, y1);
            
            interp.add(coor);
            
            xaux = x1;
            yaux = y1;
            do
            {
                xaux += dx/s;
                yaux += dy/s;
                coor = new p(xaux, yaux);
                interp.add(coor);
                contador--;
            }while(contador > 0);
        }
        
    }
    
    public static void leerArchivo_Ruta_txt()
    {
        String fname = "C:\\wamp\\www\\Ubus\\Ruta.txt";
        File f1 = new File(fname);
        
        RandomAccessFile archivo;
        
        String cad = "";
        int i, n, s, x;
        double d;
        
        try
        {
            archivo = new RandomAccessFile(f1,"rw");
            
            n = (int)archivo.length();
            byte[] b = new byte[n];
            char[] c = new char[n];
            archivo.read(b);
            
            for(i=0; i<n; i++)
                c[i] = (char)b[i];
                
            cad = String.valueOf(c);
            
            archivo.close();
        }catch(IOException e){System.err.println(e.getMessage());}
        
        StringTokenizer tokens;
        tokens = new StringTokenizer(cad,"{\":,}");
        n = tokens.countTokens();
        s = n/4;
        
        puntos = new double[s][2];
        
        x=0;
        for(i=0; i<n; i++)
        {
            switch(i%4)
            {
                case 0: case 2:
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
    }
     
    public static void write_File_mapaxml()
    {
        String fname = "C:\\wamp\\www\\Ubus\\mapa.xml";
        File f1 = new File(fname);
        if(f1.exists()) f1.delete();
        
        RandomAccessFile archivo;
        
        int n, i;
        byte[] salto = new byte[2];
               salto[0] = '\r';
               salto[1] = '\n';
        
        try
        {
            archivo = new RandomAccessFile(f1,"rw");
            
            archivo.writeBytes("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            archivo.write(salto);
            archivo.writeBytes("<markers>");
            archivo.write(salto);
            n = puntos.length;
            
            for(i=0; i<n; i++)
            {
                archivo.writeBytes("<marker lat=\"" + String.valueOf(puntos[i][0]) 
                + "\" lng=\"" + String.valueOf(puntos[i][1]) + "\"/>");
                archivo.write(salto);
            }
            
            archivo.writeBytes("</markers>");
            archivo.close();
        }catch(IOException e){System.err.println(e.getMessage());}
    }
    
    public static void write_File_dataxml(int i)
    {
        p x;
        String slat1, slon1;
        
        String fname  = "C:\\wamp\\www\\Ubus\\data.xml";
        File f1 = new File(fname);
        if(f1.exists()) f1.delete();
        
        RandomAccessFile archivo;
        
        int j;
        byte[] salto = new byte[2];
               salto[0] = '\r';
               salto[1] = '\n';
        
        try
        {
            archivo = new RandomAccessFile(f1,"rw");
            archivo.writeBytes("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); archivo.write(salto);
            archivo.writeBytes("<markers>");                                  archivo.write(salto);
            
            for(j=0; j<10; j++ )
            {
                x = interp.get(i + j*150);
                slat1 = String.valueOf(x.lat);
                slon1 = String.valueOf(x.lng);

                archivo.writeBytes("<marker status=\"busy\" lat=\"" + slat1 + "\" lng=\"" + slon1 + "\" />"); archivo.write(salto);
            }
            archivo.writeBytes("</markers>");
            archivo.close();
        }catch(IOException e){ System.err.println(e.getMessage());}        
    }
    
    static double distancia(double Lat1, double Lon1, double Lat2, double Lon2)
    {
        double D;
        double PI = 3.14159265358979323846;
        Lat1 = Lat1 * PI / 180;
        Lon1 = Lon1 * PI / 180;
        Lat2 = Lat2 * PI / 180;
        Lon2 = Lon2 * PI / 180;
        D = 6378.137 * Math.acos(Math.cos( Lat1 ) * Math.cos( Lat2 ) * Math.cos( Lon2 - Lon1 ) 
                     + Math.sin( Lat1 ) * Math.sin( Lat2 ) );
        return D;
    }
    
    public static void main(String[] args)
    {
        long time0, cont, time;
        double d;
        
        time0 = System.currentTimeMillis();
        cont = 0;
        int i=1;
        //p x;
        
        leerArchivo_Ruta_txt();
        interpolar();
        
        System.out.print(puntos[0][0] + " , ");
        System.out.println(puntos[0][1]);
        while(cont < 1600)
        {
            time = (System.currentTimeMillis() - time0)/1000;
            if(time > cont)
            {
                //x = interp.get(i);
                write_File_dataxml(i);
                //System.out.format(" %.4f  , ", x.lat);
                //System.out.format(" %.4f  \n ", x.lng);
                //d = distancia(puntos[i-1][0], puntos[i-1][1],puntos[i][0], puntos[i][1]);
                //d *= 1000;
                //System.out.format(" %.2f \n", d);
                cont += 5;
                i++;
            }
        }
        
    }
}

class p
{
    public double lat;
    public double lng;
    
    public p(double la, double ln)
    {
        lat = la;
        lng = ln;
    }
}