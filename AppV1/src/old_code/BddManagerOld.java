import java.util.ArrayList;
import java.util.HashMap;
import java.net.*;
import java.sql.*;

/* CECI EST UNE PREMIERE VERSION OU LES MESSAGES NE SONT PAS PERSISTENTS (ILS DISPARAISSENT
D'UNE UTILISATION A L'AUTRE. UNE VERSION AMELIOREE SERAIT DE STOCKER LES MESSAGES DANS UNE BDD
LOCALE COMME SQLITE QUI STOCK LES DONNEES DANS UN FICHIER EN LOCAL. DANS CE CAS, IL FAUDRA CHANGER 
LES CLASSES LOG ET MESSAGE : SUPPRIMER MESSAGE ET LOG DEVIENDRAIT UNE CLASSE QUI ECRIT DANS LA BDD ET
QUI PEUT LIRE DANS LA BDD POUR RECUPERER LES HISTORIQUES DE MESSAGE 
OU AUTRE SOLUTION : ECRIRE DANS DES FICHIERS AVEC UN FORMAT JSON, XML OU CSV*/

public class BddManagerOld implements Observable {

    protected HashMap<InetAddress,Log> logs;
    protected ArrayList<User> connected_users;
    protected User local_user;
    protected ArrayList<Observer> listObserver;

    public BddManagerOld() {
        this.logs = new HashMap<InetAddress,Log>();
        this.connected_users = new ArrayList<User>();
        try {
            InetAddress address = InetAddress.getLocalHost();
            this.local_user = new User(address,"");
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown Host Address !\n");
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
        Message message = new Message(source,dest,data,timestamp);

        if(local_user.getId() == source) {
            this.getMsgHistory(dest).addMessage(message);
            notifyObserver("new_message_to_"+ dest.getHostAddress());
        }
        else if(local_user.getId() == dest) {
            this.getMsgHistory(source).addMessage(message);
            notifyObserver("new_message_from_"+ source.getHostAddress());
        }
    }

    public Log getMsgHistory(InetAddress target) {
        Log history = this.logs.get(target);
        if(history ==  null) {
            Log log = new Log();
            this.logs.put(target,log);
            history = log;
        }
        return history;
    }
    
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

