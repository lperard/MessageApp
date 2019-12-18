import java.net.*;

public class Main {

  public static void main(String[] args) {

    //int sendPort = Integer.parseInt(args[0]);
    //int receivePort = Integer.parseInt(args[1]);
	  
	try {
		InetAddress distant = InetAddress.getByName(args[0]);
		
		//Instanciation de notre modèle
	    BddManager model = new BddManager();

	    //Création du contrôleur
	    MainController controler = new MainController(model, distant);

	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    //Création de notre fenêtre avec le contrôleur en paramètre
    //InterfaceManager view = new InterfaceManager(com);

    //Ajout de la fenêtre comme observer de notre modèle
    //model.addObserver(view);
  }
}
