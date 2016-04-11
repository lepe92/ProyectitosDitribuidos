/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author eejl_
 */
public class ContactarServidor extends Thread {

    public ContactarServidor() {
    }

    public void actualizarEnServidor(String file) {
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
            //FileChannel channel = new RandomAccessFile(secondLocalFile, "rw").getChannel();
            //FileLock lock = channel.tryLock();

            
                //System.out.println("bloqueo por parte del ftp "+lock.isValid());

                // String secondRemoteFile = "public_html/Ubus/Administrador/Choferes/" + file;
                String secondRemoteFile = "public_html/prueba/mapas/" + file;
                boolean bandera = true;

                InputStream inputStream = new FileInputStream(secondLocalFile);

                // ftpClient.deleteFile("public_html/Ubus/Administrador/info/data.xml);
                //System.out.println("Start uploading file");
                OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
                byte[] bytesIn = new byte[4096];
                int read = 0;

                while ((read = inputStream.read(bytesIn)) != -1) {
                    outputStream.write(bytesIn, 0, read);
                }
                inputStream.close();
                outputStream.close();

                // lock.release();
                // channel.close();
                boolean completed = ftpClient.completePendingCommand();
                if (completed) {
                    System.out.println((char) 27 + "[34;43mThe file is uploaded successfully. " + file);
//System.out.println("The file is uploaded successfully. " + file);
                }
           
           // channel.close();
            
            //   lock.release();
            //  channel.close();
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

    @Override
    public void run() {

        // System.out.println("Enviando data25");               
        actualizarEnServidor("data25.xml");
        // System.out.println("Enviando data142");               
        actualizarEnServidor("data142.xml");
        // System.out.println("Enviando data300");               
        actualizarEnServidor("data300.xml");
        // System.out.println("Enviando data500");               
        actualizarEnServidor("data500.xml");
        // System.out.println("Enviando data602");               
        actualizarEnServidor("data602.xml");

    }
}
