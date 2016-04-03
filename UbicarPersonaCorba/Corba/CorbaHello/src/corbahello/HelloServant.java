/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corbahello;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.ORB;

/**
 *
 * @author ilsejazmingomezperez
 */
public class HelloServant extends HelloApp.HelloPOA{

    private String massage="\n Hello world\n";
    private ORB orb; // ORB (Object Request Broker)

    
    public void setORB(ORB orb_val){
        orb=orb_val;
    }

    @Override
    public String sayHello(String msj) {
String respuesta="";        
//aqui se hará la machaaca
        //EN LUGAR DE ACCION ES msj
        //en lugar de writeUTF usaremos return respuesta
         if (msj.contains(",\"opcionactualizar\":\"1\"")) {
                //System.out.println(accion);
                String cad = msj.replace(",\"opcionactualizar\":\"1\"", "");
                // if (accion.equals("hola")) {
                //System.out.println("cliente " + this.socket + "dice " + cad);
                respuesta = actualizarNombre(cad);
                //cad es la cadena en json para mandar a php "http://www.jimenezlepe.comuv.com/insertarnombre.php";
                //}
            } else if (msj.equals("solicitaJSON")) {
                //System.out.println("cliente " + this.socket + "dice " + accion);
                respuesta = solicitarJSON();
                //cad es la cadena en json para mandar a php "http://www.jimenezlepe.comuv.com/insertarnombre.php";
            } else if (msj.contains(",\"solicitapersona\":\"solicitapersona\"")) {
                //System.out.println(accion);
                String cad = msj.replace(",\"solicitapersona\":\"solicitapersona\"", "");
                // if (accion.equals("hola")) {
                //System.out.println("cliente " + this.socket + "dice " + cad);
                respuesta = solicitarPersona(cad);
                
                //}
            } else {
    try {
        //inserción
        respuesta = insertar(msj);
    } catch (IOException ex) {
        Logger.getLogger(HelloServant.class.getName()).log(Level.SEVERE, null, ex);
    }
                //System.out.println("Cliente " + this.socket + " dice" + accion);
                if (respuesta == null) {
                 respuesta="nada";
                }
            }
       return respuesta;
    }

      public String actualizarNombre(String envio) {
        String response = "";
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        BufferedReader bufferedReader = null;
        
        try {
            
            String message = envio;
            URL url = new URL("http://www.jimenezlepe.comuv.com/insertarnombre.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //open
            conn.connect();

                    //setup send
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();

            //do somehting with response
            is = conn.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));

//                    Log.i("mensaje2", macajena);
            //Insercion correcta
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                // Log.i("mensaje", json);
                System.out.println("Json " + json);
                
            }
            response = "insertado";
        } catch (MalformedURLException ex) {
            Logger.getLogger(HelloServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HelloServant.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return response;
    }
    
    public String solicitarJSON() {
        
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL("http://jimenezlepe.comuv.com/solicitajson.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();
            
            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json + "\n");
            }
            
            return sb.toString().trim();
            
        } catch (Exception e) {
            return null;
        }
        
    }
    
    public String solicitarPersona(String message) {
        OutputStream os = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL("http://jimenezlepe.comuv.com/consultaubicacion.php");
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setFixedLengthStreamingMode(message.getBytes().length);

            //make some HTTP header nicety
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //open
            con.connect();

                    //setup send
            os = new BufferedOutputStream(con.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();
            
            StringBuilder sb = new StringBuilder();
            
            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json + "\n");
            }
            
            return sb.toString().trim();
            
        } catch (Exception e) {
            return null;
        }
        
    }
        //solicitar persona "http://jimenezlepe.comuv.com/consultaubicacion.php";
    
    public String insertar(String cad) throws MalformedURLException, IOException {
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        BufferedReader bufferedReader = null;
        URL url = new URL("http://jimenezlepe.comuv.com/solicita.php");
        
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setFixedLengthStreamingMode(cad.getBytes().length);
        
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

        //open
        conn.connect();

                    //setup send
        os = new BufferedOutputStream(conn.getOutputStream());
        os.write(cad.getBytes());
        //clean up
        os.flush();

        //do somehting with response
        is = conn.getInputStream();
        bufferedReader = new BufferedReader(new InputStreamReader(is));
        
        String response = bufferedReader.readLine();
        System.out.println(response);
        return response;
        // Log.i("mensaje2",macajena);
    }
    
    
    @Override
    public void shutdown() {
     orb.shutdown(false);
    }
    
    
}
