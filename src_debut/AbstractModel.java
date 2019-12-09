import java.util.ArrayList;
import java.util.HashMap;
import java.net.*;
import java.sql.*;

public abstract class AbstractModel implements Observable {
    
    protected HashMap<InetAddress,Log> logs;
    protected ArrayList<User> connected_users;
    protected User local_user;
    protected ArrayList<Observer> listObserver;
    
    public abstract void setLocalUser(User target);

    public abstract User getLocalUser(User target);

    public abstract void addUser(User user);

    public abstract void rmUser(User user);

    public abstract ArrayList<User> getUserList();

    public abstract void addMessage(InetAddress source, InetAddress dest, byte[] data, String timestamp);

    public abstract Log getMsgHistory(InetAddress target);        

    public abstract void addObserver(Observer obs);

    public abstract void removeObserver();

    public abstract void notifyObserver(String str);

}

