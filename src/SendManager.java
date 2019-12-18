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

    private InetAddress addr_distant;

    public SendManager(int port, InetAddress addr_distant){
        this.port = port;
        this.addr_distant = addr_distant;
        try {this.sock = new DatagramSocket();}
        catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }
    }

    public void run() {
        System.out.println("Lancement du thread d'envoi");
        Message msg = null;
        MessageSys sys1 = null;
        MessageSys sys2 = null;
        try {
            InetAddress ip = InetAddress.getLocalHost(); //Envoi à soi
            User user = new User(ip,"Test_User");
            byte[] data = "Salut !".getBytes();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = new String(dtf.format(now));
            msg = new Message(ip,ip,data,timestamp);
            sys1 = new MessageSys(Type.Hello,user);
            sys2 = new MessageSys(Type.Goodbye,user);

            boolean haventSendYet = true;
            while(haventSendYet) {
                UDPserializeSend(msg,ip);
                UDPserializeSend(sys1,ip);
                UDPserializeSend(sys2,ip);
                haventSendYet = false;
                System.out.println("J'ai envoyé mes messages !");
            }
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }
    }

    private void UDPserializeSend (Object obj, InetAddress distant) {
    	ByteArrayOutputStream outByte = null;
    	try{
			outByte = new ByteArrayOutputStream();
            byte[] objectSerialized = null;
            ObjectOutputStream objOut = new ObjectOutputStream(outByte);
        	objOut.writeObject(obj); //envoi de l'objet serializé
        	objectSerialized = outByte.toByteArray();
        	DatagramPacket objPacket = new DatagramPacket(objectSerialized, objectSerialized.length, distant, this.port);
            try{
            	sock.send(objPacket);
            	System.out.println("Envoi d'un objet serializé");
            	outByte.close();
            	objOut.close();
            }
            catch (IOException e) {
            	e.printStackTrace();
            }
        }
    	catch (UnknownHostException uhe) {
    		uhe.printStackTrace();
		}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    public void sendBroadcast (MessageSys msgSys) throws SocketException, UnknownHostException {
        this.sock.setBroadcast(true);
        UDPserializeSend(msgSys, InetAddress.getByName("255.255.255.255"));
        this.sock.setBroadcast(false); //Pas forcement utile
    }
}
