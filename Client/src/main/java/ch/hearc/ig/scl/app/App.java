package ch.hearc.ig.scl.app;
import ch.hearc.ig.scl.business.StationMeteo;
import ch.hearc.ig.scl.service.IOWMManager;

import java.io.*;
import java.rmi.AccessException;
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
    private static String host = "localhost";
    private static int port = 1099;



    static void main(String[] args){
        scanner = new Scanner(System.in);
        StringBuilder String = new StringBuilder();
        String.append("/////////////////////////////////////////\n");
        String.append("----------------OPENWEATHERMAP-----------------\n");
        String.append("/////////////////////////////////////////");
        System.out.println(String);
        menu();
    }


    private static void choice(int choice){
        IOWMManager obj = null;
        try{
            Registry reg = LocateRegistry.getRegistry(host, port);
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
                List<StationMeteo> stations = null;
                System.out.println("------Voici la liste des stations enregistrées-----");
                try {
                    stations = obj.getStations();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                for (StationMeteo s : stations) {
                    System.out.println(s.getIdStation() + " - " + s.getNom());
                }
                menu();
                break;
            case 3:
                System.out.println("Veuillez entrer l'identifiant de la station :");
                int id = Integer.parseInt(scanner.nextLine());
                System.out.println(id);
                menu();
                break;
            case 4:
                menu();
                break;
            case 5:
                menu();
                break;
            case 6:


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
            System.out.println("5. Rafraichir toutes les données des stations");
            System.out.println("6. Quitter l'application");
            do{
                try {
                    choice = scanner.nextInt();
                    validChoice = true;
                }catch (InputMismatchException wrongType) {
                    System.out.println("Choix invalide, veuillez choisir entre 1 et 4");
                    scanner.nextLine();
                }
            }while(!validChoice);
            choice(choice);
            scanner.nextLine();
        }while(choice != 6);
        scanner.close();
        System.out.println("A la prochaine !");
    }
}
