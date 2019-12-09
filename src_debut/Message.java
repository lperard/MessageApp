/*package com.sdz.model;

import java.net.*;
import java.util.*;

public class Message {

    protected InetAddress source;
    protected InetAddress dest;
    protected byte[] data;
    protected Date timestamp;

    public Message(InetAddress source, InetAddress dest, byte[] data, Date timestamp) {
        this.source = source;
        this.dest = dest;
        this.data = data;
        if(timestamp != null) {
            this.timestamp = timestamp;
        }
        else {        
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date timestamp = new Date();
            this.timestamp = dateFormat.format(timestamp);
        }
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

    public Date getTimestamp() {
        return this.timestamp;
    }

}
*/