import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class SendManager {

    private int port;
    private DatagramSocket sock;

    public SendManager(int port){
        this.port = port;
        try {this.sock = new DatagramSocket();}
        catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }
    }

    public void TCPserializedSend (Object obj, InetAddress distant) {
      try{
        Socket sock = new Socket(distant, port);
  			ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
  			oos.writeObject(obj);
  			oos.close();
        System.out.println("Envoi d'un objet sérializé via TCP");
  		}
  		catch (Exception e) {
  			e.printStackTrace();
  		}
    }

    public void UDPserializedSend (Object obj, InetAddress distant) {
    	ByteArrayOutputStream outByte = null;
    	try{
			    outByte = new ByteArrayOutputStream();
          byte[] objectSerialized = null;
          ObjectOutputStream objOut = new ObjectOutputStream(outByte);
        	objOut.writeObject(obj); //envoi de l'objet serializé
        	objectSerialized = outByte.toByteArray();
        	DatagramPacket objPacket = new DatagramPacket(objectSerialized, objectSerialized.length, distant, port);
          try{
            	sock.send(objPacket);
            	System.out.println("Envoi d'un objet serializé via UDP");
            	outByte.close();
            	objOut.close();
          } catch (IOException e) {
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
        UDPserializedSend(msgSys, InetAddress.getByName("255.255.255.255"));
        this.sock.setBroadcast(false); //Pas forcement utile
    }
}
