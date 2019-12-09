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
        try {
            Class.forName("org.sqlite.JDBC");
            this.bdd_connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");

            this.bdd_statement = this.bdd_connection.createStatement();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }

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

    public User getLocalUser(User target) {
        return this.local_user;
    }

    public void addUser(User user) {
        this.connected_users.add(user);
        notifyObserver("new_user_online");
    }

    public void rmUser(User user) {
        this.connected_users.remove(user);
        notifyObserver("new_user_offline");
    }

    public ArrayList<User> getUserList() {
        return this.connected_users;
    }

    public void addMessage(InetAddress source, InetAddress dest, byte[] data, String timestamp) {
        String sql="";
        String data_str = new String(data);       

        try {

            Class.forName("org.sqlite.JDBC");
            this.bdd_connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");

            this.bdd_statement = this.bdd_connection.createStatement();

            if(local_user.getId() == source) {
                
                sql = "create table if not exists LOG_"+dest.getHostAddress()+" (source VARCHAR(20), dest VARCHAR(20), data VARCHAR(100), timestamp VARCHAR(20))";
                System.out.println(sql);
                this.bdd_statement.executeUpdate(sql);

                sql = "insert into LOG_"+dest.getHostAddress()+" (source,dest,data,timestamp) values ('"+source.getHostAddress()+"', '"+dest.getHostAddress()+"', '"+data_str+"', '"+timestamp+"');";
                System.out.println(sql);
                this.bdd_statement.executeUpdate(sql);
        
                notifyObserver("new_message_to_"+ dest.getHostAddress());
            }
            else if(local_user.getId() == dest) {
                sql = "create table if not exists LOG_"+source.getHostAddress()+" (source VARCHAR(20), dest VARCHAR(20), data VARCHAR(100), timestamp VARCHAR(20))";
                 this.bdd_statement.executeUpdate(sql);

                sql = "insert into LOG_"+source.getHostAddress()+" (source,dest,data,timestamp) values ('"+source.getHostAddress()+"', '"+dest.getHostAddress()+"', '"+data_str+"', '"+timestamp+"');";
                this.bdd_statement.executeUpdate(sql);

                notifyObserver("new_message_from_"+ source.getHostAddress());
            }
            
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }
    }

    public ResultSet getMsgHistory(InetAddress target) {
        ResultSet rs = null;
                
        try {        
            String sql = "create table if not exists LOG_"+target.getHostAddress()+" (source VARCHAR(20), dest VARCHAR(20), data VARCHAR(100), timestamp VARCHAR(20))";
            this.bdd_statement.executeUpdate(sql);

            sql = "select * from LOG_"+target.getHostAddress()+";";
            rs = this.bdd_statement.executeQuery(sql);

        } catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }
        return rs;
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

