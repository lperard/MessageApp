import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class ChatWindow extends JFrame {

    private static String LOOKANDFEEL = "GTK+";

    private JButton send_button = new JButton("Send");
    private JTextField msg_to_send = new JTextField();
    private JLabel current_msg = new JLabel();
    private JLabel current_pseudo = null;
    private JButton change_pseudo_button = new JButton("Change pseudo");


    public ChatWindow(String name/*, String current_pseudo*/) {
        super(name);
        //this.current_pseudo = new JLabel(current_pseudo);
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
    	/*DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    	LocalDateTime now = LocalDateTime.now();
    	JLabel msg_format = new JLabel(now+current_msg);


    	pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        JPanel toppane = new JPanel();
        toppane.add(Box.createRigidArea(new Dimension(0,1)));
        toppane.add(current_msg);

        //toppane.add(labelPseudo);
        //toppane.add(textPseudo);
        //textPseudo.setPreferredSize(new Dimension(150,25));
        pane.add(toppane);

        JPanel but = new JPanel();
        but.add(Box.createRigidArea(new Dimension(0,1)));
        but.add(send_button);
        pane.add(but);

        send_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });*/
    }

    public static void createAndShowGUI(/*String pseudo*/) {
        initLookAndFeel();
        JFrame.setDefaultLookAndFeelDecorated(true);
        ChatWindow frame = new ChatWindow("MessageApp"/*,pseudo*/);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(640, 480));

        frame.addComponentsToPane(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }

}
