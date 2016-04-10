
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import javax.naming.Context;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eejl_
 */
public class pruebaDisponibilidad {
  

public static boolean exists(String URLName) {

        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            // HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con = (HttpURLConnection) new URL(URLName)
            .openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        System.out.println(pruebaDisponibilidad.exists("http://www.jimenezlepe.comuv.com/default2.php"));
    }
}
