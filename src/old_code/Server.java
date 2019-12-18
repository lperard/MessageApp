import java.net.*;
import java.io.*;


public class Server {
    static int Serverport = 5000;
    static int next_serv = 49153;
    public static void main (String[] args) throws IOException{
        
        //Quand on recoit un certain message genre "initiate connection, on port= 2000", on lance un thread qui va recevoir
        Server mainServ = new Server(Serverport);

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
            
            
            
            
            //Fermeture de la connexion
            int cmp = buff.compareTo("END");
            if (cmp == 0) {
                System.out.println("COCUOU");
                sock.close();
                //out.close();
                InputBuff.close();
            }
            cmp = buff.compareTo("Server?");
            if (cmp == 0) {
                System.out.println(next_serv);
                next_serv++;
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
