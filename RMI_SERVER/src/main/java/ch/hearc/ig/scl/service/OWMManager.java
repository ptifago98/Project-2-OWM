package ch.hearc.ig.scl.service;

import ch.hearc.ig.scl.business.Meteo;
import ch.hearc.ig.scl.business.StationMeteo;
import ch.hearc.ig.scl.deserializer.MeteoDeserializer;
import ch.hearc.ig.scl.deserializer.StationMeteoDeserializer;
import ch.hearc.ig.scl.persistence.DBConnection;
import ch.hearc.ig.scl.repository.MeteoRepository;
import ch.hearc.ig.scl.repository.PaysRepository;
import ch.hearc.ig.scl.repository.StationRepository;
import ch.hearc.ig.scl.tools.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.net.http.HttpResponse;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OWMManager extends UnicastRemoteObject implements IOWMManager {
    public OWMManager() throws RemoteException {
    }
    private Connection getConnection(){
        if (DBConnection.getConnection() == null) {
            Log.warn("Problème lors de la connexion à la base de données");
            return null;
        }else{
            return DBConnection.getConnection();
        }

    }
    // Persistance en base de données


    @Override
    public boolean insertAll(Double lat, Double lon) {
        Connection connection = getConnection();
        // Appel de l'API
        HttpResponse<String> response;
        try {
            response = ApiCallService.callAPI(lat, lon);
        }catch (NullPointerException e) {
            Log.warn("La clé d'API ou le lien n'est pas correcte");
            return false;
        }catch (RuntimeException e) {
            Log.warn("Vous n'êtes pas connecté à Internet ou au VPN de l'école");
            return false;
        }

        // Désérialisation avec un seul ObjectMapper
        Meteo meteo;
        StationMeteo station;
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Meteo.class, new MeteoDeserializer());
            module.addDeserializer(StationMeteo.class, new StationMeteoDeserializer());
            mapper.registerModule(module);

            String body = response.body();
            meteo   = mapper.readValue(body, Meteo.class);
            station = mapper.readValue(body, StationMeteo.class);
        } catch (JsonProcessingException e) {
            Log.warn("Erreur lors de la désérialisation : " + e.getMessage());
            return false;
        }

        // Validation des données désérialisées
        if (station.getPays() == null || station.getIdStation() == null) {
            Log.warn("Station invalide : pays ou identifiant manquant");
            return false;
        }


        try {
            connection.setAutoCommit(false);
            PaysRepository    paysRepo    = new PaysRepository(connection);
            StationRepository stationRepo = new StationRepository(connection);
            MeteoRepository   meteoRepo   = new MeteoRepository(connection);

            try {
                if(!paysRepo.exists(station.getPays())){
                    paysRepo.insert(station.getPays());
                }

            } catch (SQLException e) {
                // Le pays existe probablement déjà (contrainte d'unicité) → on continue
                Log.info("Pays déjà présent ou non inséré : " + e.getMessage());
            }

            try {
                if(!stationRepo.exists(station)){
                   stationRepo.insert(station, station.getPays());
                }


            } catch (SQLException e) {
                // La station existe probablement déjà → on continue
                Log.info("Station déjà présente ou non insérée : " + e.getMessage());
            }

            // L'insertion météo est critique : si elle échoue, on rollback
            if(meteoRepo.exists(meteo, station)){
                return false;
            }else{
                meteoRepo.insert(meteo, station);
            }
            connection.commit();
            return true;

        } catch (SQLException e) {
            Log.warn("Erreur SQL, rollback effectué : " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                Log.warn("Erreur lors du rollback : " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException closeEx) {
                Log.warn("Erreur lors de la fermeture de la connexion : " + closeEx.getMessage());
            }
        }
    }

    @Override
    public List<StationMeteo> getStations() throws RemoteException {
        Connection connection = getConnection();
        StationRepository stationRepo = new StationRepository(connection);

        try {
            return stationRepo.getAllStations();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la récupération des stations", e);
        }
    }

    @Override
    public StationMeteo getMeteo(String idStation) throws RemoteException{
        Connection connection = getConnection();
        StationRepository stationRepo = new StationRepository(connection);

        try {
            return stationRepo.getStation(idStation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
