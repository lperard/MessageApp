import java.lang.*;
import java.net.*;
import java.io.*;

public class SocketManager {

  private int sendPort;
  private int receivePort;
  protected SendManager sendM;
  protected ReceiveManager receiveM;

  public SocketManager(BddManager model, int sendPort, int receivePort) {
    this.sendPort=sendPort;
    this.receivePort=receivePort;

    this.sendM=new SendManager(sendPort);
    this.receiveM=new ReceiveManager(receivePort,model);

    Thread send = new Thread(sendM);
    send.start();
    Thread receive = new Thread(receiveM);
    receive.start();
    System.out.println("On a lancé les threads");
  }

}
