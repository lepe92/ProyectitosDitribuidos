
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eejl_
 */
public class EnviaPost {
    public static void main(String[] args) {
     String message="&id=[\"edxAM5s6aSI:APA91bE9q0E7fwByitjgKyEwHZkxLgiuCEYqPhSBHOmUm4QkqMUpXoYL8gikHmSEVtJm0A6bBA_xgy_TH4S5dPpbzhcajQkMQs2Eot48HqMkr8qqKr3unDNkIj-qDLAxDjGKah6AFIB9\",\"ce_KeNyXuMQ:APA91bEk7J-9srI8Mc2Y65tQJo-_dZ4gIv6MoroIOoJJMcE2n_YxRyOAa-UZZdYygtGVN2qc0u-58auBqhzLmGpoPj3aS5gLTiLkKEpbxLyj_C3gAHusB-C-YbhSa1NuGkOEqp0MSFv0\"]";
String mensaje="&mensaje={\"Camion\":[{\"nombre\":\"El mil vueltas\",\"capacidad\":\"34/40\",\"idcamion\":\"1\",\"etiqueta\":\"bus1\",\"lat\":\"20.7336\",\"lng\":\"-103.35151\"},{\"nombre\":\"Camioncito 16\",\"capacidad\":\"27/40\",\"idcamion\":\"2\",\"etiqueta\":\"bus2\",\"lat\":\"20.7339\",\"lng\":\"-103.35155\"}]}";     
//$jsonString = '["xxx@gmail.com","yy@gmail.com","rr@gmail.com"]';
//$arrayOfYourEmails=json_decode($jsonString); 
     try {
            // open a connection to the site
            URL url = new URL("http://localhost/Ubus/mandaGCM2.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print(message);
            ps.print(mensaje);

            // we have to get the input stream in order to actually send the request
          InputStream n= con.getInputStream();
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(n));
         String temp=""; 
         while((temp=bufferedReader.readLine())!=null){
           System.out.println(temp);
          }
            // close the print stream
            ps.close();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
    }
}
