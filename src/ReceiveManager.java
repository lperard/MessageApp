import java.net.*;
import java.net.DatagramSocket;
import java.io.*;

public class ReceiveManager implements Runnable{

        private DatagramSocket sock;
        protected BddManager model;
        protected SendManager sendM;

        private int port;

        public ReceiveManager (int port, BddManager model, SendManager sendM) {
            this.model = model;
            this.port = port;
            this.sendM = sendM;
            try{this.sock = new DatagramSocket(this.port);}
            catch (SocketException e) {
                System.err.println(e.getClass().getName()+":"+e.getMessage());
                System.exit(0);
            }
        }

        public void processReceivedDataMsg(Message msg) {
            InetAddress source = msg.getSource();
            InetAddress dest = msg.getDest();
            byte[] data = msg.getData();
            String timestamp = msg.getTimestamp();
            String filetype = msg.getFiletype();
            if(filetype.equals("text"))
              this.model.addMessage(source,dest,data,timestamp,filetype);
            else {
              // On sauvegarde l'image dans nos fichiers
              String path = msg.getFilepath();
              int index = path.lastIndexOf("/");
              if(index == -1) {
                index = path.lastIndexOf("\\");
              }
              String filename = path.substring(index + 1);
              File file = new File(filename);
              try {
                file.createNewFile();
              } catch (Exception e) {
                e.printStackTrace();
              }
              // On ajoute à la bdd un message avec le path du fichier
              this.model.addMessage(source,dest,file.getAbsolutePath().getBytes(),timestamp,filetype);
            }
        }

        public void processReceivedDataSys(MessageSys msys) {
            Type type = msys.getType();
            User user = msys.getUser();
            if(type.equals(Type.Hello)) {
                System.out.println("J'ai reçu un Hello !");
                if(!this.model.getLocalUser().getPseudo().equals("")) {
                    MessageSys sys = new MessageSys(Type.Connected, this.model.getLocalUser());
                    this.sendM.UDPserializeSend(sys, user.getIp());
                }
            }
            else if(type.equals(Type.Connected)) {
            	System.out.println("J'ai reçu un Connected !");
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
                System.out.println("Erreur au niveau du socket de réception !");
            }

            if(!in.getAddress().equals(this.model.getLocalUser().getIp())) {

                ByteArrayInputStream inStream = new ByteArrayInputStream(in.getData());
                try {
                	ObjectInput inObj = new ObjectInputStream(inStream);
                	Object o = inObj.readObject();
                    System.out.println("Reception d'un objet serializé");
                	if (o.getClass().toString().compareTo("class MessageSys") == 0) {
                  		MessageSys message_sys_received = (MessageSys) o;
                  		processReceivedDataSys(message_sys_received);
                	}
                	else if (o.getClass().toString().compareTo("class Message") == 0){
                		  Message message_received = (Message) o;
                    	processReceivedDataMsg(message_received);
                	}
                	else {
                		System.out.println("Serialization inconnue !");
                	}

                } catch (IOException | ClassNotFoundException e) {
                	e.printStackTrace();
                }
            }
      }
}
