import java.net.*;
import java.io.*;
public class SendManager {

    private DatagramSocket sock;

    public SendManager() {
        try {this.sock = new DatagramSocket(5001);}
        catch (SocketException e) {
            System.out.println("Ca bug");
        }
    }
    public void run() {
        System.out.println("Lancement du thread d'envoi");
        String msg = new String("bonsoir");
        String msg_envoye = msg;
        System.out.println("Récupération de l'arg:");
        System.out.println(msg);
        InetAddress host = new InetAddress("127.0.0.1");
        try{host = InetAddress.getLocalHost();}
        catch (UnknownHostException uhe) {
            System.out.println("Oula");
        }
        for (int cmp = 0; cmp < 10; cmp++){
            msg_envoye = msg.concat(Integer.toString(cmp));
            DatagramPacket out = new DatagramPacket(msg_envoye.getBytes(), msg_envoye.length(),host, 5000);
            
            try{sock.send(out);}
                catch (IOException io) {
                    System.out.println("Ca rebug");
                };
        }
        sock.close();
    }
    
}
