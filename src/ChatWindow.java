package View;

import Controller.MainController;
import Controller.Message;
import Model.User;
import Model.Log;

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

    public ChatWindow(MainController controler, String current_pseudo) {
        this.controler = controler;
        this.controler.getModel().addObserver(this);

        this.user_list = new JList<String>(this.controler.getModel().getPseudoList());
        this.user_list.setCellRenderer(new UserListLabel());
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
          String selectedItem = (String) user_list.getSelectedValue();
          int tabExists = -1;
          for(int i = 0; i<history.getTabCount(); i++) {
            if(history.getTitleAt(i).equals(selectedItem)) {
              tabExists = i;
            }
          }
          if(tabExists==-1) {
            InetAddress tab_ip = controler.getModel().getIpFromPseudo(selectedItem);
            UserTabPane historyPane = new UserTabPane(history,controler,tab_ip);
            history.addTab(selectedItem,historyPane);
            history.setTabComponentAt(history.getTabCount()-1,new ButtonTabComponent(history));
            history.setSelectedIndex(history.getTabCount()-1);
          }
          else {
            history.setSelectedIndex(tabExists);
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
      if(str.equals("new_user_online") || str.equals("new_user_offline")) {
        user_list.setListData(controler.getModel().getPseudoList());
      }
      else if(str.contains("new_message_to_")) {
        String tmp = str.replace("new_message_to_","");
        tmp = tmp.replace("_",".");
        try {
          InetAddress tmp_address = InetAddress.getByName(tmp);
          for(int i=0; i<history.getTabCount(); i++) {
            UserTabPane tab = (UserTabPane) history.getComponentAt(i);
            if(tab.getUserIp().getHostAddress().equals(tmp)) {
              UserTabPane new_tab = new UserTabPane(history, controler, tmp_address);
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
          InetAddress tmp_address = InetAddress.getByName(tmp);
          for(int i=0; i<history.getTabCount(); i++) {
            UserTabPane tab = (UserTabPane) history.getComponentAt(i);
            if(tab.getUserIp().getHostAddress().equals(tmp)) {
              UserTabPane new_tab = new UserTabPane(history, controler, tmp_address);
              history.setComponentAt(i,new_tab);
            }
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

      private final String icon = "../../img/user_icon.png";

      public UserListLabel() {
        this.setOpaque(true);
      }

      @Override
      public Component getListCellRendererComponent(JList<? extends String> list, String pseudo, int index, boolean selected, boolean expanded) {

         this.setIcon(new ImageIcon(getClass().getResource(this.icon)));
         this.setText(pseudo);
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

      private JScrollPane scrollPane;
      private JPanel historyPane = new JPanel();
      private ArrayList<MessageHistory> history;

      private JPanel msgPane = new JPanel();
      private JButton send_button = new JButton("Envoyer");
      private JTextField msg_to_send = new JTextField();

      public UserTabPane(final JTabbedPane containerPane, final MainController controler, InetAddress user_ip) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.containerPane = containerPane;
        this.controler = controler;
        this.user_ip = user_ip;
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
        msgPane.add(Box.createRigidArea(new Dimension(10,0)));
        msgPane.add(send_button);
        msgPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        send_button.addActionListener(new ActionListener () {
          public void actionPerformed(ActionEvent e) {
            processMsg();
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
        if(!msg.equals("")) {
          msg_to_send.setText("");
          String dest_pseudo = containerPane.getTitleAt(containerPane.getSelectedIndex());
          controler.sendMessage(msg.getBytes(),dest_pseudo);
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
            String dest_pseudo = containerPane.getTitleAt(containerPane.getSelectedIndex());
            controler.sendMessage(msg.getBytes(),dest_pseudo);
          }
        }
      }

      public void updateHistory() {
        history = new ArrayList<MessageHistory>();
        Log log = controler.getModel().getMsgHistory(user_ip);
        ArrayList<Message> msg_list = log.getHistory();
        for(int i=0; i<msg_list.size(); i++) {
          MessageHistory new_msg = new MessageHistory(msg_list.get(i), controler);
          history.add(new_msg);
        }
      }

      public InetAddress getUserIp() {
        return this.user_ip;
      }

      private class MessageHistory extends JPanel {

        private final MainController controler;

        private String source;
        private String dest;
        private byte[] data;
        private String timestamp;

        private JTextArea message_content;
        private JLabel time_info;

        public MessageHistory(Message msg, MainController controler) {
          super(new FlowLayout(FlowLayout.LEFT, 0, 0));
          this.controler = controler;

          // Récupération des informations du message
          boolean from_me;
          InetAddress source_address = msg.getSource();
          InetAddress dest_address = msg.getDest();
          if(source_address.equals(this.controler.getModel().getLocalUser().getIp())) {
            this.source = this.controler.getModel().getLocalUser().getPseudo();
            this.dest = this.controler.getModel().getPseudoFromIP(dest_address);
            from_me = true;
          }
          else {
            this.dest = this.controler.getModel().getLocalUser().getPseudo();
            this.source = this.controler.getModel().getPseudoFromIP(source_address);
            from_me = false;
          }

          this.data = msg.getData();
          this.timestamp = msg.getTimestamp();

          // Création des éléments du Panel
          String info = "De "+ this.source +" à "+ this.dest;
          time_info = new JLabel(this.timestamp, SwingConstants.RIGHT);
          time_info.setBorder(BorderFactory.createEmptyBorder(0,0,3,3));
          Font font = new Font("Courier", Font.ITALIC, 12);
          time_info.setFont(font);
          String data_str = new String(this.data);
          message_content = new JTextArea(data_str);
          message_content.setLineWrap(true);
          message_content.setWrapStyleWord(true);
          message_content.setEditable(false);
          message_content.setBackground(new Color(0,0,0,0));
          message_content.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

          this.setLayout(new BorderLayout());

          this.add(message_content, BorderLayout.CENTER);
          if(from_me) {
            Border redLine = BorderFactory.createLineBorder(Color.red, 2);
            this.setBorder(BorderFactory.createTitledBorder(redLine, info));
          }
          else {
            Border blueLine = BorderFactory.createLineBorder(Color.blue, 2);
            this.setBorder(BorderFactory.createTitledBorder(blueLine, info));
          }

          //this.add(Box.createRigidArea(new Dimension(0,5)));
          this.add(time_info, BorderLayout.PAGE_END);

          this.setMaximumSize(new Dimension(700,100));;
        }

      }

    }
}
