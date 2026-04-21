package ch.hearc.ig.scl.repository;

import ch.hearc.ig.scl.business.Pays;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaysRepository {
    private static final String QUERY = "INSERT INTO PAYS (NOM,CODE) VALUES (?,?)";
    private final Connection CONNECTION;
    PreparedStatement myStatement;
    public PaysRepository(Connection connection) {
        this.CONNECTION = connection;
    }

    public void insert(Pays pays) throws SQLException {
        try {
            myStatement = CONNECTION.prepareStatement(QUERY);
            myStatement.setString(1,pays.getName());
            myStatement.setString(2,pays.getCode());

            int rowsAffected = myStatement.executeUpdate();
            if (rowsAffected == 0){
                throw new SQLException("insertion du pays immpossible");
            }
        } catch (SQLException e){
            throw e;
        }
    }
}
