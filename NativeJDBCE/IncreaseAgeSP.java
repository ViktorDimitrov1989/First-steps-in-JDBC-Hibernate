package NativeJDBCE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class IncreaseAgeSP {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int id = Integer.parseInt(reader.readLine());

        String invokeProcedure = "CALL usp_get_older (?)";
        String selectMinionById = "SELECT name, age FROM minions WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD)){
            CallableStatement invokeProcedureStatement = connection.prepareCall(invokeProcedure);

            invokeProcedureStatement.setInt(1,id);
            invokeProcedureStatement.execute();
            invokeProcedureStatement.close();

            PreparedStatement selectMinionByIdPS = connection.prepareCall(selectMinionById);
            selectMinionByIdPS.setInt(1,id);
            ResultSet selectedMinion = selectMinionByIdPS.executeQuery();

            if(selectedMinion.next()){
                System.out.println(String.format("Name: %s, Age: %s", selectedMinion.getString("name"), selectedMinion.getString("age")));
            }
            selectMinionByIdPS.close();



        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
