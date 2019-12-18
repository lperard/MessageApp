import java.net.*;
import java.io.*;

public class UDPsock {
    private DatagramSocket sock;


    static public void main (String[] args) throws SocketException, UnknownHostException, IOException {
        DatagramSocket sock = new DatagramSocket(5001);
        String msg = new String(args[0]);
        String msg_envoye = msg;
        System.out.println("Récupération de l'arg:");
        System.out.println(msg);
        InetAddress host = InetAddress.getLocalHost();
        for (int cmp = 0; cmp < 10; cmp++){
            msg_envoye = msg.concat(Integer.toString(cmp));
            DatagramPacket out = new DatagramPacket(msg_envoye.getBytes(), msg_envoye.length(),host, 5000);
            sock.send(out);
        }
        
        sock.close();
    }



}