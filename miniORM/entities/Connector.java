package miniORM.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    private static Connection connection = null;

    public static void initConnection(String driver,
                                      String host,
                                      String port,
                                      String dbName,
                                      String username,
                                      String password) throws SQLException {

        String connectionString = "jdbc:" + driver + "://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        connection = DriverManager.getConnection(connectionString,username,password);
    }

    public static Connection getConnection() {
        return connection;
    }

}
