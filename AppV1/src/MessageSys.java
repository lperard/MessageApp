import java.io.Serializable;
public class MessageSys implements Serializable {
	
	private static final long serialVersionUID = 3333L;
	
	private Type type;

    private User user;

    public MessageSys (Type type, User user) {
        this.type = type;
        this.user = user;
    }

    public String toString () {
        String print = new String("");
        return print;
    }

    public String constructMessageSystem () {
    String message = "";
    String nom = this.type.toString();
    message += nom;
    message += " ";
    message += this.user.toString();
    return message;
    }
    
    public Type getType() {
    	return this.type;
    }

    public User getUser() {
        return this.user;
    }

}
