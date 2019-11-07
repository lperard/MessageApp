import java.net.*;
import java.io.*;


public class Server  {
    static int port = 1998;
    public static void main (String[] args) throws IOException{
        ServerSocket serv = new ServerSocket(port);
        BufferedReader InputBuff;
        PrintWriter out;
        String message = "TROUVE";
        String buff;

        while (true) {
            Socket sock = serv.accept();
            InputBuff = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(),true);

            buff = InputBuff.readLine();
            if (buff != null) {
                out.println(message);
            }
            int cmp = buff.compareTo("END");
            if (cmp == 0) {
                System.out.println("COCUOU");
                sock.close();
                out.close();
                InputBuff.close();
                break;
            }
                
        /*InputBuff.close();
        out.close();
        sock.close();*/
            
        }
        
    }
    
       
}
