import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;


public class Scanner {

    public static void main (String[] args)throws IOException {
        for (int i = 1024; i < 2048; i++) {
            try {
                Socket link = new Socket("127.0.0.1", i);
                BufferedReader InputBuff = new BufferedReader(new InputStreamReader(link.getInputStream()));
                PrintWriter out = new PrintWriter(link.getOutputStream(),true);
                String str = args[0];
                out.println(str);
                String message;
                message = InputBuff.readLine();
                int cmp = message.compareTo("TROUVE");
                if (cmp == 0) {
                    System.out.println("TROUVE sur le port " + i);
                    break;
                }
                InputBuff.close();
                out.close();
                link.close();
            }
            catch (IOException e) {
                System.out.println("PAS trouve sur le port " + i);
            }
            
        }
        

        

        

        
        


    }
}