package Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Model.BddManager;
import Model.Status;
import Model.User;

public class HttpHandler implements Runnable {

	private BddManager model;

	public HttpHandler (BddManager model) {
			this.model = model;
	}

	public void sendHttpHello(User user) {
		URL url;
		String mac = user.getMac();
		String addr = user.getIp().getHostAddress();
		String pseudo = user.getPseudo();
		boolean connected = user.getConnected();
        Status status = user.getStatus();
		try {
			url = new URL("https://srv-gei-tomcat.insa-toulouse.fr/servPresence_LACOTE_PERARD/test?mac="+mac+"&ip="+addr+"&pseudo="+pseudo+"&connected="+connected+"&status="+status);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        	connection.setRequestMethod("GET");
        	connection.setDoOutput(true);
        	int result = connection.getResponseCode();
        	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        	String inputLine;
        	StringBuffer content = new StringBuffer();
        	int nb_ligne = 1;
        	while ((inputLine = in.readLine()) != null) {
        		content.append(inputLine);
        		parseResponse(inputLine);
        	}
        	in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parseResponse (String oneUser) throws UnknownHostException {
			String[] user = oneUser.split(" ");
            Status status;
            if(user[4].equals("local")) {
                status = Status.Local;   
            }
            else {
                status = Status.Remote;
            }
			if(user[3].equals("true")) {
				User new_user = new User(user[0],InetAddress.getByName(user[1]),user[2],true,status);
				if(!new_user.getMac().equals(model.getLocalUser().getMac())) {
                    model.addUser(new_user);
                }
			}
			else if(user[3].equals("false")) {
				User user_to_remove= new User(user[0],InetAddress.getByName(user[1]),user[0],false,status);
				model.rmUser(user_to_remove);
			}
	}

	@Override
	public void run() {
		System.out.println("HttpHandler lancé !");
		while (true) {
			try {
				User current_user = model.getLocalUser();
				sendHttpHello(current_user);
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
