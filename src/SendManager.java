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
    
    private InetAddress addr_distant;

    public SendManager(int port, InetAddress addr_distant){
        this.port = port;
        this.addr_distant = addr_distant;
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
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Scanner input = new Scanner(System.in);
        while(true) {
        	try{
        		Thread.sleep(1000);
        		//UDPserializeSend(msg_sys, this.addr_distant);
        		sendBroadcast(msg_sys);
        	}
        	catch (InterruptedException e) {
        		e.printStackTrace();
        	} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
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