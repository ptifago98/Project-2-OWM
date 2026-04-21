package ch.hearc.ig.scl.repository;

import ch.hearc.ig.scl.business.Meteo;
import ch.hearc.ig.scl.business.StationMeteo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MeteoRepository {
    private static final String QUERY = """
            INSERT INTO METEO (DESCRIPTION, DATE_MESURE, TEMPERATURE, TEMP_RESSENTI,
                               PRESSION, HUMIDITE, VENT_VITESSE, VENT_ORIENTATION, ICON, NUM_STATION)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT NUMERO FROM STATION WHERE ? = NOM))
            """;
    private final Connection CONNECTION;
    PreparedStatement myStatement;
    public MeteoRepository(Connection connection) {
        this.CONNECTION = connection;
    }

    public void insert(Meteo meteo, StationMeteo station) throws SQLException {

        try {
            myStatement = CONNECTION.prepareStatement(QUERY);
            myStatement.setString(1,meteo.getDescription());
            myStatement.setDate(2, new Date(meteo.getDate().getTime()));
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
}
