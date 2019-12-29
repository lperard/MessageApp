import javax.swing.*;
import java.awt.*;

public class UserListLabel extends JLabel implements ListCellRenderer<String> {

  private final String icon = "img/user_icon.png";

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
