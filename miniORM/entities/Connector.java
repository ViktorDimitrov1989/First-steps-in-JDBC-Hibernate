package miniORM.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {

    private static Connection connection = null;

    public static void initConnection(String driver,
                                      String host,
                                      String port,
                                      String dbName,
                                      String username,
                                      String password) throws SQLException {
        /*Properties connectionProperties = new Properties();
        connectionProperties.put("username", username);
        connectionProperties.put("password", password);*/

        String connectionString = "jdbc:" + driver + "://" + host + ":" + port + "/" + dbName;

        connection = DriverManager.getConnection(connectionString,username,password);
    }

    public static Connection getConnection() {
        return connection;
    }

}
