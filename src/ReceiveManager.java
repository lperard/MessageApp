import java.net.*;
import java.net.DatagramSocket;
import java.io.*;

// A ENLEVER C EST POUR LES TESTS
import java.util.ArrayList;


// ATTENTION ! POUR QUE LE RECEIVEMANAGER AIT UN ACCES AUX METHODES DU BDDMANAGER,
// IL VA FALLOIR FAIRE EN SORTE DE REFERENCER LE BDDMANAGER DU CONTROLEUR ET DE LE PASSER
// EN ARGUMENT DU CONSTRUCTEUR DU RECEIVELMANAGER (PAS DE NEW !). AINSI ON A UN PEU COMME UN POINTEUR SUR
// LE BDDMANAGER !
// ATTENTION ENCORE ! ATTENTION A L'ACCES CONCURRENTIEL AU NIVEAU DU BDDMANAGER, IL NE FAUDRAIT PAS QUE LE MAIN
// CONTROLLER ET QUE LE RECEIVEMANAGER ESSAYE D'ACCEDER EN MEME TEMPS AUX DONNEES DU BDDMANAGER !

public class ReceiveManager implements Runnable{

        private DatagramSocket sock;
        protected BddManager model;

        private int port;


        public ReceiveManager (int port, BddManager model) {
            this.model = model;
            try{this.sock = new DatagramSocket(port);}
            catch (SocketException e) {
                System.err.println(e.getClass().getName()+":"+e.getMessage());
                System.exit(0);
            }
            this.port = port;
        }

        public void processReceivedDataMsg(Message msg) {
            InetAddress source = msg.getSource();
            InetAddress dest = msg.getDest();
            byte[] data = msg.getData();
            String timestamp = msg.getTimestamp();
            this.model.addMessage(source,dest,data,timestamp);

            // A ENLEVER C EST JUSTE POUR LES TESTS
            Log log = this.model.getMsgHistory(model.getLocalUser().getIp());
            ArrayList<Message> history = log.getHistory();
            System.out.println("Contenu de la table LOG_"+ model.getLocalUser().getIp().getHostAddress());
            System.out.println("");
            for(int i=0; i<history.size() ; i++) {

                System.out.println("SOURCE = " + history.get(i).getSource().getHostAddress());
                System.out.println("DEST = " + history.get(i).getDest().getHostAddress());
                String data_str = new String(history.get(i).getData());
                System.out.println("DATA = " + data_str);
                System.out.println("TIME = " + history.get(i).getTimestamp());
                System.out.println("");
            }
        }

        public void processReceivedDataSys(MessageSys msys) {
            Type type = msys.getType();
            User user = msys.getUser();
            if(type.equals(Type.Hello)) {
                System.out.println("J'ai reçu un Hello !");
                this.model.addUser(user);
            }
            else if(type.equals(Type.Goodbye)) {
                System.out.println("J'ai reçu un Goodbye !");
                this.model.rmUser(user);
            }
            else if(type.equals(Type.ChangePseudo)) {
                System.out.println("J'ai reçu un ChangePseudo !");
                this.model.rmUser(user);
                this.model.addUser(user);
            }

            // A ENLEVER C EST JUSTE POUR LES TESTS
            ArrayList<User> connected_users = this.model.getUserList();
            System.out.println("");
            System.out.println("Voici la liste des utilisateurs connectés :");
            for(int i=0;i<connected_users.size();i++) {
                String pseudo = connected_users.get(i).getPseudo();
                System.out.println(pseudo);
            }
            System.out.println("");

        }

        public void run() {
            System.out.println("Lancement du thread de reception");
            while(true) {
                UDPserializedReceive();
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
                System.out.println("Reception d'un objet serializé");
            	//System.out.println(o.getClass().toString());
            	if (o.getClass().toString().compareTo("class MessageSys") == 0) {
            		MessageSys message_sys_received = (MessageSys) o;
            		processReceivedDataSys(message_sys_received);
            	}
            	else if (o.getClass().toString().compareTo("class Message") == 0){
            		Message message_received = (Message) o;
                	processReceivedDataMsg(message_received);
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
}
