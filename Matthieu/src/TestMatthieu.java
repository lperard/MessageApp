import java.util.ArrayList;
import java.net.*;
import java.sql.*;

// A ENLEVER A TERME C EST POUR LES TESTS
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class TestMatthieu {
    public static void main(String [] args) {
        BddManager bdd = new BddManager();

        try {

            byte[] data = "hello world".getBytes();
            InetAddress me = InetAddress.getLocalHost();
            InetAddress you = InetAddress.getByName("java.sun.com");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = new String(dtf.format(now));
            bdd.addMessage(me,you,data,timestamp);
            Log log = bdd.getMsgHistory(you);
            ArrayList<Message> history = log.getHistory();
            for(int i=0; i<history.size() ; i++) {

                System.out.println("SOURCE = " + history.get(i).getSource().getHostAddress());
                System.out.println("DEST = " + history.get(i).getDest().getHostAddress());
                String data_str = new String(history.get(i).getData());
                System.out.println("DATA = " + data_str);
                System.out.println("TIME = " + history.get(i).getTimestamp());
                System.out.println("");
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }
    }
}
