package com.sdz.model;

import java.util.ArrayList;
import java.util.HashMap;
import com.sdz.observer.Observable;
import com.sdz.observer.Observer;
import java.net.*;

public abstract class AbstractModel implements Observable {
    
    protected HashMap<InetAddress,Log> logs = new HashMap<InetAddress,Log>();
    protected ArrayList<User> connected_users = new ArrayList<User>();
    protected User local_user = User(InetAddress.getLocalHost().getHostAddress(),"");
    private ArrayList<Observer> listObserver = new ArrayList<Observer>();

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
