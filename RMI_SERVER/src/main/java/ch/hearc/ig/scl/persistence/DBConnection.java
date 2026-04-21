package ch.hearc.ig.scl.persistence;

import ch.hearc.ig.scl.tools.EnvProperties;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static final String SID = EnvProperties.get("SID");
    private static final String HOST = EnvProperties.get("HOST");
    private static final String PORT = EnvProperties.get("PORT");
    private static final String USER = EnvProperties.get("USER");
    private static final String PASSWORD = EnvProperties.get("PASSWORD");




    private static PoolDataSource pds = null;

    public static Connection getConnection() {
        try{
            if (pds == null){
                pds = PoolDataSourceFactory.getPoolDataSource();

                pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
                pds.setURL("jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + SID);
                pds.setUser(USER);
                pds.setPassword(PASSWORD);

                pds.setInitialPoolSize(5);
                pds.setMinPoolSize(1);
                pds.setMaxPoolSize(10);

                pds.setInactiveConnectionTimeout(300);
                pds.setTimeToLiveConnectionTimeout(3600);
                pds.setAbandonedConnectionTimeout(300);
                pds.setTimeoutCheckInterval(60);

            }
            return pds.getConnection();



        } catch (SQLException e) {
            return null;
        }


    }
}
