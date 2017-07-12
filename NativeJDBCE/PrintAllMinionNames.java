package NativeJDBCE;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrintAllMinionNames {
    public static void main(String[] args) {
        String query = "SELECT name FROM minions";

        try(Connection connection = DriverManager.getConnection(Constants.URL,Constants.USERNAME, Constants.USERNAME)){
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            List<String> names = new ArrayList<>();

            while(rs.next()){
                names.add(rs.getString("name"));
            }

            int first = 0;
            int second = names.size() - 1;
            for (int i = 0; i < names.size() / 2; i++) {
                first += i;
                second -= i;
                System.out.println(names.get(first));
                System.out.println(names.get(second));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
