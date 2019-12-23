import java.io.IOException;
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
}
