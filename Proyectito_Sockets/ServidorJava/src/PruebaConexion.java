
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author edwin
 */
public class PruebaConexion extends Thread {
    /*
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;
    
    public PruebaConexion(Socket socket) {
        this.socket = socket;
        
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void desconnectar() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     */
    public String actualizarNombre(String envio) {
        String response = "";
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        BufferedReader bufferedReader = null;
        
        try {
            
           String urlParameters  =envio;//"param1=a&param2=b&param3=c";
byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
int    postDataLength = postData.length;
String request        = "http://jimenezlepe.comuv.com/Ubus/prueba.php";
URL    url            = new URL( request );
conn= (HttpURLConnection) url.openConnection();           
conn.setDoOutput( true );
conn.setInstanceFollowRedirects( false );
conn.setRequestMethod( "POST" );
conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
conn.setRequestProperty( "charset", "utf-8");
conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
conn.setUseCaches( false );
try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
   wr.write( postData );
}

            //do somehting with response
            is = conn.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));

//                    Log.i("mensaje2", macajena);
            //Insercion correcta
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                // Log.i("mensaje", json);
                if(!(json.contains("<!")||json.contains("<")))
                     System.out.println(json);
                
                
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return response;
    }
    
    public static void main(String args[]){
    PruebaConexion p= new PruebaConexion();
    String usuario="Edwin";
    String correo="eejl_1992@hotmail.com";
    p.actualizarNombre("usuario="+usuario+"&correo="+correo);
    }
    /*
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
    public void run() {
        String accion = "";
        
        try {
            accion = dis.readUTF();
            System.out.println(accion);
            if (accion.contains(",\"opcionactualizar\":\"1\"")) {
                //System.out.println(accion);
                String cad = accion.replace(",\"opcionactualizar\":\"1\"", "");
                // if (accion.equals("hola")) {
                System.out.println("cliente " + this.socket + "dice " + cad);
                String respuesta = actualizarNombre(cad);
                //cad es la cadena en json para mandar a php "http://www.jimenezlepe.comuv.com/insertarnombre.php";

                dos.writeUTF(respuesta);
                //}
            } else if (accion.equals("solicitaJSON")) {
                System.out.println("cliente " + this.socket + "dice " + accion);
                String respuesta = solicitarJSON();
                //cad es la cadena en json para mandar a php "http://www.jimenezlepe.comuv.com/insertarnombre.php";

                dos.writeUTF(respuesta);
            } else if (accion.contains(",\"solicitapersona\":\"solicitapersona\"")) {
                //System.out.println(accion);
                String cad = accion.replace(",\"solicitapersona\":\"solicitapersona\"", "");
                // if (accion.equals("hola")) {
                System.out.println("cliente " + this.socket + "dice " + cad);
                String respuesta = solicitarPersona(cad);
                dos.writeUTF(respuesta);
                //}
            } else {
                //inserción
                String respuesta = insertar(accion);
                System.out.println("Cliente " + this.socket + " dice" + accion);
                if (respuesta != null) {
                    dos.writeUTF(respuesta);
                } else {
                    dos.writeUTF("nada");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        desconnectar();
        
    } */
}
