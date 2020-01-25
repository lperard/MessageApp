import java.lang.*;
import java.net.*;
import java.io.*;

public class SocketManager {

  private int sendPort;
  private int receivePort;
  protected SendManager sendM;
  protected ReceiveTCP receiveTCP;
  protected ReceiveUDP receiveUDP;

  public SocketManager(BddManager model, int sendPort, int receivePort, InetAddress ip) {
    this.sendPort = sendPort;
    this.receivePort = receivePort;

    try{
        this.sendM = new SendManager(receivePort);
    }
    catch (Exception e){
        System.out.println("Unknown Host Address !\n");
        System.exit(0);
    }
    this.receiveUDP = new ReceiveUDP(receivePort,model,this.sendM);
    this.receiveTCP = new ReceiveTCP(receivePort,model,this.sendM);

    Thread udp = new Thread(receiveUDP);
    udp.start();
    Thread tcp = new Thread(receiveTCP);
    tcp.start();
  }

  public SendManager getSendManager() {
	  return this.sendM;
  }

}
