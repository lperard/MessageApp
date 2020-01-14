import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ReceiveTCP implements Runnable {

  protected BddManager model;
  protected SendManager sendM;
  private int port;
  private ServerSocket servsock;

  public ReceiveTCP (int port, BddManager model, SendManager sendM) {
    this.port = port;
    this.model = model;
    this.sendM = sendM;
    try {
      servsock = new ServerSocket(this.port);
    } catch (Exception e) {
      e.printStackTrace();
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
      else if(filetype.equals("img")) {
        // On sauvegarde l'image dans nos fichiers
        String path = msg.getFilepath();
        int index = path.lastIndexOf("/");
        if(index == -1) {
          index = path.lastIndexOf("\\");
        }
        String filename = path.substring(index + 1);
        File file = new File("tmp/"+filename);
        try {
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // On récupère l'extension du fichier
            index = filename.lastIndexOf(".");
            String file_extension = filename.substring(index + 1);
            System.out.println("extension : "+file_extension);

            ByteArrayInputStream inStream = new ByteArrayInputStream(data);
            BufferedImage bImg = ImageIO.read(inStream);
            ImageIO.write(bImg, file_extension, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // On ajoute à la bdd un message avec le path du fichier
        this.model.addMessage(source,dest,file.getAbsolutePath().getBytes(),timestamp,filetype);
      }
      else if(filetype.equals("file")) {
        // On sauvegarde l'image dans nos fichiers
        String path = msg.getFilepath();
        int index = path.lastIndexOf("/");
        if(index == -1) {
          index = path.lastIndexOf("\\");
        }
        String filename = path.substring(index + 1);
        File file = new File("tmp/"+filename);
        try {
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            OutputStream os = new FileOutputStream(file);
            os.write(data);
            os.close();
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
              this.sendM.TCPserializedSend(sys, user.getIp());
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
    System.out.println("Lancement du thread de reception TCP");
    while(true) {
        TCPserializedReceive();
    }
  }

  public void TCPserializedReceive() {
    try {
        Socket sock = this.servsock.accept();
		ObjectInputStream objectInput = new ObjectInputStream(sock.getInputStream());
		Object ObjReceived = objectInput.readObject();
		if (ObjReceived.getClass().toString().compareTo("class MessageSys") == 0) {
    		MessageSys message_sys_received = (MessageSys) ObjReceived;
    		System.out.println("TCP: received serialized Message Sys");
    		processReceivedDataSys(message_sys_received);
    	}
    	else if (ObjReceived.getClass().toString().compareTo("class Message") == 0){
    		Message message_received = (Message) ObjReceived;
    		System.out.println("TCP: received serialized Message");
    		processReceivedDataMsg(message_received);
    	}
    	else {
    		System.out.println("Unrecognized serialization");
    	}
	}
	catch (IOException|ClassNotFoundException e) {
		e.printStackTrace();
	}
  }

}
