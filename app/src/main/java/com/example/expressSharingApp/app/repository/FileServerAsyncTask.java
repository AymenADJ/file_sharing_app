package com.example.expressSharingApp.app.repository;

import static java.lang.System.out;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.example.expressSharingApp.app.activities.ReceiveActivity;
import com.example.expressSharingApp.app.activities.SendingFilesActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class FileServerAsyncTask extends AsyncTask {
    Context context;
    ArrayList<String> files ;
    String host ;
    int port;
    public FileServerAsyncTask(Context context , int port , String host){
        this.context = context;
        this.port = port;
        this.host = host;
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        try {

            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();

            /**
             * If this code is reached, a client has connected and transferred data
             * Save the input stream from the client as a JPEG file
             */
            final File f = new File(Environment.getExternalStorageDirectory() + "/"
                    + context.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
                    + ".jpg");

            File dirs = new File(f.getParent());
            if (!dirs.exists())
                dirs.mkdirs();
            f.createNewFile();
            InputStream inputstream = client.getInputStream();
            copyFile(inputstream, new FileOutputStream(f));
            serverSocket.close();
            Toast.makeText(context, "End sending files", Toast.LENGTH_SHORT).show();
            return f.getAbsolutePath();
        } catch (IOException e) {
            return null;
        }
    }
//            Socket socket = new Socket();
//            try {
//                socket.bind(null);
//                socket.connect((new InetSocketAddress(host, port)), 500);
//                OutputStream stream = socket.getOutputStream();
//                ContentResolver cr = context.getContentResolver();
//                InputStream is = null;
//                try {
//                    is = cr.openInputStream(Uri.parse(files.get(0)));
//                } catch (FileNotFoundException e) {
//                }
//                copyFile(is, stream);
//            } catch (IOException e) {
//            } finally {
//                if (socket != null) {
//                    if (socket.isConnected()) {
//                        try {
//                            socket.close();
//                        } catch (IOException e) {
//                            // Give up
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        ((SendingFilesActivity)context).changeElements(100,"");
//    return null;
//    }
//        int percentage = 0;
//        try{
////           if(port == 9999){ // send
//               files = ((SendingFilesActivity)context).files;
//               socket.bind(null);
//               socket.connect((new InetSocketAddress(host, port)), 500); // connect with device that have "port" as port
////               socket.setKeepAlive(true);
//               for (int i = 0; i <files.size() ; i++) {
//                   OutputStream stream = socket.getOutputStream(); // create new file on receiver device
//                   ContentResolver cr = context.getContentResolver();
//                   InputStream is = cr.openInputStream(Uri.parse(files.get(i))); // open file that we wanna to send it
//                   copyFile(is, stream); // copy {send in reality} from destination file to source file
//                   percentage = (int)(i/files.size() *100);
//                   ((SendingFilesActivity) context).changeElements(percentage , files.get(0));
//               }
//               socket.setKeepAlive(false);
//               socket.close();
//           }
//           if(port == 5000) { // receive
//               ServerSocket serverSocket = new ServerSocket(port); // connect to the sender
//               Socket client = serverSocket.accept(); // accept receiving files
//               String extension="jpg";
//               while (client.getKeepAlive()){
//                   String filePath = Environment.getExternalStorageDirectory() + "/"
//                           + context.getPackageName() + "/" + System.currentTimeMillis()
//                           + "."+extension;
//                   final File f = new File(filePath); // create file
//                   File dirs = new File(f.getParent()); // put the new file into a directory located on receiver device
//                   if (!dirs.exists())
//                       dirs.mkdirs();
//                   f.createNewFile();
//                   InputStream inputstream = client.getInputStream(); // receive streams
//                   copyFile(inputstream, new FileOutputStream(f)); // put them into the new file
//                   ((ReceiveActivity)context).setTextView(filePath);
//               }
//               serverSocket.close(); // close server socket : end transmitting files
//                socket.close();
//           }
//        }catch(IOException e){
//        e.printStackTrace();
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        return null;
// }

    private void copyFile(InputStream inputStream, OutputStream out) {
        byte buf[] = new byte[1024]; // buffer
        int len; // nn readen chars
        try {
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
