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
                byte[] buffer = new byte[1024]; // préparation du buffer
                DatagramPacket in = new DatagramPacket(buffer, buffer.length);
                try{sock.receive(in);}
                catch (IOException io) {
                    System.out.println("Ca rebug");
                }
                ByteArrayInputStream inStream = new ByteArrayInputStream(in.getData());
                try {	
                	ObjectInput inObj = new ObjectInputStream(inStream);
                	//Recupère l'adresse du client et le port sur leuquel le client envoi
                	/*InetAddress clientAddress = in.getAddress();
                	int clientPort = in.getPort();*/
                	MessageSys message_sys_received = (MessageSys) inObj.readObject();
                	System.out.println(message_sys_received.constructMessageSystem());
                }
                catch (IOException | ClassNotFoundException e) {
                	e.printStackTrace();
                }
                
                /*String msg = new String(in.getData(),0, in.getLength());
                System.out.println(msg);*/
            }
        }
        
        //sock.close();
}
//insa-08133