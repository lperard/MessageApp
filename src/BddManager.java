import java.util.ArrayList;
import java.net.*;
import java.sql.*;

public class BddManager implements Observable {

    private Connection bdd_connection;
    private PreparedStatement bdd_preparedstatement;
    private Statement bdd_statement;

    protected ArrayList<User> connected_users;
    protected User local_user;
    protected ArrayList<Observer> listObserver;

    public BddManager(String mac, InetAddress ip) {

        this.bdd_connection = null;
        this.bdd_statement = null;
        this.bdd_preparedstatement = null;

        this.connected_users = new ArrayList<User>();

        this.local_user = new User(mac,ip,"",false);
        this.listObserver = new ArrayList<Observer>();
    }

    public void setLocalUser(User target) {
        this.local_user = target;
    }

    public User getLocalUser() {
        return this.local_user;
    }

    public void addUser(User user) {
      // Si un utilisateur a déjà l'IP de user, on le supprime avant de le rajouter
      // En effet on est dans le cas où son pseudo a changé
      boolean already_exists = false;
      for(int i=0; i<this.connected_users.size();i++) {
        User current = this.connected_users.get(i);
        if(current.getMac().equals(user.getMac())) {
          this.connected_users.remove(i);
          already_exists = true;
        }
      }
      this.connected_users.add(user);
      if(!already_exists)
        notifyObserver("new_user_online");
    }

    public void rmUser(User user) {
      int index = -1;
      for(int i=0; i<this.connected_users.size();i++) {
        User current = this.connected_users.get(i);
        if(current.getMac().equals(user.getMac())) {
          index = i;
        }
      }
      if(index!=-1) {
        this.connected_users.remove(index);
        notifyObserver("new_user_offline_"+user.getIp().getHostAddress());
      }
    }

    public void newPseudo(User user) {
      for(int i=0; i<this.connected_users.size();i++) {
        User current = this.connected_users.get(i);
        if(current.getMac().equals(user.getMac())) {
          this.connected_users.remove(i);
        }
      }
      this.connected_users.add(user);
      notifyObserver("new_pseudo_"+user.getIp().getHostAddress());
    }

    public ArrayList<User> getUserList() {
      return this.connected_users;
    }

    public String[] getPseudoList() {
        String[] list = new String[this.connected_users.size()];
        for(int i = 0; i<this.connected_users.size(); i++) {
          list[i] = this.connected_users.get(i).getPseudo();
        }
        return list;
    }

    public InetAddress getIpFromPseudo(String pseudo) {
      InetAddress ip = null;
      for(int i=0; i<this.connected_users.size(); i++) {
        User user = this.connected_users.get(i);
        if(user.getPseudo().equals(pseudo)) {
          ip = user.getIp();
        }
      }
      return ip;
    }

    public String getPseudoFromIp(InetAddress ip) {
      String pseudo = "";
      for(int i = 0; i<this.connected_users.size(); i++) {
        User user = this.connected_users.get(i);
        if(user.getIp().equals(ip)) {
          pseudo = user.getPseudo();
        }
      }
      return pseudo;
    }

    public String getPseudoFromMac(String mac) {
      String pseudo = "";
      for(int i = 0; i<this.connected_users.size(); i++) {
        User user = this.connected_users.get(i);
        if(user.getMac().equals(mac)) {
          pseudo = user.getPseudo();
        }
      }
      return pseudo;
    }

    public String getMacFromIp(InetAddress ip) {
      String mac = "";
      for(int i = 0; i<this.connected_users.size(); i++) {
        User user = this.connected_users.get(i);
        if(user.getIp().equals(ip)) {
          mac = user.getMac();
        }
      }
      return mac;
    }

    public InetAddress getIpFromMac(String mac) {
      InetAddress ip = null;
      for(int i = 0; i<this.connected_users.size(); i++) {
        User user = this.connected_users.get(i);
        if(user.getMac().equals(mac)) {
          ip = user.getIp();
        }
      }
      return ip;
    }

    public String getStatusFromPseudo(String pseudo) {
        String status = "";
        for(int i = 0; i < this.connected_users.size(); i++) {
            User user = this.connected_users.get(i);
            if(user.getPseudo().equals(pseudo)) {
                if(user.getStatus().equals(Status.Local))
                    status = "local";   
                else
                    status = "remote";
            }
        }
        return status;
    }

