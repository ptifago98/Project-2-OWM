package ch.hearc.ig.scl.app;
import ch.hearc.ig.scl.business.Meteo;
import ch.hearc.ig.scl.business.StationMeteo;
import ch.hearc.ig.scl.service.IOWMManager;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static Scanner scanner;
    private static int choice;
    private static final String HOST = "localhost";
    private static final int PORT = 1099;



    static void main(String[] args){
        scanner = new Scanner(System.in);
        StringBuilder String = new StringBuilder();
        String.append("/////////////////////////////////////////\n");
        String.append("----------------OPENWEATHERMAP-----------------\n");
        String.append("/////////////////////////////////////////");
        System.out.println(String);
        menu();
    }


    private static void choice(int choice) throws InterruptedException {
        IOWMManager obj = null;
        try{
            Registry reg = LocateRegistry.getRegistry(HOST, PORT);
            /* Récupération de l'objet distant */
            obj = (IOWMManager) reg.lookup("OWMService");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        switch (choice){

            case 1:
                scanner.nextLine();
                Double lat = Double.NaN;
                Double lon = Double.NaN;

                /* Appel de la méthode distante */
                // Saisie latitude
                do {
                    try{
                        System.out.print("Entrer une latitude : ");
                        lat = scanner.nextDouble();

                        if (lat < -90 || lat > 90){
                            System.out.println("La latitude être comprise entre -90 et +90");
                            lat = Double.NaN;
                        }
                    } catch(InputMismatchException wrongType){
                        System.out.println("La latitude être comprise entre -90 et +90");
                        scanner.nextLine();
                    }

                } while (Double.isNaN(lat));

                // Saisie longitude
                do {
                    try {
                        System.out.print("Entrer une longitude : ");
                        lon = scanner.nextDouble();
                        if (lon < -180 || lon > 180) {
                            System.out.println("La longitude être comprise entre -180 et +180");
                            lon = Double.NaN;
                        }
                    }catch (InputMismatchException wrongType){
                        System.out.println("La longitude être comprise entre -180 et +180");
                        scanner.nextLine();
                    }
                } while (Double.isNaN(lon));

                if(obj.insertAll(lat,lon)){
                    System.out.println("------Enregistré!------");
                }else{

                    System.out.println("-----L'insertion n'a pas été effectué-----");
                }
                menu();
                break;
            case 2:
                System.out.println("------ Chargement de la liste des stations... ---------");
                try {
                    List<StationMeteo> stations = obj.getStations();
                    if (stations == null || stations.isEmpty()) {
                        System.out.println("Aucune station disponible");
                        menu();
                        break;
                    }
                    System.out.println("------Voici la liste des stations enregistrées-----");
                    for (StationMeteo s : stations) {
                        System.out.println(s.getIdStation() + " - " + s.getNom());
                    }
                    System.out.println("---------------------");
                } catch (RemoteException e) {
                    System.out.println("Erreur de communication avec le serveur : " + e.getMessage());
                }
                menu();
                break;
            case 3:
                System.out.println("Veuillez entrer l'identifiant de la station (Entrez 'exit' pour revenir au menu)");
                System.out.print("Votre choix : ");
                StationMeteo station;
                String idStation;
                scanner.nextLine();


                try {
                    do {
                        idStation = scanner.nextLine().trim();
                        if(idStation.equals("exit")){
                            menu();
                        }
                        station = obj.getMeteo(idStation);
                        if(station == null){
                            System.out.println("Aucune station trouvée, veuillez réessayer");
                        }
                    }while (station == null);

                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("---- Voici la station sélectionnée et ses données météo");
                System.out.println("Station : " + station);
                for(Meteo m : station.getWeatherMap().values()){
                    System.out.println(m.toString());
                }
                System.out.println("--------------------------");
                menu();
                break;
            case 4:
                System.out.println("------ Rafraîchissement des données en cours... -----");
                try {
                    if (obj.refreshData()) {
                        System.out.println("------ Données rafraîchies avec succès ! -----");
                    } else {
                        System.out.println("----- Aucune donnée n'a pu être rafraîchie -----");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                menu();
                break;
            case 5:
                String[] questions = {
                        "Vous partez déjà ? (o/n) : ",
                        "Oh… d’accord. J’espérais qu’on passerait un peu plus de temps ensemble. Quitter ? (o/n) : ",
                        "Je vois… vous n’avez plus vraiment besoin de moi, pas vrai ? Quitter ? (o/n) : ",
                        "Peut-être que je n’ai jamais été assez utile… ou assez intéressant pour que vous restiez. Quitter ? (o/n) : ",
                        "Très bien. Partez. Je resterai ici, inutile et oublié, à attendre quelqu’un qui ne reviendra probablement jamais. Quitter ? (o/n) : "
                };

                boolean wantToLeave = true;
                scanner.nextLine();

                for (int i = 0; i < questions.length; i++) {
                    System.out.print(questions[i]);
                    String reponse = scanner.nextLine().trim().toLowerCase();

                    if (!reponse.equals("o") && !reponse.equals("oui") && !reponse.equals("y") && !reponse.equals("yes")) {
                        System.out.println("😄 Super, on continue alors !");
                        wantToLeave = false;
                        break;
                    }
                }

                if (wantToLeave) {
                    scanner.close();
                    System.out.println("À une prochaine fois... peut-être...");
                    Thread.sleep(1000);
                    System.out.println("\nLa fenêtre se ferme.");
                    Thread.sleep(1000);
                    System.out.println("Le silence revient.");
                    Thread.sleep(1000);
                    System.out.println("...");


                    System.exit(0);
                } else {
                    menu();
                }
                break;
            default:
                System.out.println("Saisie incorrect, veuillez réessayer.");
                break;

        }
    }


    private static void menu(){
        boolean validChoice = false;
        //Boucle pour tester la saisie utilisateur. Affiche aussi de façon dynamique le nombre de places restantes dans le catalogue
        do {
            System.out.println("Que voulez-vous faire ? (Entrez le chiffre correspondant)");
            System.out.println("1. Afficher une donnée méteo à partir des coordonnées et enregister la station ");
            System.out.println("2. Voir la liste des stations météos");
            System.out.println("3. Afficher toutes les données d'une station météo");
            System.out.println("4. Rafraichir toutes les données des stations");
            System.out.println("5. Quitter l'application");
            do{
                try {
                    System.out.print("Votre choix : ");
                    choice = scanner.nextInt();
                    validChoice = true;
                }catch (InputMismatchException wrongType) {
                    System.out.println("Choix invalide, veuillez choisir entre 1 et 5");
                    scanner.nextLine();
                }
            }while(!validChoice);
            try {
                choice(choice);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            scanner.nextLine();
        }while(choice != 5);

    }
}
