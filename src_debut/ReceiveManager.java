import java.net.*;
import java.net.DatagramSocket;
import java.io.*;

public class ReceiveManager implements Runnable{
        private DatagramSocket sock;
        private int port;

        public ReceiveManager (int port) {
            try{this.sock = new DatagramSocket(5000);}
            catch (SocketException e) {
                System.out.println("Ca bug");
            }
            this.port = 5000;
        }
        public void run() {
            System.out.println("Lancement du thread de reception");
            int cmp = 0;
            while(true) {
                byte[] buffer = new byte[256];
                DatagramPacket in = new DatagramPacket(buffer, buffer.length);
                try{sock.receive(in);}
                catch (IOException io) {
                    System.out.println("Ca rebug");
                }
                cmp++;
                InetAddress clientAddress = in.getAddress();
                int clientPort = in.getPort();
                String msg = new String(in.getData(),0, in.getLength());
                System.out.println(msg);
                System.out.println(cmp);
            }
        }
        
        //sock.close();
}