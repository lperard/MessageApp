import java.net.*;

public class MainController {

  protected BddManager model;
  protected SocketManager com;

  public MainController(BddManager model, InetAddress distant) {
    this.model = model;
    this.com = new SocketManager(this.model, distant);
  }

}