    public void addMessage(String source, String dest, byte[] data, String timestamp, String filetype) {
        String sql="";
        String data_str = new String(data);
        String source_reformatted = source.replace("-","_");
        String dest_reformatted = dest.replace("-","_");

        try {

            Class.forName("org.sqlite.JDBC");
            this.bdd_connection = DriverManager.getConnection("jdbc:sqlite:app.db");
            this.bdd_statement = this.bdd_connection.createStatement();

            if(local_user.getMac().equals(source)) {

                String ip_source = getLocalUser().getIp().getHostAddress();
                String ip_dest = getIpFromMac(dest).getHostAddress();
                System.out.println("ip source :"+ip_source+" | ip dest : "+ip_dest);

                sql = "create table if not exists LOG_"+ dest_reformatted +" (source VARCHAR(20), dest VARCHAR(20), data VARCHAR(300), timestamp VARCHAR(20), filetype VARCHAR(20))";
                this.bdd_statement.executeUpdate(sql);

                sql = "insert into LOG_" + dest_reformatted +" (source, dest, data, timestamp, filetype) values (?,?,?,?,?);";
                this.bdd_preparedstatement = this.bdd_connection.prepareStatement(sql);
                this.bdd_preparedstatement.setString(1, source);
                this.bdd_preparedstatement.setString(2, dest);
                this.bdd_preparedstatement.setString(3, data_str);
                this.bdd_preparedstatement.setString(4, timestamp);
                this.bdd_preparedstatement.setString(5, filetype);
                this.bdd_preparedstatement.executeUpdate();

                notifyObserver("new_message_to_"+ ip_dest);
            }
            else if(local_user.getMac().equals(dest)) {

                String ip_source = getIpFromMac(source).getHostAddress();
                String ip_dest = getLocalUser().getIp().getHostAddress();

                sql = "create table if not exists LOG_"+ source_reformatted +" (source VARCHAR(20), dest VARCHAR(20), data VARCHAR(300), timestamp VARCHAR(20), filetype VARCHAR(20))";
                this.bdd_statement.executeUpdate(sql);

                sql = "insert into LOG_" + source_reformatted +" (source, dest, data, timestamp, filetype) values (?,?,?,?,?);";
                this.bdd_preparedstatement = this.bdd_connection.prepareStatement(sql);
                this.bdd_preparedstatement.setString(1, source);
                this.bdd_preparedstatement.setString(2, dest);
                this.bdd_preparedstatement.setString(3, data_str);
                this.bdd_preparedstatement.setString(4, timestamp);
                this.bdd_preparedstatement.setString(5, filetype);
                this.bdd_preparedstatement.executeUpdate();

                notifyObserver("new_message_from_"+ ip_source);
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }
    }

    public Log getMsgHistory(String mac) {
        ResultSet rs = null;
        Log log = new Log();
        String mac_reformatted = mac.replace("-","_");

        try {
            Class.forName("org.sqlite.JDBC");
            this.bdd_connection = DriverManager.getConnection("jdbc:sqlite:app.db");
            this.bdd_statement = this.bdd_connection.createStatement();

            String sql = "create table if not exists LOG_"+ mac_reformatted +" (source VARCHAR(20), dest VARCHAR(20), data VARCHAR(300), timestamp VARCHAR(20), filetype VARCHAR(20))";
            this.bdd_statement.executeUpdate(sql);

            sql = "select * from LOG_"+ mac_reformatted +";";
            rs = this.bdd_statement.executeQuery(sql);

            while(rs.next()) {
                String source = rs.getString("source");
                String dest = rs.getString("dest");
                String data_str = rs.getString("data");
                String timestamp = rs.getString("timestamp");
                String filetype = rs.getString("filetype");
                byte[] data = data_str.getBytes();

                Message tmp_msg = new Message(source,dest,data,timestamp,filetype);
                log.addMessage(tmp_msg);
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }

        return log;
    }

    // Implémentation du pattern observable
    public void addObserver(Observer obs) {
        this.listObserver.add(obs);
    }

    public void removeObserver() {
        this.listObserver = new ArrayList<Observer>();
    }

    public void notifyObserver(String str) {
        for(Observer obs : listObserver)
            obs.update(str);
    }
}
