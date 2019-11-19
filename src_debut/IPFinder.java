// File: IPFinder.java
// Get the IP address of a host
import java.net.*;
import java.io.*;
import javax.swing.*;
public class IPFinder {
    
    public static String getInetAddress() throws IOException{
        String host;
        host = JOptionPane.showInputDialog("Please input the server's name");
        try{
            InetAddress address = InetAddress.getByName(host);
            JOptionPane.showMessageDialog(null,"IPaddress: " + address.toString());
        }
        catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null,"Couldnot find " + host);
        }
        return host;
    }
}