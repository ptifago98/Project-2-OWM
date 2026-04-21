package ch.hearc.ig.scl.repository;

import ch.hearc.ig.scl.business.Pays;
import ch.hearc.ig.scl.business.StationMeteo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StationRepository {
    private static final String QUERY = "INSERT INTO STATION (ID_STATION, TIME_ZONE,LATITUDE,LONGITUDE,NOM,NUM_PAYS) VALUES (?,?,?,?,?,(SELECT NUMERO FROM PAYS WHERE ? = CODE))";
    private final Connection CONNECTION;
    PreparedStatement myStatement;
    public StationRepository(Connection connection) {
        this.CONNECTION = connection;
    }

    public void insert(StationMeteo station,Pays pays) throws SQLException {
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

            throw e;
        }
    }
}
