import java.net.*;
import java.io.Serializable;
public class User implements Serializable {
    private static final long serialVersionUID = 2222L;
    protected InetAddress id;
    protected String pseudo;
    protected boolean connected;
    
    public User(InetAddress id, String pseudo) {
        this.id = id;
        this.pseudo = pseudo;
        this.connected = false;        
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean getConnected() {
        return this.connected;
    }

    public void setId(InetAddress ip) {
        this.id = ip;
    }

    public InetAddress getId() {
        return this.id;
    }
    
    public String toString () {
        String print = id.getHostAddress() + " " + pseudo;
        return print; 
    }
}

