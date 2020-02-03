import javax.swing.*;

import Controller.MainController;
import Model.BddManager;
import View.LoginWindow;

import java.net.*;
import java.io.*;

public class Main {

    private static String LOOKANDFEEL = "GTK+";

    public static void main(String[] args) {
    int sendPort = 5000;
    int receivePort = 6000;

      //Récupération de notre IP sur le réseau local
      InetAddress my_ip = null;
      try {
          DatagramSocket sock = new DatagramSocket(8888);
          sock.connect(new InetSocketAddress("8.8.8.8", 8888));
          my_ip = sock.getLocalAddress();
          sock.disconnect();
      } catch (Exception e) {
          System.err.println(e.getClass().getName()+":"+e.getMessage());
          System.exit(0);
      }

      // Ici on cherche à déterminer notre adresse MAC
      StringBuilder sb = new StringBuilder();
      try {
          NetworkInterface network = NetworkInterface.getByInetAddress(my_ip);
          byte[] mac = network.getHardwareAddress();
          for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
          }
      } catch (Exception e) {
         System.err.println(e.getClass().getName()+":"+e.getMessage());
         System.exit(0);
      }
      String my_mac = sb.toString();

      //Instanciation de notre modèle"
      BddManager model = new BddManager(my_mac,my_ip);

      //Création du contrôleur
      MainController controler = new MainController(model,sendPort,receivePort);

      //Création de notre fenêtre de login avec le contrôleur en paramètre
      initLookAndFeel();
      JFrame.setDefaultLookAndFeelDecorated(true);
      LoginWindow view = new LoginWindow(controler,false);
  }

  private static void initLookAndFeel() {
      String lookAndFeel = null;

      if(LOOKANDFEEL != null) {
          if(LOOKANDFEEL.equals("Metal")) {
              lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
          }
          else if (LOOKANDFEEL.equals("System")) {
              lookAndFeel = UIManager.getSystemLookAndFeelClassName();
          }
          else if (LOOKANDFEEL.equals("Motif")) {
              lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
          }
          else if (LOOKANDFEEL.equals("GTK+")) {
              lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
          }
          else {
              System.err.println("Unexpected value of LOOKANDFEEL specified: " + LOOKANDFEEL);
              lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
          }

          try {
              UIManager.setLookAndFeel(lookAndFeel);
          } catch (ClassNotFoundException e) {
              System.err.println("Couldn't find class for specified look and feel:"
                      + lookAndFeel);
              System.err.println("Did you include the L&F library in the class path?");
              System.err.println("Using the default look and feel.");
          } catch (UnsupportedLookAndFeelException e) {
              System.err.println("Can't use the specified look and feel ("
                      + lookAndFeel
                      + ") on this platform.");
              System.err.println("Using the default look and feel.");
          } catch (Exception e) {
              System.err.println("Couldn't get specified look and feel ("
                      + lookAndFeel
                      + "), for some reason.");
              System.err.println("Using the default look and feel.");
              e.printStackTrace();
          }
      }
  }
}
