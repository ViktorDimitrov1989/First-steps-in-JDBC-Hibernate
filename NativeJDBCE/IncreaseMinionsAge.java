package NativeJDBCE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class IncreaseMinionsAge {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] ids = reader.readLine().split("\\s+");

        String selectAllMinionsQuery = "SELECT name, age FROM minions";

        try(Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD)){

            for (String id : ids) {
                incrementAge(connection,id);
            }

            Statement statement = connection.createStatement();
            ResultSet minionsRs = statement.executeQuery(selectAllMinionsQuery);

            while (minionsRs.next()){
                System.out.println(minionsRs.getString("name") + " " + minionsRs.getString("age"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private static void incrementAge(Connection connection, String id) throws SQLException {
        String query = "UPDATE minions \n" +
                "\tSET name = CONCAT(UCASE(LEFT(name, 1)), LCASE(SUBSTRING(name, 2))), age = age + 1\n" +
                "\t WHERE id = ?";

        PreparedStatement updateStatement = connection.prepareStatement(query);
        updateStatement.setInt(1, Integer.parseInt(id));

        updateStatement.executeUpdate();
        updateStatement.close();
    }

}
