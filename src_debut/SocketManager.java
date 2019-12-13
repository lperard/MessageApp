import java.lang.*;
import java.net.*;
import java.io.*;
public class SocketManager {

     public static void main (String[] args) throws SocketException, UnknownHostException, IOException {
        int sendPort = Integer.parseInt(args[0]);
        int receivePort = Integer.parseInt(args[1]);
        SendManager sendM = new SendManager(sendPort);
        Thread send = new Thread(sendM);
        send.start();
        ReceiveManager receiveM = new ReceiveManager(receivePort);
        Thread receive = new Thread(receiveM);
        receive.start();
        System.out.println("On a lancé les threads");
    }

}