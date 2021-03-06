import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.filechooser.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;

public class ChatWindow extends JFrame implements Observer {

    private MainController controler;

    private JPanel users = new JPanel();
    private JPanel chat = new JPanel();

    private JPanel pseudoPane = new JPanel();
    private JLabel current_pseudo = null;
    private JButton change_pseudo_button = new JButton("Changer de pseudo");

    private JPanel listPane = new JPanel();
    private JLabel online_users = new JLabel("Utilisateurs en ligne:", SwingConstants.CENTER);
    private JScrollPane scrollPane = new JScrollPane();
    private JList<String> user_list = null;

    private JTabbedPane history = new JTabbedPane();

    private File file = null;

    public ChatWindow(MainController controler, String current_pseudo) {
        this.controler = controler;
        this.controler.getModel().addObserver(this);

        this.user_list = new JList<String>(this.controler.getModel().getPseudoList());
        this.user_list.setCellRenderer(new UserListLabel(this.controler.getModel()));
        this.current_pseudo = new JLabel("Connecté en tant que "+current_pseudo, SwingConstants.CENTER);
        this.history.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        this.showGUI();
    }

    public void addComponentsToPanes() {
      users.setLayout(new BorderLayout(20,20));
      users.add(pseudoPane, BorderLayout.PAGE_START);
      users.add(listPane, BorderLayout.CENTER);
      users.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

      pseudoPane.setLayout(new BorderLayout(10,10));
      current_pseudo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      pseudoPane.add(current_pseudo, BorderLayout.PAGE_START);
      pseudoPane.add(change_pseudo_button, BorderLayout.CENTER);

      listPane.setLayout(new BorderLayout(10,10));
      online_users.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      listPane.add(online_users, BorderLayout.PAGE_START);
      listPane.add(scrollPane, BorderLayout.CENTER);

      scrollPane.setViewportView(user_list);
      user_list.setLayoutOrientation(JList.VERTICAL);
      user_list.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

      user_list.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          if(user_list.getModel().getSize()!=0) {
            String selectedItem = (String) user_list.getSelectedValue();
            int tabExists = -1;
            for(int i = 0; i<history.getTabCount(); i++) {
              String tabTitle = history.getTitleAt(i).replace(" *","");
              if(tabTitle.equals(selectedItem)) {
                tabExists = i;
              }
            }
            if(tabExists==-1) {
              InetAddress tab_ip = controler.getModel().getIpFromPseudo(selectedItem);
              String tab_mac = controler.getModel().getMacFromIp(tab_ip);
              String pseudo = controler.getModel().getPseudoFromMac(tab_mac);
              UserTabPane historyPane = new UserTabPane(history,controler,tab_ip,tab_mac, pseudo);
              history.addTab(selectedItem,historyPane);
              history.setTabComponentAt(history.getTabCount()-1,new ButtonTabComponent(history));
              history.setSelectedIndex(history.getTabCount()-1);
            }
            else {
                history.setSelectedIndex(tabExists);
                // On enlève le * de notification
                if(history.getTitleAt(tabExists).contains(" *")) {
                    history.setTitleAt(tabExists, history.getTitleAt(tabExists).replace(" *",""));
                }
            }
          }
        }
      });

      change_pseudo_button.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            controler.getModel().removeObserver();
            dispose();
            LoginWindow login = new LoginWindow(controler,true);
          }
      });

      chat.setLayout(new BoxLayout(chat, BoxLayout.PAGE_AXIS));
      chat.add(history);

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
        this.setSize(new Dimension(1000, 500));
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.setLayout(new BorderLayout());
        users.setPreferredSize(new Dimension(300, 500));
        chat.setPreferredSize(new Dimension(700, 500));
        this.add(users, BorderLayout.LINE_START);
        this.add(chat, BorderLayout.LINE_END);

        this.addComponentsToPanes();

        this.pack();
        this.setVisible(true);
    }

    // Implémentation du pattern observer
    public void update(String str) {
      System.out.println(str);
      if(str.equals("new_user_online")) {
        user_list.setListData(controler.getModel().getPseudoList());
      }
      else if(str.contains("new_user_offline_")) {
        user_list.setListData(controler.getModel().getPseudoList());
        String tmp = str.replace("new_user_offline_","");
        try {
          InetAddress tmp_address = InetAddress.getByName(tmp);
          for(int i=0; i<history.getTabCount(); i++) {
            UserTabPane tab = (UserTabPane) history.getComponentAt(i);
            if(tab.getUserIp().getHostAddress().equals(tmp)) {
              history.remove(i);
            }
          }
        }
        catch (Exception e) {
          System.err.println(e.getClass().getName()+":"+e.getMessage());
          System.exit(0);
        }
      }
      else if(str.contains("new_pseudo_")) {
        user_list.setListData(controler.getModel().getPseudoList());
        String tmp = str.replace("new_pseudo_","");
        try {
          InetAddress tmp_address = InetAddress.getByName(tmp);
          for(int i=0; i<history.getTabCount(); i++) {
            UserTabPane tab = (UserTabPane) history.getComponentAt(i);
            if(tab.getUserIp().getHostAddress().equals(tmp)) {
              String new_pseudo = controler.getModel().getPseudoFromIp(tab.getUserIp());
              history.setTitleAt(i,new_pseudo);
              String mac = controler.getModel().getMacFromIp(tmp_address);
              String pseudo = controler.getModel().getPseudoFromMac(mac);
              UserTabPane new_tab = new UserTabPane(history, controler, tmp_address, mac, pseudo);
              history.setComponentAt(i,new_tab);
            }
          }
        }
        catch (Exception e) {
          System.err.println(e.getClass().getName()+":"+e.getMessage());
          System.exit(0);
        }
      }
      else if(str.contains("new_message_to_")) {
        String tmp = str.replace("new_message_to_","");
        tmp = tmp.replace("_",".");
        try {
          InetAddress ip = InetAddress.getByName(tmp);
          for(int i=0; i<history.getTabCount(); i++) {
            UserTabPane tab = (UserTabPane) history.getComponentAt(i);
            if(tab.getUserIp().getHostAddress().equals(tmp)) {
              String mac = controler.getModel().getMacFromIp(ip);
              String pseudo = controler.getModel().getPseudoFromMac(mac);
              UserTabPane new_tab = new UserTabPane(history, controler, ip, mac, pseudo);
              history.setComponentAt(i,new_tab);
            }
          }
        }
        catch (Exception e) {
          System.err.println(e.getClass().getName()+":"+e.getMessage());
          System.exit(0);
        }
      }
      else if(str.contains("new_message_from_")) {
        String tmp = str.replace("new_message_from_","");
        tmp = tmp.replace("_",".");
        try {
          InetAddress ip = InetAddress.getByName(tmp);
          int tab_already_exists = 0;
          for(int i=0; i<history.getTabCount(); i++) {
            UserTabPane tab = (UserTabPane) history.getComponentAt(i);
            if(tab.getUserIp().getHostAddress().equals(tmp)) {
              String mac = controler.getModel().getMacFromIp(ip);
              String pseudo = controler.getModel().getPseudoFromMac(mac);
              UserTabPane new_tab = new UserTabPane(history, controler, ip, mac, pseudo);
              history.setComponentAt(i,new_tab);
              history.setTitleAt(i, history.getTitleAt(i).replace("*","") + " *");
              tab_already_exists = 1;
            }
          }
          if(tab_already_exists == 0) {
            String mac = controler.getModel().getMacFromIp(ip);
            String pseudo = controler.getModel().getPseudoFromMac(mac);
            UserTabPane tab = new UserTabPane(history, controler, ip, mac, pseudo);
            history.addTab(pseudo + " *",tab);
            history.setTabComponentAt(history.getTabCount()-1,new ButtonTabComponent(history));
          }
        }
        catch (Exception e) {
          System.err.println(e.getClass().getName()+":"+e.getMessage());
          System.exit(0);
        }
      }
    }

    private class ButtonTabComponent extends JPanel {
      private final JTabbedPane pane;

      public ButtonTabComponent(final JTabbedPane pane) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
          throw new NullPointerException("JTabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);

        JLabel label = new JLabel() {
          public String getText() {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i!=-1) {
              return pane.getTitleAt(i);
            }
            return null;
          }
        };

        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
        JButton button = new TabButton();
        add(button);
        setBorder(BorderFactory.createEmptyBorder(2,0,0,0));

        this.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            int index = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (pane.getTitleAt(index).contains(" *")){
              pane.setTitleAt(index, pane.getTitleAt(index).replace(" *",""));
            }
            pane.setSelectedIndex(index);
          }
        });
      }

      private class TabButton extends JButton implements ActionListener {
        public TabButton() {
          int size = 17;
          setPreferredSize(new Dimension(size, size));
          setContentAreaFilled(false);
          setFocusable(false);
          setBorder(BorderFactory.createEtchedBorder());
          setBorderPainted(false);
          addMouseListener(buttonMouseListener);
          setRolloverEnabled(true);
          addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                pane.remove(i);
            }
        }

        protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          Graphics2D g2 = (Graphics2D) g.create();
          if(getModel().isPressed()) {
            g2. translate(1,1);
          }
          g2.setStroke(new BasicStroke(2));
          g2.setColor(Color.BLACK);
          if(getModel().isRollover()) {
            g2.setColor(Color.RED);
          }
          int delta = 6;
          g2.drawLine(delta, delta, getWidth()-delta-1, getHeight()-delta-1);
          g2.drawLine(getWidth()-delta-1,delta,delta,getHeight()-delta-1);
          g2.dispose();
        }
      }

      private final MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
          Component component = e.getComponent();
          if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            button.setBorderPainted(true);
          }
        }

        public void mouseExited(MouseEvent e) {
           Component component = e.getComponent();
           if (component instanceof AbstractButton) {
               AbstractButton button = (AbstractButton) component;
               button.setBorderPainted(false);
           }
        }

      };
    }

    private class UserListLabel extends JLabel implements ListCellRenderer<String> {

      private final String icon = "../img/user_icon.png";
      private BddManager model;

      public UserListLabel(BddManager model) {
        this.setOpaque(true);
        this.model = model;
      }

      @Override
      public Component getListCellRendererComponent(JList<? extends String> list, String pseudo, int index, boolean selected, boolean expanded) {

         this.setIcon(new ImageIcon(getClass().getResource(this.icon)));
         String status = this.model.getStatusFromPseudo(pseudo);
         this.setText(pseudo + " (" + status + ")");
         if (selected) {
           this.setBackground(list.getSelectionBackground());
           this.setForeground(list.getSelectionForeground());
         } else {
           this.setBackground(list.getBackground());
           this.setForeground(list.getForeground());
         }

         return this;
       }
    }

    private class UserTabPane extends JPanel {
      private final JTabbedPane containerPane;
      private final MainController controler;
      private InetAddress user_ip;
      private String user_mac;
      private String user_pseudo;

      private JScrollPane scrollPane;
      private JPanel historyPane = new JPanel();
      private ArrayList<MessageHistory> history;

      private JPanel msgPane = new JPanel();
      private JButton send_button = new JButton("Envoyer");
      private JTextField msg_to_send = new JTextField();
      private JButton open_button = new JButton("Importer");

      public UserTabPane(final JTabbedPane containerPane, final MainController controler, InetAddress user_ip, String user_mac, String user_pseudo) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.containerPane = containerPane;
        this.controler = controler;
        this.user_pseudo = user_pseudo;
        this.user_ip = user_ip;
        this.user_mac = user_mac;
        this.setLayout(new BorderLayout());

        this.updateHistory();
        historyPane.setLayout(new BoxLayout(historyPane, BoxLayout.PAGE_AXIS));
        for(int i=0; i<history.size(); i++) {
            historyPane.add(history.get(i));
            historyPane.add(Box.createRigidArea(new Dimension(0,5)));
        }
        historyPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        scrollPane = new JScrollPane(historyPane);
        historyPane.setAutoscrolls(true);
        scrollPane.setPreferredSize(new Dimension(700,415));
        scrollPane.setLayout(new ScrollPaneLayout());
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        // Déplacer la scrollbar à la fin de la discussion
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });

        this.add(scrollPane, BorderLayout.PAGE_START);
        this.add(msgPane, BorderLayout.PAGE_END);

        msgPane.setLayout(new BoxLayout(msgPane, BoxLayout.LINE_AXIS));
        msgPane.add(msg_to_send);
        msgPane.add(Box.createRigidArea(new Dimension(5,0)));
        msgPane.add(send_button);
        msgPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        msgPane.add(Box.createRigidArea(new Dimension(5,0)));
        msgPane.add(open_button);

        send_button.addActionListener(new ActionListener () {
          public void actionPerformed(ActionEvent e) {
            if(controler.getModel().getStatusFromMac(user_mac).equals("local"))
                processMsg();
            else {
                JOptionPane.showMessageDialog(null,"Impossible d'envoyer un message à un utilisateur se trouvant sur un autre réseau !");
            }
          }
        });

        open_button.addActionListener(new ActionListener () {
          public void actionPerformed(ActionEvent e) {
            processFile();
          }
        });

        msg_to_send.addKeyListener(new CustomKeyListener());
      }

      private class CustomKeyListener implements KeyListener{
          public void keyTyped(KeyEvent e) {
          }
          public void keyPressed(KeyEvent e) {
             if(e.getKeyCode() == KeyEvent.VK_ENTER){
               processMsg();
             }
          }
          public void keyReleased(KeyEvent e) {
          }
      }

      public void processMsg() {
        String msg = msg_to_send.getText();
        if(msg.length()>250) {
            msg_to_send.setText("");
            JOptionPane.showMessageDialog(null,"Les messages sont limités à 250 caractères !");
        }
        else if(!msg.equals("")) {
            msg_to_send.setText("");
            controler.sendMessage(msg.getBytes(),user_pseudo);
        }
        else {
          Object[] options = {"Envoyer quand même", "Annuler"};
          int choice = JOptionPane.showOptionDialog(null, "Vous êtes sur le point d'envoyer un message vide !", "Attention message vide !",
                                              JOptionPane.YES_NO_OPTION,
                                              JOptionPane.QUESTION_MESSAGE,
                                              null,
                                              options,
                                              options[1]);
          if(choice==0) {
            controler.sendMessage(msg.getBytes(),user_pseudo);
          }
        }
      }

      public void processFile() {
        JFileChooser file_chooser = new JFileChooser();
        int returnVal = file_chooser.showOpenDialog(file_chooser);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = file_chooser.getSelectedFile();
            String path = file.getAbsolutePath();
            try {
                // On récupère l'extension du fichier
                int index = path.lastIndexOf(".");
                String file_extension = path.substring(index + 1);
                System.out.println("extension : "+file_extension);

                if(file_extension.equals("jpg") || file_extension.equals("jpeg") || file_extension.equals("png") || file_extension.equals("bmp") || file_extension.equals("svg")) {

                    BufferedImage bImage = ImageIO.read(new File(path));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(bImage, file_extension, bos);
                    byte[] data = bos.toByteArray();
                    controler.sendImage(path,data,user_pseudo);
                }
                else {
                    byte[] data = new byte [(int)file.length()];
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(data,0,data.length);
                    controler.sendFile(path,data,user_pseudo);
                }
            } catch (Exception e) {
                System.err.println(e.getClass().getName()+":"+e.getMessage());
            }
        }
      }

      public void updateHistory() {
        history = new ArrayList<MessageHistory>();
        Log log = controler.getModel().getMsgHistory(this.user_mac);
        ArrayList<Message> msg_list = log.getHistory();
        for(int i=0; i<msg_list.size(); i++) {
          MessageHistory new_msg = new MessageHistory(msg_list.get(i), controler);
          history.add(new_msg);
        }
      }

      public InetAddress getUserIp() {
        return this.user_ip;
      }

      public String getUserMac() {
        return this.user_mac;
      }

      private class MessageHistory extends JPanel {

        private final MainController controler;

        private String source;
        private String dest;
        private byte[] data;
        private String timestamp;
        private String filetype;

        private JTextArea message_content;
        private JLabel picLabel;
        private JButton download_file;
        private JLabel time_info;

        public MessageHistory(Message msg, MainController controler) {
          super(new FlowLayout(FlowLayout.LEFT, 0, 0));
          this.controler = controler;

          // Récupération des informations du message
          boolean from_me;
          String source_address = msg.getSource();
          String dest_address = msg.getDest();
          if(source_address.equals(this.controler.getModel().getLocalUser().getMac())) {
            this.source = this.controler.getModel().getLocalUser().getPseudo();
            this.dest = this.controler.getModel().getPseudoFromMac(dest_address);
            from_me = true;
          }
          else {
            this.dest = this.controler.getModel().getLocalUser().getPseudo();
            this.source = this.controler.getModel().getPseudoFromMac(source_address);
            from_me = false;
          }

          this.data = msg.getData();
          this.timestamp = msg.getTimestamp();
          this.filetype = msg.getFiletype();

          // Création des éléments du Panel
          String info = "De "+ this.source +" à "+ this.dest;
          time_info = new JLabel(this.timestamp, SwingConstants.RIGHT);
          time_info.setBorder(BorderFactory.createEmptyBorder(0,0,3,3));
          Font font = new Font("Courier", Font.ITALIC, 12);
          time_info.setFont(font);

          this.setLayout(new BorderLayout());

          String data_str = new String(this.data);

          if(this.filetype.equals("text")) {
            message_content = new JTextArea(data_str);
            message_content.setLineWrap(true);
            message_content.setWrapStyleWord(true);
            message_content.setEditable(false);
            message_content.setBackground(new Color(0,0,0,0));
            message_content.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            this.add(message_content, BorderLayout.CENTER);

            this.setMaximumSize(new Dimension(700,100));
          }
          else if(this.filetype.equals("img")) {
            try {
                BufferedImage img = ImageIO.read(new File(data_str));
                picLabel = new JLabel(new ImageIcon(img));
                this.add(picLabel, BorderLayout.CENTER);

                int height = img.getHeight() + 100;
                this.setMaximumSize(new Dimension(700,height));
            } catch (Exception e) {
                e.printStackTrace();
            }
          }
          else if(this.filetype.equals("file")) {
            int index = data_str.lastIndexOf("/");
            if(index == -1) {
              index = data_str.lastIndexOf("\\");
            }
            String filename = data_str.substring(index + 1);
            download_file = new JButton("Télécharger "+ filename);
            this.add(download_file, BorderLayout.CENTER);

            this.setMaximumSize(new Dimension(700,100));

            download_file.addMouseListener(new MouseAdapter() {
              public void mouseClicked(MouseEvent e) {
                String filename = download_file.getText().replace("Télécharger ","");

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Sauvegarder sous");
                fileChooser.setSelectedFile(new File(filename));

                int userSelection = fileChooser.showSaveDialog(download_file);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    try {
                        String file_path = "tmp/" + filename;
                        File fileToSave = new File(file_path);

                        File savedFile = fileChooser.getSelectedFile();
                        savedFile.createNewFile();

                        byte[] file_content = new byte [(int)fileToSave.length()];
                        FileInputStream fis = new FileInputStream(fileToSave);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        bis.read(file_content,0,file_content.length);

                        OutputStream os = new FileOutputStream(savedFile);
                        os.write(file_content);
                        os.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
              }
            });
          }
          else {
            System.out.println("Unrecognized filetype !");
          }

          // On met des bordures de couleur différente en fonction de la source du message
          if(from_me) {
            Border redLine = BorderFactory.createLineBorder(Color.red, 2);
            this.setBorder(BorderFactory.createTitledBorder(redLine, info));
          }
          else {
            Border blueLine = BorderFactory.createLineBorder(Color.blue, 2);
            this.setBorder(BorderFactory.createTitledBorder(blueLine, info));
          }

          this.add(time_info, BorderLayout.PAGE_END);
        }

      }

    }
}
