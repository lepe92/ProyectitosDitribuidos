
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
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
public class ServidorHilo extends Thread {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;

    private boolean servidor2 = false;

    public ServidorHilo(Socket socket) {
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

    public String actualizarNombre(String envio) {
        String response = "";
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        BufferedReader bufferedReader = null;

        try {

            String message = envio;
            URL url;
            if (!servidor2) {
                //temporal solo para que jale con el segundo servidor
                url = new URL("http://www.jimenezlepe.comuv.com/insertarnombre.php");
            } else {
                url = new URL("http://www.distribuidos.net23.net/insertarnombre.php");
            }
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
        } catch (HttpRetryException e) {
            System.out.println("el servidor no está disponible");
            response = "ERROR";
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return response;
    }

    public void setServidor2(boolean valor){
    servidor2=valor;
    }
    
    public String solicitarJSON() {
        System.out.println("valor de servidor 2"+servidor2);
        BufferedReader bufferedReader = null;
        try {
            URL url;
            if (!servidor2) {
                url = new URL("http://jimenezlepe.comuv.com/solicitajson.php");
            } else {
                url = new URL("http://distribuidos.net23.net/solicitajson.php");
            }
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json + "\n");
            }

            return sb.toString().trim();

        } 
        
        catch (Exception e) {
            return null;
        }

    }

    public String solicitarPersona(String message) {
        OutputStream os = null;
        BufferedReader bufferedReader = null;
        try {
            URL url;
            if (!servidor2) {
                url = new URL("http://jimenezlepe.comuv.com/consultaubicacion.php");
            } else {
                url = new URL("http://distribuidos.net23.net/consultaubicacion.php");
            }
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
        URL url;
        if (!servidor2) {
            url = new URL("http://jimenezlepe.comuv.com/solicita.php");
        } else {
            url = new URL("http://distribuidos.net23.net/solicita.php");
        }
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


    @Override
    public void run() {
        String accion = "";

        servidor2=!exists("http://jimenezlepe.comuv.com/default.php");
        
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
                System.out.println("Respuesta insertarnombre "+respuesta);
                if (!respuesta.equals("ERROR")) {
                    dos.writeUTF(respuesta);
                } else {
                    setServidor2(true);
                    respuesta = actualizarNombre(cad);
                    System.out.println("Respuesta insertarnombre "+respuesta);
                    dos.writeUTF(respuesta);
                }
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

    }
}
