import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class HttpHandler implements Runnable {

	private BddManager model;

	public HttpHandler (BddManager model) {
			this.model = model;
	}

	public void sendHttpHello(User user) {
			URL url;
			String addr = user.getIp().getHostAddress();
			String pseudo = user.getPseudo();
			boolean connected = user.getConnected();
			try {
				url = new URL("http://localhost:8080/servPresence/test?ip="+addr+"&pseudo="+pseudo+"&status="+connected);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    	connection.setRequestMethod("GET");
	    	connection.setDoOutput(true);
	    	int status = connection.getResponseCode();
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
			if(user[2].equals("true")) {
				User new_user = new User(InetAddress.getByName(user[1]),user[0],true);
				model.addUser(new_user);
			}
			else if(user[2].equals("false")) {
				User user_to_remove= new User(InetAddress.getByName(user[1]),user[0],false);
				model.rmUser(user_to_remove);
			}
	}

	@Override
	public void run() {
		System.out.println("HttpHandler lancé !");
		while (true) {
			try {
				Thread.sleep(5000);
				User current_user = model.getLocalUser();
				sendHttpHello(current_user);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
