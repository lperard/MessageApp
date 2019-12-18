public class MainController {

  protected BddManager model;
  protected SocketManager com;

  public MainController(BddManager model, int sendPort, int receivePort) {
    this.model = model;
    this.com = new SocketManager(this.model, sendPort, receivePort);
  }

}
