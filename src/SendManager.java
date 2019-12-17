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
        MessageSys msg_sys = null;
        try {
            InetAddress ip = InetAddress.getLocalHost(); //Envoi à soi même
            User user = new User(ip, "Didiax");
            msg_sys = new MessageSys(Type.Hello,user);
            //String message = msg_sys.constructMessageSystem();        
            //System.out.println(message);
        }
        catch (UnknownHostException e) {
            System.out.println ("Erreur dans la récupération du nom d'hôte");
        }
        Scanner input = new Scanner(System.in);
        ByteArrayOutputStream outByte = null;
        while(true) {
            //String msg = input.nextLine();
        	try{
        		InetAddress nous = InetAddress.getByName("10.1.5.99");
        		UDPserializeSend(msg_sys, nous);
        	}
        	catch (UnknownHostException e) {
        		System.out.println("ba mince");
        	}
        	
        }
        
        
        //sock.close();
        //input.close()
    }

    private void UDPserializeSend (Object obj, InetAddress distant) {
    	ByteArrayOutputStream outByte = null;
    	try{ 
			outByte = new ByteArrayOutputStream();
            //DatagramPacket out = new DatagramPacket(msg.getBytes(), msg.length(), host, port);
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
                System.out.println("Ca rebug");
            }
        }
    	catch (UnknownHostException uhe) {
    		uhe.printStackTrace();
			System.out.println(uhe.toString());
		}
    	catch (IOException e) {
    		e.printStackTrace();
			System.out.println(e.toString());
    	}
    }
    public void sendBroadcast (MessageSys msgSys) {
        /*DatagramSocket BroadcastSocket = new DatagramSocket();
        BroadcastSocket.setBroadcast(true);
        String message_sys = new String(constructMessageSystem());
        DatagramPacket message_broadcasted = new DatagramPacket();*/
    }
}