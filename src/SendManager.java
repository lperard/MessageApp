import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;

// A ENLEVER A TERME C EST POUR LES TESTS
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

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
        Message msg = null;
        try {
            InetAddress ip = InetAddress.getLocalHost(); //Envoi à soi
            byte[] data = "Salut !".getBytes();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = new String(dtf.format(now));
            msg = new Message(ip,ip,data,timestamp);
            //String message = msg_sys.constructMessageSystem();
            //System.out.println(message);
        }
        catch (UnknownHostException e) {
            System.out.println ("Erreur dans la récupération du nom d'hôte");
        }
        Scanner input = new Scanner(System.in);
        ByteArrayOutputStream outByte = null;

        boolean haventSendYet = true;
        while(haventSendYet) {
            //String msg = input.nextLine();
            try{
              InetAddress host = InetAddress.getLocalHost();
    			    outByte = new ByteArrayOutputStream();
              //DatagramPacket out = new DatagramPacket(msg.getBytes(), msg.length(), host, port);
              byte[] objectSerialized = null;
              ObjectOutputStream objOut = new ObjectOutputStream(outByte);
            	objOut.writeObject(msg); //envoi de l'objet serializé
            	objectSerialized = outByte.toByteArray();
            	DatagramPacket objPacket = new DatagramPacket(objectSerialized, objectSerialized.length, host, this.port);
              try{sock.send(objPacket); haventSendYet = false; System.out.println("J'ai envoyé un message !");}
              catch (IOException io) {
                System.out.println("Ca rebug");
              }
            }
            //https://www.javaworld.com/article/2077539/java-tip-40--object-transport-via-datagram-packets.html
            catch (UnknownHostException uhe) {
                System.out.println("Oula");
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
