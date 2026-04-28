package ch.hearc.ig.scl.service;

import ch.hearc.ig.scl.business.StationMeteo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;



public interface IOWMManager extends Remote{
    boolean insertAll(Double lat, Double lon) throws RemoteException;
    List<StationMeteo> getStations() throws RemoteException;
    StationMeteo getMeteo(String idStation) throws RemoteException;


}
