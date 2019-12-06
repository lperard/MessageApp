import java.net.*;
import java.io.*;
import java.util.Scanner;

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
}