package Controller;
import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Model.BddManager;
import Model.User;

public class ReceiveUDP implements Runnable {
  private DatagramSocket sock;
  protected BddManager model;
  protected SendManager sendM;
  private int port;

  public ReceiveUDP (int port, BddManager model, SendManager sendM) {
      this.model = model;
      this.port = port;
      this.sendM = sendM;
      try{this.sock = new DatagramSocket(this.port);}
      catch (SocketException e) {
          System.err.println(e.getClass().getName()+":"+e.getMessage());
          System.exit(0);
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
          this.model.newPseudo(user);
      }
  }

  public void run() {
      System.out.println("Lancement du thread de reception UDP");
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
            if (o.getClass().toString().compareTo("class MessageSys") == 0) {
                MessageSys message_sys_received = (MessageSys) o;
                System.out.println("UDP: received serialized Message Sys");
                processReceivedDataSys(message_sys_received);
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
