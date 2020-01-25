import java.io.Serializable;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class Message implements Serializable{

	private static final long serialVersionUID = 1111L;

	protected String source;
	protected String dest;
	protected byte[] data;
	protected String timestamp;
	protected String filetype;
	protected String filepath = null;

    public Message(String source, String dest, byte[] data, String timestamp) {
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

		public Message(String source, String dest, byte[] data, String timestamp, String filetype) {
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

		public Message(String source, String dest, byte[] data, String timestamp, String filetype, String filepath) {
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
				this.filepath=filepath;
    }

    public String getSource() {
        return this.source;
    }

    public String getDest() {
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

		public String getFilepath() {
			return this.filepath;
		}

}
