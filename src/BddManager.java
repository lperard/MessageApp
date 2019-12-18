import java.util.ArrayList;
import java.net.*;
import java.sql.*;

public class BddManager implements Observable {

    private Connection bdd_connection;
    private Statement bdd_statement;
    protected ArrayList<User> connected_users;
    protected User local_user;
    protected ArrayList<Observer> listObserver;

    public BddManager() {
        this.bdd_connection = null;
        this.bdd_statement = null;

        this.connected_users = new ArrayList<User>();
        try {
            InetAddress address = InetAddress.getLocalHost();
            this.local_user = new User(address,"");
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown Host Address !\n");
            System.exit(0);
        }
        this.listObserver = new ArrayList<Observer>();
    }

    public void setLocalUser(User target) {
        this.local_user = target;
        // Faut-il notifier qqch à la view ici ???
    }

    public User getLocalUser() {
        return this.local_user;
    }

    public void addUser(User user) {
      boolean exists = false;
      for(int i=0; i<this.connected_users.size();i++) {
        User current = this.connected_users.get(i);
        if(current.getId().equals(user.getId())) {
          exists = true;
        }
      }
      if(!exists) {
        this.connected_users.add(user);
        notifyObserver("new_user_online");
      }
    }

    public void rmUser(User user) {
      int index = -1;
      for(int i=0; i<this.connected_users.size();i++) {
        User current = this.connected_users.get(i);
        if(current.getId().equals(user.getId())) {
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
            System.out.println("Opened database successfully");

            this.bdd_statement = this.bdd_connection.createStatement();

            if(local_user.getId().equals(source)) {
                sql = "create table if not exists LOG_"+ dest_reformatted +" (source VARCHAR(20), dest VARCHAR(20), data VARCHAR(100), timestamp VARCHAR(20))";
                this.bdd_statement.executeUpdate(sql);

                sql = "insert into LOG_"+ dest_reformatted +" (source,dest,data,timestamp) values ('"+ source_address +"', '"+ dest_address +"', '"+data_str+"', '"+timestamp+"');";
                this.bdd_statement.executeUpdate(sql);

                notifyObserver("new_message_to_"+ dest_address);
            }
            else if(local_user.getId().equals(dest)) {
                sql = "create table if not exists LOG_"+ source_reformatted +" (source VARCHAR(20), dest VARCHAR(20), data VARCHAR(100), timestamp VARCHAR(20))";
                this.bdd_statement.executeUpdate(sql);

                sql = "insert into LOG_"+ source_reformatted +" (source,dest,data,timestamp) values ('"+ source_address +"', '"+ dest_address +"', '"+data_str+"', '"+timestamp+"');";
                this.bdd_statement.executeUpdate(sql);

                notifyObserver("new_message_from_"+ source_address);
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
        listObserver = new ArrayList<Observer>();
    }

    public void notifyObserver(String str) {
        for(Observer obs : listObserver)
            obs.update(str);
    }
}
