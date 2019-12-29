import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

// A ENLEVER C EST POUR LES TESTS
import java.net.InetAddress;

public class LoginWindow extends JFrame implements Observer {

    private MainController controler;
    private boolean alreadyConnected;

    private JPanel users = new JPanel();
    private JPanel login = new JPanel();

    private JLabel labelPseudo = new JLabel("Choisissez votre pseudo:");
    private JButton connect_button = new JButton("Connexion !");
    private JTextField textPseudo = new JTextField();

    private JLabel online_users = new JLabel("Utilisateurs en ligne:", SwingConstants.CENTER);
    private JScrollPane scrollPane = new JScrollPane();
    private JList<String> user_list = null;

    // POUR LES TESTS
    private JPanel test_user = new JPanel();
    private JButton add_user = new JButton("+");
    private JButton rm_user = new JButton("-");

    public LoginWindow(MainController controler, boolean alreadyConnected) {
      this.alreadyConnected = alreadyConnected;
      this.controler = controler;
      this.controler.getModel().addObserver(this);
      this.user_list = new JList<String>(this.controler.getModel().getPseudoList());

      this.showGUI();
    }

    public void addComponentsToPanes() {
        users.setLayout(new BorderLayout(10,10));
        online_users.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        user_list.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        scrollPane.setViewportView(user_list);
        user_list.setLayoutOrientation(JList.VERTICAL);
        users.add(online_users, BorderLayout.PAGE_START);
        users.add(scrollPane, BorderLayout.CENTER);
        users.add(test_user, BorderLayout.PAGE_END); // A ENLEVER, TEST
        users.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // POUR LES TESTS
        test_user.setLayout(new BoxLayout(test_user, BoxLayout.LINE_AXIS));
        test_user.add(Box.createRigidArea(new Dimension(10,0)));
        test_user.add(add_user);
        test_user.add(Box.createRigidArea(new Dimension(10,0)));
        test_user.add(rm_user);
        test_user.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        // POUR LES TESTS
        add_user.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              try {
                InetAddress address = InetAddress.getByName("190.168.120.200");
                controler.getModel().addUser(new User(address,"Test User"));
              } catch (Exception ex) {
                System.err.println(ex.getClass().getName()+":"+ex.getMessage());
                System.exit(0);
              }
            }
        });

        // POUR LES TESTS
        rm_user.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              InetAddress address = InetAddress.getByName("190.168.120.200");
              controler.getModel().rmUser(new User(address,"Test User"));
            } catch (Exception ex) {
              System.err.println(ex.getClass().getName()+":"+ex.getMessage());
              System.exit(0);
            }
          }
        });

        login.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        labelPseudo.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        login.add(labelPseudo, c);

        c.insets = new Insets(10,0,0,0);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        login.add(textPseudo, c);

        c.insets = new Insets(20,0,0,0);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        login.add(connect_button, c);

        login.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        connect_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              choosePseudo();
            }
        });

        textPseudo.addKeyListener(new CustomKeyListener());
    }

    private class CustomKeyListener implements KeyListener {
        public void keyTyped(KeyEvent e) {
        }
        public void keyPressed(KeyEvent e) {
           if(e.getKeyCode() == KeyEvent.VK_ENTER){
             choosePseudo();
           }
        }
        public void keyReleased(KeyEvent e) {
        }
     }

     public void choosePseudo() {
      String pseudo = textPseudo.getText();
      if(!pseudo.equals("")) {
        if(pseudo.length()>16) {
          JOptionPane.showMessageDialog(null,"Ce pseudo est trop long (16 caractères maximum !)");
        }
        else {
          String[] pseudo_list = controler.getModel().getPseudoList();
          boolean pseudoAlreadyTaken = false;
          for(int i=0; i<pseudo_list.length; i++) {
            if(pseudo_list[i].equals(pseudo)) {
              pseudoAlreadyTaken = true;
            }
          }
          if(!pseudoAlreadyTaken) {
              if(!alreadyConnected)
                controler.connect(pseudo);
              else
                controler.updatePseudo(pseudo);
              controler.getModel().removeObserver();
              dispose();
              ChatWindow chat = new ChatWindow(controler, pseudo);
          }
          else {
            JOptionPane.showMessageDialog(null,"Ce pseudo n'est pas disponible pour le moment. Veuillez en choisir un autre !");
          }
        }
      }
      else {
        JOptionPane.showMessageDialog(null,"Vous devez choisir un pseudo non vide !");
      }
    }

    public void showGUI() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
          @Override
          public void windowClosing(java.awt.event.WindowEvent e) {
            controler.disconnect();
            e.getWindow().dispose();
          }
        });

        this.setTitle("MessageApp");
        this.setSize(new Dimension(500, 200));
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.setLayout(new BorderLayout(10,10));
        users.setPreferredSize(new Dimension(200, 200));
        login.setPreferredSize(new Dimension(300, 200));
        this.add(users, BorderLayout.LINE_START);
        this.add(login, BorderLayout.LINE_END);

        this.addComponentsToPanes();

        this.pack();
        this.setVisible(true);
    }

    // Implémentation du pattern observer
    public void update(String str) {
      System.out.println(str);
      if(str.equals("new_user_online") || str.equals("new_user_offline")) {
        user_list.setListData(controler.getModel().getPseudoList());
      }
    }

}
