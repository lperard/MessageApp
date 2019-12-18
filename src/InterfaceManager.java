import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InterfaceManager implements Observer {

    /*public void displayLoginWindow() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginWindow.createAndShowGUI();            
            }
        });
    }*/

    public void displayChatConnection() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChatWindow.createAndShowGUI();
            }
         });
    }
    
    // Implémentation du pattern observer
    public void update(String str) {
    }

    public static void main(String[] args) {
        InterfaceManager UI = new InterfaceManager();        
        UI.displayChatConnection();
    }
    
}
