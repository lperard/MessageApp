import java.net.*;
import java.net.DatagramSocket;
import java.io.*;

public class UDPserv {
    private DatagramSocket sock;


    static public void main (String[] args) throws SocketException,IOException{
        int port = Integer.parseInt(args[0]);
        DatagramSocket sock = new DatagramSocket(port);
        int cmp = 0;
        while(true) {
            byte[] buffer = new byte[256];
            DatagramPacket in = new DatagramPacket(buffer, buffer.length);
            sock.receive(in);
            cmp++;
            InetAddress clientAddress = in.getAddress();
            int clientPort = in.getPort();
            String msg = new String(in.getData(),0, in.getLength());
            System.out.println(msg);
            System.out.println(cmp);
        }
        
        //sock.close();
    }
}