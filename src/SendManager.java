import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class SendManager {

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

    public void UDPserializeSend (Object obj, InetAddress distant) {
    	ByteArrayOutputStream outByte = null;
    	try{
			    outByte = new ByteArrayOutputStream();
          byte[] objectSerialized = null;
          ObjectOutputStream objOut = new ObjectOutputStream(outByte);
        	objOut.writeObject(obj); //envoi de l'objet serializé
        	objectSerialized = outByte.toByteArray();
        	DatagramPacket objPacket = new DatagramPacket(objectSerialized, objectSerialized.length, distant, 6000);
          try{
            	sock.send(objPacket);
            	System.out.println("Envoi d'un objet serializé");
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
        UDPserializeSend(msgSys, InetAddress.getByName("255.255.255.255"));
        this.sock.setBroadcast(false); //Pas forcement utile
    }
}
