package com.sdz.model;
import com.sdz.observer.Observable;

/* CECI EST UNE PREMIERE VERSION OU LES MESSAGES NE SONT PAS PERSISTENTS (ILS DISPARAISSENT
D'UNE UTILISATION A L'AUTRE. UNE VERSION AMELIOREE SERAIT DE STOCKER LES MESSAGES DANS UNE BDD
LOCALE COMME SQLITE QUI STOCK LES DONNEES DANS UN FICHIER EN LOCAL. DANS CE CAS, IL FAUDRA CHANGER 
LES CLASSES LOG ET MESSAGE : SUPPRIMER MESSAGE ET LOG DEVIENDRAIT UNE CLASSE QUI ECRIT DANS LA BDD ET
QUI PEUT LIRE DANS LA BDD POUR RECUPERER LES HISTORIQUES DE MESSAGE 
OU AUTRE SOLUTION : ECRIRE DANS DES FICHIERS AVEC UN FORMAT JSON, XML OU CSV*/

public class BddManager extends AbstractModel {

    public BddManager() {
        this.logs = new HashMap<InetAddress,Log>();
        this.connected_users = new ArrayList<User>();
        this.local_user = User(InetAddress.getLocalHost(),"");
        this.listObserver = new ArrayList<Observer>();
    }

    public void setLocalUser(User target) {
        this.local_user = User;
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

    public void addMessage(InetAddress source, InetAddress dest, byte[] data, Date timestamp) {
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
            this.logs.add(target,log);
            history = log;
        }
        return history;
    }
    
}
