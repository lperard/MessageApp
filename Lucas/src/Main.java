public class Main {

  public static void main(String[] args) {

    int sendPort = Integer.parseInt(args[0]);
    int receivePort = Integer.parseInt(args[1]);

    //Instanciation de notre modèle
    BddManager model = new BddManager();

    //Création du contrôleur
    MainController controler = new MainController(model,sendPort,receivePort);

    //Création de notre fenêtre avec le contrôleur en paramètre
    //InterfaceManager view = new InterfaceManager(com);

    //Ajout de la fenêtre comme observer de notre modèle
    //model.addObserver(view);
  }
}
