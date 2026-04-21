package ch.hearc.ig.scl.service;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IOWMManager extends Remote{
    boolean insertAll(Double lat, Double lon) throws RemoteException;


}
