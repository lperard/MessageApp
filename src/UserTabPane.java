import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;

public class UserTabPane extends JPanel {
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

}
