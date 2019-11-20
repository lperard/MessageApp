import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;


public class Sock {

    private int port;

    private Socket link;

    //private BufferedReader inputBuff;

    //private PrintWrinter out;

    private String lastData;

    public boolean connected = false;

    public Sock(Socket sock, int port) {
        this.link = sock;
        this.port = port;
    }

    /*public static void main (String[] args)throws IOException {
        
        Sock sock = new Sock();
        Sock.connectdingToServer();
        int sent_port;
        //int cmp = str.compareTo("JOUR");
        
        
        out.println("Server?");
        sent_port = inputBuff.readLine();

        //On coupe l'ancienne connexion et on se connecte au nouveau serveur
        



        //this.port = sent_port;
        if (cmp == 0) {
                      // envoi d'un message
        }
        else {
            out.println(str);
        }
        //System.out.println(date);
    }

    private void setupConnection(String host, int port) {
        /*this.linkServ = new Socket(host, port);
        this.inputBuff = new BufferedReader(new InputStreamReader(linkServ.getInputStream()));
        this.currentOut = new PrintWriter(linkServ.getOutputStream(),true);*/
        
        /*Socket linkServ = new Socket("127.0.0.1", 49152);
        System.out.println("Connexion au localhost\n");
        BufferedReader InputBuff = new BufferedReader(new InputStreamReader(linkServ.getInputStream()));
        PrintWriter out = new PrintWriter(linkServ.getOutputStream(),true);
        System.out.println("Streams en place\n");*/
    }
    
 /*   private void connectingToServer () {
        setupConnection("127.0.0.1", 49152);
    }
    private Integer waitForData() {
        String data = "";
        data = this.currentInputBuff.readLine();
        return data;
    }
    private void endConnection() {
        this.linkServ.close();
        this.InputBuff.close();
        this.out.close();

        /*linkServ.close();
        InputBuff.close();
        out.close();*/
//Gestion de l'horodatage des messages
        /*Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        System.out.println(formatter.format(date));*/