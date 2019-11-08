import java.net.ServerSocket;

public class ConnectionThread extends Thread {

    //Définition des attributs
    private ServerSocket serv;
    
    private BufferedReader InputBuff;
    
    private String buff;

    private String logConnection;

    private int connectionPort;
    public ConnectionThread (int port, String filepath){
        this.connectionPort = port;
        this.serv = new ServerSocket(connectionPort);
        this.logConnection = filepath;
        




        start();
    }
}