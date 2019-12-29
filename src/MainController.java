import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.net.*;

public class MainController {

  private BddManager model;
  private SocketManager com;

  public MainController(BddManager model, int sendPort, int receivePort) {
    this.model = model;
    this.com = new SocketManager(this.model, sendPort, receivePort);
  }

  public BddManager getModel() {
    return this.model;
  }

  public void connect(String pseudo) {
    User local_user = model.getLocalUser();
    User new_user = new User(local_user.getIp(),pseudo);
    model.setLocalUser(new_user);
    // PUIS SEND HELLO BROADCAST !
  }

  public void updatePseudo(String pseudo) {
    User local_user = model.getLocalUser();
    User new_user = new User(local_user.getIp(),pseudo);
    model.setLocalUser(new_user);
    // PUIS SEND CHANGEPSEUDO BROADCAST !
  }

  public void disconnect() {
    // SEND GOODBYE BROADCAST !
    System.exit(0);
  }

  public void sendMessage(byte[] data, String dest_pseudo) {
    InetAddress ip_source = model.getLocalUser().getIp();
    InetAddress ip_dest = model.getIpFromPseudo(dest_pseudo);

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String timestamp = new String(dtf.format(now));

    if(ip_dest!=null) {
      model.addMessage(ip_source, ip_dest, data, timestamp);
      // PUIS SEND LE MESSAGE VIA SOCKET !
    }
    else {
      System.out.println("Il semblerait que le destinataire n'est pas connecté !");
    }
  }

}
