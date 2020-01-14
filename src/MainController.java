import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MainController {

  private BddManager model;
  private SocketManager com;

  public MainController(BddManager model, int sendPort, int receivePort) {
    this.model = model;
    this.com = new SocketManager(this.model, sendPort, receivePort);
    MessageSys sys = new MessageSys(Type.Hello, this.model.getLocalUser());
    try {
    	this.com.getSendManager().sendBroadcast(sys);
    } catch (Exception e) {
    	System.out.println("Unknown Host Address !\n");
        System.exit(0);
    }
  }

  public BddManager getModel() {
    return this.model;
  }

  public void connect(String pseudo) {
    User local_user = model.getLocalUser();
    User new_user = new User(local_user.getIp(),pseudo);
    model.setLocalUser(new_user);
    MessageSys sys = new MessageSys(Type.Connected, this.model.getLocalUser());
    try {
    	this.com.getSendManager().sendBroadcast(sys);
    } catch (Exception e) {
    	System.out.println("Unknown Host Address !\n");
        System.exit(0);
    }
  }

  public void updatePseudo(String pseudo) {
    User local_user = model.getLocalUser();
    User new_user = new User(local_user.getIp(),pseudo);
    model.setLocalUser(new_user);
    MessageSys sys = new MessageSys(Type.ChangePseudo, this.model.getLocalUser());
    try {
    	this.com.getSendManager().sendBroadcast(sys);
    } catch (Exception e) {
    	System.out.println("Unknown Host Address !\n");
        System.exit(0);
    }
  }

  public void disconnect() {
  MessageSys sys = new MessageSys(Type.Goodbye, this.model.getLocalUser());
    try {
    	this.com.getSendManager().sendBroadcast(sys);
    } catch (Exception e) {
    	System.out.println("Unknown Host Address !\n");
        System.exit(0);
    }
    System.exit(0);
  }

  public void sendMessage(byte[] data, String dest_pseudo) {
    InetAddress ip_source = model.getLocalUser().getIp();
    InetAddress ip_dest = model.getIpFromPseudo(dest_pseudo);

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String timestamp = new String(dtf.format(now));

    if(ip_dest!=null) {
      model.addMessage(ip_source, ip_dest, data, timestamp, "text");
      Message msg = new Message(ip_source, ip_dest, data, timestamp, "text");
      com.getSendManager().TCPserializedSend(msg, ip_dest);
    }
    else {
      System.out.println("Il semblerait que le destinataire n'est pas connecté !");
    }
  }

  public void sendImage(String path, byte[] data, String dest_pseudo) {
    InetAddress ip_source = model.getLocalUser().getIp();
    InetAddress ip_dest = model.getIpFromPseudo(dest_pseudo);

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String timestamp = new String(dtf.format(now));

    if(ip_dest!=null) {
        // On sauvegarde l'image dans nos fichiers
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
        model.addMessage(ip_source, ip_dest, file.getAbsolutePath().getBytes(), timestamp, "img");
        Message msg = new Message(ip_source, ip_dest, data, timestamp, "img", path);
        com.getSendManager().TCPserializedSend(msg, ip_dest);
    }
    else {
        System.out.println("Il semblerait que le destinataire n'est pas connecté !");
    }
  }

  public SocketManager getSocketManager() {
    return this.com;
  }

}
