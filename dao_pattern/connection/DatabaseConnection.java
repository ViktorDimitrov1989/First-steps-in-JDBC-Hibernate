package dao_pattern.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/school?useSSL=false";
        String user = "root";
        String pass = "root";

        return DriverManager.getConnection(url, user, pass);
    }


}
