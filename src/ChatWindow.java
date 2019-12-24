import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;

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
}
