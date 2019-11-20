import java.net.*;
import java.util.*;
import java.io.*;

public class ConnectionManager{

    private static int connectionPort = 5000;

    private HashMap<Integer, Sock> socketHashMap;
    public ConnectionManager (){
        this.socketHashMap = new HashMap<Integer, Sock>();
    }

    public static void main (String[] args) throws UnknownHostException,IOException{
        int lastConnected = 0;
        Sock newSock = new Sock(new Socket("127.0.0.1",connectionPort), connectionPort);
        connectionPort++;
        socketHashMap.put(lastConnected,newSock);
        while (true) {
            if (socketHashMap.get(lastConnected).connected == true){
            lastConnected++;            
            socketHashMap.put(lastConnected,new Sock(new Socket("127.0.0.1",connectionPort), connectionPort));
            connectionPort++;
            }
        }
        
    }

    
}