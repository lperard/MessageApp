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
            ResultSet rs = bdd.getMsgHistory(you);

            while(rs.next()) {
                String source = rs.getString("source");
                String dest = rs.getString("dest");
                String data_str = rs.getString("data");
                String timestamp_str = rs.getString("timestamp");

                System.out.println("SOURCE = "+source);
                System.out.println("DEST = "+dest);
                System.out.println("DATA = "+data_str);
                System.out.println("TIMESTAMP = "+timestamp_str);
                System.out.println();
            }
    
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }
    }
}
