import java.io.Serializable;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class Message implements Serializable{

	private static final long serialVersionUID = 1111L;

	protected InetAddress source;

	protected InetAddress dest;

	protected byte[] data;

	protected String timestamp;

	protected String filetype;

    public Message(InetAddress source, InetAddress dest, byte[] data, String timestamp) {
        this.source = source;
        this.dest = dest;
        this.data = data;
        if(timestamp != null) {
            this.timestamp = timestamp;
        }
        else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            this.timestamp = dateFormat.format(date);
        }
				this.filetype="text";
    }

		public Message(InetAddress source, InetAddress dest, byte[] data, String timestamp, String filetype) {
        this.source = source;
        this.dest = dest;
        this.data = data;
        if(timestamp != null) {
            this.timestamp = timestamp;
        }
        else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            this.timestamp = dateFormat.format(date);
        }
				this.filetype=filetype;
    }

    public InetAddress getSource() {
        return this.source;
    }

    public InetAddress getDest() {
        return this.dest;
    }

    public byte[] getData() {
        return this.data;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

		public String getFiletype() {
				return this.filetype;
		}

}
