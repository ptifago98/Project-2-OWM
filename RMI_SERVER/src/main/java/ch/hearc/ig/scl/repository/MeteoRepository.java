package ch.hearc.ig.scl.repository;

import ch.hearc.ig.scl.business.Meteo;
import ch.hearc.ig.scl.business.StationMeteo;
import ch.hearc.ig.scl.tools.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeteoRepository {
    private final Connection CONNECTION;

    public MeteoRepository(Connection connection) {
        this.CONNECTION = connection;
    }
    private int getStationNum(StationMeteo station) throws SQLException{
        final String QUERY = "SELECT NUMERO FROM STATION WHERE ID_STATION = ?";
        PreparedStatement myStatement;
        try{
            myStatement = CONNECTION.prepareStatement(QUERY);
            myStatement.setString(1,station.getIdStation());
            ResultSet result = myStatement.executeQuery();
            if (result.next()) {
                return result.getInt("NUMERO");
            } else {
                System.out.println("Aucune station trouvée avec l'ID: " + station.getIdStation());
                return 0;
            }
        }catch (SQLException e){
            System.out.println("Erreur lors de la requete meteoRepository.getStationNum");
            return 0;
        }

    }
    public boolean exists(Meteo meteo, StationMeteo station) throws SQLException{
        final String QUERY = "SELECT 1 FROM METEO WHERE NUM_STATION = ? AND DATE_MESURE = ?";
        PreparedStatement myStatement;
        try{
            myStatement = CONNECTION.prepareStatement(QUERY);
            myStatement.setInt(1,getStationNum(station));
            myStatement.setTimestamp(2, new Timestamp(meteo.getDate().getTime()));
            ResultSet result = myStatement.executeQuery();
            if(result.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            Log.warn(String.valueOf(e));
            return false;
        }
    }
    public void insert(Meteo meteo, StationMeteo station) throws SQLException {
        final String QUERY = """
            INSERT INTO METEO (DESCRIPTION, DATE_MESURE, TEMPERATURE, TEMP_RESSENTI,
                               PRESSION, HUMIDITE, VENT_VITESSE, VENT_ORIENTATION, ICON, NUM_STATION)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT NUMERO FROM STATION WHERE ? = NOM))
            """;
        PreparedStatement myStatement;
        try {
            myStatement = CONNECTION.prepareStatement(QUERY);
            myStatement.setString(1,meteo.getDescription());
            myStatement.setTimestamp(2, new Timestamp(meteo.getDate().getTime()));
            myStatement.setDouble(3,meteo.getTemperature());
            myStatement.setDouble(4,meteo.getTemperatureRessentie());
            myStatement.setInt(5,meteo.getPression());
            myStatement.setDouble(6,meteo.getHumidite());
            myStatement.setDouble(7,meteo.getVentVitesse());
            myStatement.setDouble(8,meteo.getVentOrientation());
            myStatement.setString(9,meteo.getIcon());
            myStatement.setString(10,station.getNom());

            int rowsAffected = myStatement.executeUpdate();
            if (rowsAffected == 0){
                throw new SQLException("insertion de la meteo immpossible");
            }
        } catch (SQLException e){
            throw e;
        }
    }
    public List<Meteo> getMeteo(String idStation) throws SQLException {
        final String QUERY = "SELECT * FROM METEO WHERE (NUM_STATION = (SELECT NUMERO FROM STATION WHERE ID_STATION = ?)) ORDER BY DATE_MESURE";

        List<Meteo> dataMeteo = new ArrayList<>();
        PreparedStatement myStatement;

        try {
            myStatement = CONNECTION.prepareStatement(QUERY);
            myStatement.setString(1, idStation);
            ResultSet result = myStatement.executeQuery();

            while (result.next()) {
                Meteo meteo = new Meteo(
                        result.getString("DESCRIPTION"),
                        result.getTimestamp("DATE_MESURE"),
                        result.getDouble("TEMPERATURE"),
                        result.getDouble("TEMP_RESSENTI"),
                        result.getInt("PRESSION"),
                        result.getDouble("HUMIDITE"),
                        result.getDouble("VENT_VITESSE"),
                        result.getDouble("VENT_ORIENTATION"),
                        result.getString("ICON")
                );
                dataMeteo.add(meteo);
            }
            return dataMeteo;


        } catch (SQLException e) {
            Log.warn(String.valueOf(e));
            return null;
        }
    }
}
