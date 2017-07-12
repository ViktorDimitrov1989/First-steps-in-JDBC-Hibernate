package NativeJDBCE;

import java.sql.*;

public class GetVillainNames {
    public static void main(String[] args) {

        try(
                Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
                Statement statement = connection.createStatement();
        ){
            statement.executeQuery(Constants.USING_DATABASE);

            String query = "SELECT v.name AS name, COUNT(m.id) AS minions_count\n" +
                    "\tFROM villains AS v\n" +
                    "\tINNER JOIN villains_minions AS vm\n" +
                    "\tON v.id = vm.villain_id\n" +
                    "\tINNER JOIN minions AS m\n" +
                    "\tON vm.minion_id = m.id\n" +
                    "\tGROUP BY v.name\n" +
                    "\tHAVING minions_count > 3\n" +
                    "\tORDER BY minions_count DESC";

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()){
                System.out.println(rs.getString("name") + " " + rs.getString("minions_count"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
