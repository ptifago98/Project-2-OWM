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

public class OWMManager extends UnicastRemoteObject implements IOWMManager {
    public OWMManager() throws RemoteException {
    }

    @Override
    public boolean insertAll(Double lat, Double lon) {
        // Appel de l'API
        HttpResponse<String> response;
        try {
            response = ApiCallService.callAPI(lat, lon);
        } catch (NullPointerException e) {
            Log.warn("La clé d'API ou le lien n'est pas correcte");
            return false;
        } catch (RuntimeException e) {
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

        // Persistance en base de données
        Connection connection = DBConnection.getConnection();
        if (connection == null) {
            Log.warn("Problème lors de la connexion à la base de données");
            return false;
        }

        try {
            connection.setAutoCommit(false);

            PaysRepository    paysRepo    = new PaysRepository(connection);
            StationRepository stationRepo = new StationRepository(connection);
            MeteoRepository   meteoRepo   = new MeteoRepository(connection);

            try {
                paysRepo.insert(station.getPays());
            } catch (SQLException e) {
                // Le pays existe probablement déjà (contrainte d'unicité) → on continue
                Log.info("Pays déjà présent ou non inséré : " + e.getMessage());
                return false;
            }

            try {
                stationRepo.insert(station, station.getPays());
            } catch (SQLException e) {
                // La station existe probablement déjà → on continue
                Log.info("Station déjà présente ou non insérée : " + e.getMessage());
                return false;
            }

            // L'insertion météo est critique : si elle échoue, on rollback
            meteoRepo.insert(meteo, station);

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
}
