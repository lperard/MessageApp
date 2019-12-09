import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class SendManager implements Runnable{

    private DatagramSocket sock;

    private int port;

    public SendManager(int port){
        this.port = port;
        try {this.sock = new DatagramSocket();}
        catch (SocketException e) {
            System.out.println("Ca bug");
        }
    }
    public void run() {
        System.out.println("Lancement du thread d'envoi");
        try {
            InetAddress ip = InetAddress.getLocalHost();
            User user = new User(ip, "Didiax");
            MessageSys msg_sys = new MessageSys(Type.Hello,user);
            //MAINTENANT ON SERIALIZE
            










            String message = msg_sys.constructMessageSystem();
            System.out.println(message);
        }
        catch (UnknownHostException e) {
            System.out.println ("Erreur dans la récupération du nom d'hôte");
        }
        Scanner input = new Scanner(System.in);
        while(true) {
            String msg = input.nextLine();
            try{ 
                InetAddress host = InetAddress.getLocalHost();
                    DatagramPacket out = new DatagramPacket(msg.getBytes(), msg.length(), host, port);
                    
                    try{sock.send(out);}
                        catch (IOException io) {
                            System.out.println("Ca rebug");
                        };
                }
            catch (UnknownHostException uhe) {
                System.out.println("Oula");
            }
        }
        
        
        //sock.close();
        //input.close();
    }

    public void sendBroadcast (MessageSys msgSys) {
        /*DatagramSocket BroadcastSocket = new DatagramSocket();
        BroadcastSocket.setBroadcast(true);
        String message_sys = new String(constructMessageSystem());
        DatagramPacket message_broadcasted = new DatagramPacket();*/
    }
}