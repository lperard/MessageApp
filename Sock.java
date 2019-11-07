import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;


public class Sock {

    public static void main (String[] args)throws IOException {
        Socket link = new Socket("127.0.0.1", Server.port);

        BufferedReader InputBuff = new BufferedReader(new InputStreamReader(link.getInputStream()));
        PrintWriter out = new PrintWriter(link.getOutputStream(),true);

        
        
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        System.out.println(formatter.format(date));
        String str = args[0];
        int cmp = str.compareTo("JOUR");
        if (cmp == 0) {
            out.println(date);          // envoi d'un message
        }
        else {
            out.println(str);
        }
        //System.out.println(date);
        InputBuff.close();
        out.close();
        link.close();


    }
}