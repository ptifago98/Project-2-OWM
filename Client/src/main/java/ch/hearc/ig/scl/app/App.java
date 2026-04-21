package ch.hearc.ig.scl.app;
import ch.hearc.ig.scl.service.IOWMManager;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Double lat = Double.NaN;
        Double lon = Double.NaN;
        String host = "localhost";
        int port = 1099
                ;
        try
        {
            Registry reg = LocateRegistry.getRegistry(host, port);
            /* Récupération de l'objet distant */
            IOWMManager obj = (IOWMManager) reg.lookup("OWMService");
            /* Appel de la méthode distante */
            // Saisie latitude
            do {
                try{
                    System.out.print("Entrer une latitude : ");
                    lat = sc.nextDouble();

                    if (lat < -90 || lat > 90){
                        System.out.println("La latitude être comprise entre -90 et +90");
                        lat = Double.NaN;
                    }
                } catch(InputMismatchException wrongType){
                    System.out.println("La latitude être comprise entre -90 et +90");
                    sc.nextLine();
                }

            } while (Double.isNaN(lat));

            // Saisie longitude
            do {
                try {
                    System.out.print("Entrer une longitude : ");
                    lon = sc.nextDouble();
                    if (lon < -180 || lon > 180) {
                        System.out.println("La longitude être comprise entre -180 et +180");
                        lon = Double.NaN;
                    }
                }catch (InputMismatchException wrongType){
                    System.out.println("La longitude être comprise entre -180 et +180");
                    sc.nextLine();
                }
            } while (Double.isNaN(lon));

            if(obj.insertAll(lat,lon)){
                System.out.println("Enregistré!");
            }else{
                System.out.println("Pas enregistré!");
            }
            sc.close();
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);

        }
    }
}
