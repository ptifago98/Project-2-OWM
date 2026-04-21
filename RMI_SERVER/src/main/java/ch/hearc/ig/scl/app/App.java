package ch.hearc.ig.scl.app;

import ch.hearc.ig.scl.service.OWMManager;

import java.io.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            /* Création du registre RMI sur le port 1099 */
            Registry reg = LocateRegistry.createRegistry(1099);
            /* Création de l'objet distant */
            OWMManager obj = new OWMManager();
            /* Enregistrement de l'objet distant dans le registre */
            reg.rebind("OWMService", obj);
            System.out.println("Serveur prêt");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


    }
}
