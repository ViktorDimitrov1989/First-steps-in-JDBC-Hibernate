package NativeJDBCE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class AddMinion {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String minionInfo[] = reader.readLine().split("\\s");
        String minionname = minionInfo[1];
        int minionAge = Integer.parseInt(minionInfo[2]);
        String townName = minionInfo[3];


        String villainInfo[] = reader.readLine().split("\\s");
        String villainName = villainInfo[1];

        String selectTownQuery = "SELECT * FROM towns WHERE name = ?";
        String selectVillainQury = "SELECT * FROM villains WHERE name = ?";
        String selectMinionQuery = "SELECT * FROM minions WHERE name = ?";

        String insertTownQuery = "INSERT INTO towns(name) VALUES(?)";
        String insertVillainsQuery = "INSERT INTO villains(name, evilness_factor) VALUES(?,'evil')";

        String insertIntoMinionsQuery = "INSERT INTO minions(name,age,town_id) VALUES(?,?,?)";
        String insertMapTableQuery = "INSERT INTO villains_minions(minion_id, villain_id) VALUES(?,?)";


        try(
                Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
                Statement updateStatement = connection.createStatement();
                ){
            PreparedStatement townStatement = connection.prepareStatement(selectTownQuery);
            townStatement.setString(1, townName);
            ResultSet townResult = townStatement.executeQuery();

             if(!townResult.next()){
                 PreparedStatement insertTownStatement = connection.prepareStatement(insertTownQuery);
                 insertTownStatement.setString(1,townName);
                 insertTownStatement.executeUpdate();
                 System.out.printf("Town %s was added to the database.\n", townName);
                 insertTownStatement.close();
             }

            int townId = 0;
            ResultSet townResultUpdate = townStatement.executeQuery();

            if(townResultUpdate.next()){
                townId = townResultUpdate.getInt("id");
            }
            townStatement.close();

            PreparedStatement selectVillainStatement = connection.prepareStatement(selectVillainQury);
            selectVillainStatement.setString(1,villainName);

            ResultSet villainResult = selectVillainStatement.executeQuery();

            if(!villainResult.next()){
                PreparedStatement insertVillainStatement = connection.prepareStatement(insertVillainsQuery);
                insertVillainStatement.setString(1,villainName);
                insertVillainStatement.executeUpdate();
                insertVillainStatement.close();
            }

            int villainId = 0;
            ResultSet villainResultUpdated = selectVillainStatement.executeQuery();
            if(villainResultUpdated.next()){
                villainId = villainResultUpdated.getInt("id");
                System.out.printf("Villain %s was added to the database.\n", villainName);
            }
            selectVillainStatement.close();

            PreparedStatement insertMinionPS = connection.prepareStatement(insertIntoMinionsQuery);
            insertMinionPS.setString(1, minionname);
            insertMinionPS.setInt(2, minionAge);
            insertMinionPS.setInt(3, townId);
            insertMinionPS.executeUpdate();
            insertMinionPS.close();

            PreparedStatement selectMinionStatement = connection.prepareStatement(selectMinionQuery);
            selectMinionStatement.setString(1, minionname);

            ResultSet minionResult = selectMinionStatement.executeQuery();

            int minionId = 0;
            if(minionResult.next()){
                minionId = minionResult.getInt("id");
            }
            selectMinionStatement.close();

            PreparedStatement insertIntoMappingTable = connection.prepareStatement(insertMapTableQuery);
            insertIntoMappingTable.setInt(1,minionId);
            insertIntoMappingTable.setInt(2, villainId);
            insertIntoMappingTable.executeUpdate();
            insertIntoMappingTable.close();

            System.out.printf("Successfuly added %s to be minion of %s.\n", minionname, villainName);


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
