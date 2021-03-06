import java.net.*;
import java.io.Serializable;
public class User implements Serializable {
    private static final long serialVersionUID = 2222L;

    protected String mac;
    protected InetAddress ip;
    protected String pseudo;
    protected boolean connected;
    protected Status status;

    public User(String mac, InetAddress ip, String pseudo, boolean connected) {
        this.mac = mac;
        this.ip = ip;
        this.pseudo = pseudo;
        this.connected = connected;
        this.status = Status.Local;
    }

    public User(String mac, InetAddress ip, String pseudo, boolean connected, Status status) {
        this.mac = mac;
        this.ip = ip;
        this.pseudo = pseudo;
        this.connected = connected;
        this.status = status;
    }

    public String getMac() {
        return this.mac;
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

    public Status getStatus() {
        return this.status;
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
