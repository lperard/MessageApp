import java.net.*;
import java.io.Serializable;
public class User implements Serializable {
    private static final long serialVersionUID = 2222L;

    protected InetAddress ip;
    protected String pseudo;
    protected boolean connected;

    public User(InetAddress ip, String pseudo, boolean connected) {
        this.ip = ip;
        this.pseudo = pseudo;
        this.connected = connected;
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

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public InetAddress getIp() {
        return this.ip;
    }

    public String toString () {
        String print = ip.getHostAddress() + " " + pseudo;
        return print;
    }
}
