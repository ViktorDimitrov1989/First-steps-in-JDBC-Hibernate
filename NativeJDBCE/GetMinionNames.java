package NativeJDBCE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class GetMinionNames {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int villainId = Integer.parseInt(reader.readLine());

        String query = "SELECT v.name AS villain_name, m.name AS minion_name, m.age AS minion_age \n" +
                "\tFROM minions AS m\n" +
                "\tINNER JOIN villains_minions AS vm ON m.id = vm.minion_id\n" +
                "\tINNER JOIN villains AS v ON v.id = vm.villain_id\n" +
                "\tWHERE v.id = ?";

        String villaiNameQuery = "SELECT name FROM villains WHERE id = ?";

        try(
                Connection connection = DriverManager.getConnection(Constants.URL,Constants.USERNAME,Constants.PASSWORD);
                PreparedStatement statement = connection.prepareStatement(query);
                PreparedStatement villainNameStatement = connection.prepareStatement(villaiNameQuery);
                ){

            statement.setInt(1,villainId);
            ResultSet rs = statement.executeQuery();

            villainNameStatement.setInt(1,villainId);
            ResultSet villainNameRs = villainNameStatement.executeQuery();

            String villainName = "";
            villainNameRs.next();
            try{
                villainName = villainNameRs.getString("name");

            }catch (SQLException e){
                System.out.println("No villain with ID 10 exists in the database.");
                return;
            }

            int cnt = 1;
            String res = String.format("Villain: %s\n", villainName);
            while (rs.next()){
                res += String.format("%s. %s %s\n", cnt++, rs.getString("minion_name"), rs.getString("minion_age"));
            }

            System.out.println(res);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
