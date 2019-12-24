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

    public BddManager() {

        this.bdd_connection = null;
        this.bdd_statement = null;
        this.bdd_preparedstatement = null;

        this.connected_users = new ArrayList<User>();

        try {
            InetAddress my_address = InetAddress.getLocalHost();
            this.local_user = new User(my_address,"");

            // POUR LES TESTS A ENLEVER !
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.1"),"Jean"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.2"),"Pierre"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.3"), "Matthieu"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.4"), "Paul"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.5"), "Philippe"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.6"), "Claude"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.7"), "Marie"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.8"), "Lucie"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.9"), "Claire"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.10"), "Julie"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.11"), "Alain"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.12"), "Adrien"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.13"), "Lucas"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.14"), "Rafael"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.15"), "Ben"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.16"), "Grégoire"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.17"), "Charles"));
            this.connected_users.add(new User(InetAddress.getByName("190.168.120.18"), "Emilie"));

        }
        catch (UnknownHostException e) {
            System.out.println("Unknown Host Address !\n");
            System.exit(0);
        }
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
      for(int i=0; i<this.connected_users.size();i++) {
        User current = this.connected_users.get(i);
        if(current.getIp().equals(user.getIp())) {
          this.connected_users.remove(i);
        }
      }
      this.connected_users.add(user);
      notifyObserver("new_user_online");
    }

    public void rmUser(User user) {
      int index = -1;
      for(int i=0; i<this.connected_users.size();i++) {
        User current = this.connected_users.get(i);
        if(current.getIp().equals(user.getIp())) {
          index = i;
        }
      }
      if(index!=-1) {
        this.connected_users.remove(index);
        notifyObserver("new_user_offline");
      }
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

    public String getPseudoFromIP(InetAddress address) {
      String pseudo = "";
      for(int i = 0; i<this.connected_users.size(); i++) {
        User user = this.connected_users.get(i);
        if(user.getIp().equals(address)) {
          pseudo = user.getPseudo();
        }
      }
      return pseudo;
    }

    public void addMessage(InetAddress source, InetAddress dest, byte[] data, String timestamp) {
        String sql="";
        String source_address = source.getHostAddress();
        String dest_address = dest.getHostAddress();
        String source_reformatted = source_address.replace('.','_');
        String dest_reformatted = dest_address.replace('.','_');
        String data_str = new String(data);

        try {

            Class.forName("org.sqlite.JDBC");
            this.bdd_connection = DriverManager.getConnection("jdbc:sqlite:app.db");
            this.bdd_statement = this.bdd_connection.createStatement();

            if(local_user.getIp().equals(source)) {
                sql = "create table if not exists LOG_"+ dest_reformatted +" (source VARCHAR(20), dest VARCHAR(20), data VARCHAR(100), timestamp VARCHAR(20))";
                this.bdd_statement.executeUpdate(sql);

                sql = "insert into LOG_" + dest_reformatted +" (source, dest, data, timestamp) values (?,?,?,?);";
                this.bdd_preparedstatement = this.bdd_connection.prepareStatement(sql);
                this.bdd_preparedstatement.setString(1,source_address);
                this.bdd_preparedstatement.setString(2,dest_address);
                this.bdd_preparedstatement.setString(3, data_str);
                this.bdd_preparedstatement.setString(4, timestamp);
                this.bdd_preparedstatement.executeUpdate();

                notifyObserver("new_message_to_"+ dest_reformatted);
            }
            else if(local_user.getIp().equals(dest)) {
                sql = "create table if not exists LOG_"+ source_reformatted +" (source VARCHAR(20), dest VARCHAR(20), data VARCHAR(100), timestamp VARCHAR(20))";
                this.bdd_statement.executeUpdate(sql);

                sql = "insert into LOG_"+ source_reformatted +" (source,dest,data,timestamp) values ('"+ source_address +"', '"+ dest_address +"', '"+data_str+"', '"+timestamp+"');";
                this.bdd_statement.executeUpdate(sql);

                notifyObserver("new_message_from_"+ source_reformatted);
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }
    }

    public Log getMsgHistory(InetAddress target) {
        ResultSet rs = null;
        String target_address = target.getHostAddress();
        String target_reformatted = target_address.replace('.','_');

        Log log = new Log();

        try {
            Class.forName("org.sqlite.JDBC");
            this.bdd_connection = DriverManager.getConnection("jdbc:sqlite:app.db");
            this.bdd_statement = this.bdd_connection.createStatement();

            String sql = "create table if not exists LOG_"+ target_reformatted +" (source VARCHAR(20), dest VARCHAR(20), data VARCHAR(100), timestamp VARCHAR(20))";
            this.bdd_statement.executeUpdate(sql);

            sql = "select * from LOG_"+ target_reformatted +";";
            rs = this.bdd_statement.executeQuery(sql);

            while(rs.next()) {
                String source = rs.getString("source");
                String dest = rs.getString("dest");
                String data_str = rs.getString("data");
                String timestamp = rs.getString("timestamp");

                InetAddress source_address = InetAddress.getByName(source);
                InetAddress dest_address = InetAddress.getByName(dest);
                byte[] data = data_str.getBytes();

                Message tmp_msg = new Message(source_address,dest_address,data,timestamp);
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
