package com.sdz.model;
/*
import java.util.ArrayList;
import java.util.HashMap;
import com.sdz.observer.Observable;
import com.sdz.observer.Observer;
import java.net.*;
import java.sql.*;

public abstract class AbstractModel implements Observable {
    
    protected HashMap<InetAddress,Log> logs;
    protected ArrayList<User> connected_users;
    protected User local_user;
    private ArrayList<Observer> listObserver;
    
    public abstract void setLocalUser(User target);

    public abstract User getLocalUser(User target);

    public abstract void addUser(User user);

    public abstract void rmUser(User user);

    public abstract ArrayList<User> getUserList();

    public abstract void addMessage(InetAddress source, InetAddress dest, byte[] data, Date timestamp);

    public abstract Log getMsgHistory(InetAddress target);        

    // Implémentation du pattern observer
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
*/