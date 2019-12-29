import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.net.InetAddress;

public class MessageHistory extends JPanel {

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
      this.setBorder(BorderFactory.createTitledBorder(info));
    }

    //this.add(Box.createRigidArea(new Dimension(0,5)));
    this.add(time_info, BorderLayout.PAGE_END);

    this.setMaximumSize(new Dimension(700,100));;
  }

}
