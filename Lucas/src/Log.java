import java.util.ArrayList;

public class Log {

    protected ArrayList<Message> history = new ArrayList<Message>();

    public void addMessage(Message msg) {
        this.history.add(msg);
    }

    public ArrayList<Message> getHistory() {
        return this.history;
    }

}

