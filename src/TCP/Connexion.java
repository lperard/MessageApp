
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.*;

// Va servir pour les connexions TCP
public class Connexion {
	private Socket sock;

	private int sendPort;
	
	public void TCPserializeSend(Object o){
		try{
			ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
			oos.writeObject(o);
			oos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void TCPserializeReceive () {
		try {
			ObjectInputStream objectInput = new ObjectInputStream(this.sock.getInputStream());
			Object ObjReceived = objectInput.readObject();
			if (ObjReceived.getClass().toString().compareTo("class MessageSys") == 0) {
        		MessageSys message_sys_received = (MessageSys) ObjReceived;
        		System.out.println("TCP: received serialized Message Sys");
        		System.out.println(message_sys_received.toString());
        	}
        	else if (ObjReceived.getClass().toString().compareTo("class Message") == 0){
        		Message message_received = (Message) ObjReceived;
        		System.out.println("TCP: received serialized Message");
        		System.out.println(message_received.toString());
        	}
        	else {
        		System.out.println("On est dans les choux");
        	}
			} 
		catch (IOException|ClassNotFoundException e) {
			e.printStackTrace();
		}
	
	}
}


