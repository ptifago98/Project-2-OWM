package ch.hearc.ig.scl.repository;

import ch.hearc.ig.scl.business.Pays;
import ch.hearc.ig.scl.tools.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaysRepository {

    private final Connection CONNECTION;

    public PaysRepository(Connection connection) {
        this.CONNECTION = connection;
    }

    public boolean exists(Pays pays) throws SQLException{
        final String QUERY = "SELECT 1 FROM PAYS WHERE NOM = ?";
        PreparedStatement myStatement;
        try{
            myStatement = CONNECTION.prepareStatement(QUERY);
            myStatement.setString(1,pays.getName());
            ResultSet result = myStatement.executeQuery();
            if(result.next()){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e) {
            Log.warn(String.valueOf(e));
            return false;
        }
    }
    public void insert(Pays pays) throws SQLException {
        final String QUERY = "INSERT INTO PAYS (NOM,CODE) VALUES (?,?)";
        PreparedStatement myStatement;
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
