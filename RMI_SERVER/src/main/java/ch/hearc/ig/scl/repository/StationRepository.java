package ch.hearc.ig.scl.repository;

import ch.hearc.ig.scl.business.Meteo;
import ch.hearc.ig.scl.business.Pays;
import ch.hearc.ig.scl.business.StationMeteo;
import ch.hearc.ig.scl.service.ApiCallPaysService;
import ch.hearc.ig.scl.tools.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StationRepository {
    private final Connection CONNECTION;

    public StationRepository(Connection connection) {
        this.CONNECTION = connection;
    }
    public boolean exists(StationMeteo station) throws SQLException {
        return exists(station.getIdStation());
    }

    public boolean exists(String idStation) throws SQLException {
        final String QUERY = "SELECT 1 FROM STATION WHERE ID_STATION = ?";

        try {
            PreparedStatement myStatement = CONNECTION.prepareStatement(QUERY);
            myStatement.setString(1, idStation);
            ResultSet result = myStatement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            Log.warn(String.valueOf(e));
            return false;
        }
    }

    public void insert(StationMeteo station,Pays pays) throws SQLException {
        final String QUERY = "INSERT INTO STATION (ID_STATION, TIME_ZONE,LATITUDE,LONGITUDE,NOM,NUM_PAYS) VALUES (?,?,?,?,?,(SELECT NUMERO FROM PAYS WHERE ? = CODE))";
        PreparedStatement myStatement;
        try {

            myStatement = CONNECTION.prepareStatement(QUERY);
            myStatement.setString(1,station.getIdStation());
            myStatement.setInt(2,station.getTimeZone());
            myStatement.setDouble(3,station.getLatitude());
            myStatement.setDouble(4,station.getLongitude());
            myStatement.setString(5,station.getNom());
            myStatement.setString(6,pays.getCode());

            int rowsAffected = myStatement.executeUpdate();
            if (rowsAffected == 0){
                throw new SQLException("insertion de la station impossible");
            }
        } catch (SQLException e){
                Log.warn(String.valueOf(e));
            throw e;
        }
    }
    public List<StationMeteo> getAllStations() throws SQLException {
        final String QUERY = "SELECT S.ID_STATION, S.TIME_ZONE, S.LATITUDE, S.LONGITUDE, S.NOM, P.CODE FROM STATION S JOIN PAYS P ON P.NUMERO = S.NUM_PAYS";
        String stationResult;
        List<StationMeteo> stations = new ArrayList<>();
        PreparedStatement myStatement;

        try{
            myStatement = CONNECTION.prepareStatement(QUERY);
            ResultSet result = myStatement.executeQuery();
            //Pays pays = new Pays();


            while (result.next()) {
                //pays.setCode(result.getString("CODE"));
                //ApiCallPaysService.callApiName(pays);
                StationMeteo stationMeteo = new StationMeteo(
                        result.getString("ID_STATION"),
                        result.getInt("TIME_ZONE"),
                        null,
                        result.getDouble("LATITUDE"),
                        result.getDouble("LONGITUDE"),
                        result.getString("NOM")
                );
                stations.add(stationMeteo);
            }

            return stations;
        }catch (SQLException e){
            Log.warn(String.valueOf(e));
            return null;
        }

    }
    public StationMeteo getStation(String stationId) throws SQLException{
        if(!exists(stationId)){
            return null;
        }
        final String QUERY = "SELECT S.*, P.CODE FROM STATION S JOIN PAYS P ON P.NUMERO = S.NUM_PAYS WHERE ID_STATION = ?";
        MeteoRepository meteoRepo = new MeteoRepository(CONNECTION);
        List<Meteo> dataMeteo = meteoRepo.getMeteo(stationId);

        try{
            PreparedStatement myStatement = CONNECTION.prepareStatement(QUERY);
            myStatement.setString(1, stationId);
            ResultSet result = myStatement.executeQuery();

            if (!result.next()) {
                return null;
            }

            Pays pays = new Pays();
            pays.setCode(result.getString("CODE"));
            ApiCallPaysService.callApiName(pays);

            StationMeteo stationMeteo = new StationMeteo(
                    result.getString("ID_STATION"),
                    result.getInt("TIME_ZONE"),
                    pays,
                    result.getDouble("LATITUDE"),
                    result.getDouble("LONGITUDE"),
                    result.getString("NOM")
            );

            for (Meteo m : dataMeteo){
                stationMeteo.addWeather(m);
            }

            return stationMeteo;
        }catch (SQLException e){
            Log.warn(String.valueOf(e));
            return null;
        }
    }
}
