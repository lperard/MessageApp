import java.net.*;
import java.io.*;


public class Server  {
    static int port = 1998;
    public static void main (String[] args) throws IOException{
        
        Server[] bonsoir;
        for(int i = 1980; i < 2000; i++) {
            bonsoir.append(new Server(i));
        }

    }

    public Server(int port) throws IOException{
        ServerSocket serv = new ServerSocket(port);
        BufferedReader InputBuff;
        //PrintWriter out;
        //String message = "TROUVE";
        String buff;

        while (true) {
            Socket sock = serv.accept();
            System.out.println("Server connected");
            InputBuff = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            //out = new PrintWriter(sock.getOutputStream(),true);
            System.out.println("Streams en place");

            buff = InputBuff.readLine();
            if (buff != null) {
                //out.println(message);
            }
            int cmp = buff.compareTo("END");
            if (cmp == 0) {
                System.out.println("COCUOU");
                sock.close();
                //out.close();
                InputBuff.close();
            }
            cmp = buff.compareTo("Server?");
            if (cmp == 0) {
                System.out.println("Bien vu!");
                sock.close();
                //out.close();
                InputBuff.close();
            }
        System.out.println("Fermeture du serveur");       
        InputBuff.close();
        //out.close();
        sock.close();
        
        }
    }
    
//Code pour créer un seul serveur dans le main qui marche
/*ServerSocket serv = new ServerSocket(port);
        BufferedReader InputBuff;
        PrintWriter out;
        String message = "TROUVE";
        String buff;

        while (true) {
            Socket sock = serv.accept();
            System.out.println("Server connected");
            InputBuff = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            //out = new PrintWriter(sock.getOutputStream(),true);
            System.out.println("Streams en place");

            buff = InputBuff.readLine();
            if (buff != null) {
                out.println(message);
            }
            int cmp = buff.compareTo("END");
            if (cmp == 0) {
                System.out.println("COCUOU");
                sock.close();
                out.close();
                InputBuff.close();
            }
            cmp = buff.compareTo("Server?");
            if (cmp == 0) {
                System.out.println("Bien vu!");
                sock.close();
                out.close();
                InputBuff.close();
            }
        System.out.println("Fermeture du serveur");       
        InputBuff.close();
        //out.close();
        sock.close();
        
        }*/      
}
