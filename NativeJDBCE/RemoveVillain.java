package NativeJDBCE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class RemoveVillain {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Integer villainId = Integer.parseInt(reader.readLine());


        String removeFromMappingTableQuery = "DELETE FROM villains_minions WHERE villain_id = ?";
        String removeFromVillainsQuery = "DELETE FROM villains WHERE id = ?";
        String selectVillain = "SELECT * FROM villains WHERE id = ?";

        try(Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD)){
            PreparedStatement selectVillainStatement = connection.prepareStatement(selectVillain);
            selectVillainStatement.setInt(1,villainId);

            ResultSet resultVillain = selectVillainStatement.executeQuery();


            if(resultVillain.next()){
                String villainName = resultVillain.getString("name");
                PreparedStatement deleteMapTableStatement = connection.prepareStatement(removeFromMappingTableQuery);
                deleteMapTableStatement.setInt(1, villainId);
                int affectedRows = deleteMapTableStatement.executeUpdate();
                deleteMapTableStatement.close();

                PreparedStatement deleteFromVillainStmt = connection.prepareStatement(removeFromVillainsQuery);
                deleteFromVillainStmt.setInt(1,villainId);
                deleteFromVillainStmt.executeUpdate();
                deleteFromVillainStmt.close();

                System.out.printf("%s was deleted.\n", villainName);
                System.out.printf("%d minions released.\n", affectedRows);

            }else{
                System.out.println("No such villain was found.\n");
            }

            selectVillainStatement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
