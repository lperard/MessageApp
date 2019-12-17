import java.util.ArrayList;
import java.net.*;
import java.sql.*;

public class TestMatthieu {    
    public static void main(String [] args) {
        BddManager bdd = new BddManager();

        try {
        
            byte[] data = "hello world".getBytes();
            InetAddress me = InetAddress.getLocalHost();
            InetAddress you = InetAddress.getByName("java.sun.com");
            String timestamp = "14:30:15";
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
