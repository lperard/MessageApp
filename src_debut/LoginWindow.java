import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginWindow extends JFrame {

    private static String LOOKANDFEEL = "GTK+";

    private JLabel labelPseudo = new JLabel("Pseudo :");
    private JButton button = new JButton("Connexion !");
    private JTextField textPseudo = new JTextField();

    public LoginWindow(String name) {
        super(name);
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

    public void addComponentsToPane(final Container pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        JPanel toppane = new JPanel();
        toppane.add(Box.createRigidArea(new Dimension(0,1)));
        toppane.add(labelPseudo);
        toppane.add(textPseudo);
        textPseudo.setPreferredSize(new Dimension(150,25));
        pane.add(toppane);
        
        JPanel but = new JPanel();
        but.add(Box.createRigidArea(new Dimension(0,1)));
        but.add(button);
        pane.add(but);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    public static void createAndShowGUI() {
        initLookAndFeel();
        JFrame.setDefaultLookAndFeelDecorated(true);
        LoginWindow frame = new LoginWindow("MessageApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 150));
        frame.setMaximumSize(new Dimension(300,150));
        frame.setLocationRelativeTo(null);
        frame.addComponentsToPane(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }

}
