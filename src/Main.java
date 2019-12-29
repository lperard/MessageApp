import javax.swing.*;

public class Main {

  private static String LOOKANDFEEL = "Metal";

  public static void main(String[] args) {

    int sendPort = Integer.parseInt(args[0]);
    int receivePort = Integer.parseInt(args[1]);

    //Instanciation de notre modèle
    BddManager model = new BddManager();

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
