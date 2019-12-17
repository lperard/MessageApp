import java.net.*;
import java.net.DatagramSocket;
import java.io.*;


// ATTENTION ! POUR QUE LE RECEIVEMANAGER AIT UN ACCES AUX METHODES DU BDDMANAGER,
// IL VA FALLOIR FAIRE EN SORTE DE REFERENCER LE BDDMANAGER DU CONTROLEUR ET DE LE PASSER
// EN ARGUMENT DU CONSTRUCTEUR DU RECEIVELMANAGER (PAS DE NEW !). AINSI ON A UN PEU COMME UN POINTEUR SUR
// LE BDDMANAGER !
// ATTENTION ENCORE ! ATTENTION A L'ACCES CONCURRENTIEL AU NIVEAU DU BDDMANAGER, IL NE FAUDRAIT PAS QUE LE MAIN
// CONTROLLER ET QUE LE RECEIVEMANAGER ESSAYE D'ACCEDER EN MEME TEMPS AUX DONNEES DU BDDMANAGER !

public class ReceiveManager implements Runnable{
        
        private DatagramSocket sock;
        
        private int port;

        /* Constructeur */
        public ReceiveManager (int port) {
            try{this.sock = new DatagramSocket(port);}
            catch (SocketException e) {
                System.out.println("Ca bug");
            }
            this.port = port;
        }
        

        public void run() {
            System.out.println("Lancement du thread de reception");
            while(true) {
                UDPserializedReceive();
                
                /*String msg = new String(in.getData(),0, in.getLength());
                System.out.println(msg);*/
            }
        }
        public void UDPserializedReceive () {
        	byte[] buffer = new byte[1024]; // préparation du buffer
            DatagramPacket in = new DatagramPacket(buffer, buffer.length);
            try{
            	this.sock.receive(in);
            }
            catch (IOException io) {
                System.out.println("Ca rebug");
            }
            ByteArrayInputStream inStream = new ByteArrayInputStream(in.getData());
            try {
            	ObjectInput inObj = new ObjectInputStream(inStream);
            	//Recupère l'adresse du client et le port sur leuquel le client envoi
            	/*InetAddress clientAddress = in.getAddress();
            	int clientPort = in.getPort();*/
            	Object o = inObj.readObject();
            	//System.out.println(o.getClass().toString());
            	if (o.getClass().toString().compareTo("class MessageSys") == 0) {
            		System.out.println("Reception d'un objet serializé");
            		MessageSys message_sys_received = (MessageSys) o;
            		//PROCESSRECEIVDATASYS
            	}
            	else if (o.getClass().toString().compareTo("class Message") == 0){
            		System.out.println("Well");
            		Message message_received = (Message) o;
            		//PROCESSRECEIVDATAMSG
            	}
            	else {
            		System.out.println("On est dans les choux");
            	}
            	
            	//System.out.println(message_sys_received.getType().toString());
            	//System.out.println(message_sys_received.constructMessageSystem());
            }
            catch (IOException | ClassNotFoundException e) {
            	e.printStackTrace();
            	System.out.println("Ca marche pas");
            }
        }
        
        //sock.close();
}
//insa-08133