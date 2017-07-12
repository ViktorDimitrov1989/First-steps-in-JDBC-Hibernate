package fetching_results_exercises;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //DatabaseCreator dbCreator = new DatabaseCreator();
        //dbCreator.createDataBase();
        try(Connection connection = DriverManager.getConnection(Constants.URL, Constants.USER, Constants.PASS);
             Statement statement = connection.createStatement()) {
            statement.execute("USE minions_db");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            deleteVillainById(reader, connection);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void deleteVillainById(BufferedReader reader, Connection connection) throws IOException, SQLException {
        int idToDelete = Integer.parseInt(reader.readLine());

        PreparedStatement deleteFromVillains = null;
        PreparedStatement deleteFromMinionsVillains = null;
        PreparedStatement releasedMinionsCnt = null;
        PreparedStatement getVillainName = null;

        String deleteVillainQuery = String.format("DELETE FROM villains\n" +
                "\tWHERE villain_id = %d", idToDelete);

        String getMinionsCountQuery = String.format("SELECT COUNT(*) AS count FROM minions AS m\n" +
                "\tINNER JOIN minions_villains AS mv ON m.minion_id = mv.minion_id\n" +
                "\tINNER JOIN villains AS v ON mv.villain_id = v.villain_id\n" +
                "\tWHERE v.villain_id = %d", idToDelete);

        String deleteMinionsQuery = String.format("DELETE FROM minions_villains\n" +
                "\tWHERE villain_id = %d", idToDelete);

        String getVillainNameQuery = String.format("SELECT name FROM villains WHERE villain_id = %d", idToDelete);

        try{
            Savepoint save1 = connection.setSavepoint();

            connection.setAutoCommit(false);
            releasedMinionsCnt = connection.prepareStatement(getMinionsCountQuery);
            deleteFromMinionsVillains = connection.prepareStatement(deleteMinionsQuery);
            deleteFromVillains = connection.prepareStatement(deleteVillainQuery);
            getVillainName = connection.prepareStatement(getVillainNameQuery);

            int releasedCnt;
            String villainName = "";

            ResultSet minionsCountRs = releasedMinionsCnt.executeQuery();
            if(minionsCountRs.next()){
                releasedCnt = minionsCountRs.getInt("count");
            }else{
                releasedCnt = 0;
            }

            ResultSet villainNameRs = getVillainName.executeQuery();
            if(villainNameRs.next()){
                villainName = villainNameRs.getString("name");
            }else{
                connection.rollback(save1);
            }

            deleteFromMinionsVillains.executeUpdate();
            deleteFromVillains.executeUpdate();

            connection.commit();
            System.out.println(String.format("%s was deleted", villainName));
            System.out.println(String.format("%d minions released", releasedCnt));
        } catch (SQLException e) {
            System.out.println("No such villain was found");
            e.printStackTrace();
        }finally {
            if(deleteFromVillains != null){
                deleteFromVillains.close();
            }
            if(deleteFromMinionsVillains != null){
                deleteFromMinionsVillains.close();
            }
            if(releasedMinionsCnt != null){
                releasedMinionsCnt.close();
            }
            if(getVillainName != null){
                getVillainName.close();
            }

            connection.setAutoCommit(true);
        }

    }

    private static void updateTownsToUpper(Connection connection, BufferedReader reader) throws SQLException, IOException {
        String country = reader.readLine();

        String updateQuery = String.format("UPDATE towns " +
                "SET name = UPPER(name) " +
                "WHERE country = '%s'", country);

        PreparedStatement ps = connection.prepareStatement(updateQuery);

        int updatedSize = ps.executeUpdate(updateQuery);

        String selectQuery = String.format("SELECT name FROM towns WHERE UPPER(country) = '%s'", country);

        ResultSet rs = ps.executeQuery(selectQuery);

        List updatedTowns = new ArrayList<>();
        while (rs.next()) {
            String townName = rs.getString("name");

            updatedTowns.add(townName);
        }
        if(updatedSize == 0){
            System.out.println("No town names were affected.");
        }else{
            System.out.println(String.format("%d town names were affected.\n%s",updatedSize, updatedTowns));
        }

    }

    private static void addMinionsToVillains(Statement statement) throws IOException, SQLException {
       /* BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] minionTokens = reader.readLine().split(":")[1].trim().split("\\s+");
        String villainName = reader.readLine().split(":")[1].trim();

        String townName = minionTokens[2];
        String minionName = minionTokens[0];
        int minionAge = Integer.parseInt(minionTokens[1]);


        int villainId = 0;
        int minionId = 0;
        int townId = 0;

        ResultSet isTownPresent = statement.executeQuery("SELECT * FROM towns WHERE name = '" + townName + "'");
        if (!isTownPresent.next()) {
            townId = executeInsertQuery("INSERT INTO towns(name, country) VALUES ('" + townName + "', NULL )", statement);
            System.out.printf("Town %s was added to the database.\n", townName);
        } else {
            ResultSet townIdRs = statement.executeQuery("SELECT town_id FROM towns WHERE name = '" + townName + "'");
            if (townIdRs.next()) {
                townId = townIdRs.getInt("town_id");
            }
        }

        ResultSet isVillainPresent = statement.executeQuery("SELECT * FROM villains WHERE name = '" + villainName + "'");
        if (!isVillainPresent.next()) {
            villainId = executeInsertQuery(String.format("INSERT INTO villains(name, evilness_factor) VALUES ('%s', '%s')", villainName, "evil"), statement);

            System.out.printf("Villain %s was added to the database.\n", villainName);
        } else {
            ResultSet villainIdRs = statement.executeQuery("SELECT villain_id FROM villains WHERE name = '" + villainName + "'");
            if (villainIdRs.next()) {
                villainId = villainIdRs.getInt("villain_id");
            }
        }

        ResultSet isMinionPresent = statement.executeQuery("SELECT * FROM minions WHERE name = '" + minionName + "' AND age = '" + minionAge + "'");
        if (!isMinionPresent.next()) {
            minionId = executeInsertQuery(String.format("INSERT INTO minions(name,age,town_id) VALUES ('%s', '%d', '%d')", minionName, minionAge, townId), statement);
            System.out.printf("Minion %s successfully added to the database.\n", minionName);
        } else {
            ResultSet minionIdRs = statement.executeQuery("SELECT minion_id FROM minions WHERE name = '" + minionName + "'");
            if (minionIdRs.next()) {
                minionId = minionIdRs.getInt("minion_id");
            }
        }


        statement.execute(String.format("INSERT INTO minions_villains(minion_id, villain_id) VALUES(%d, %d)", minionId, villainId));
        System.out.printf("Successfully added %s to be minion of %s\n", minionName, villainName);*/
    }

    private static int executeInsertQuery(String query, Statement statement) throws SQLException {
        int result = -1;

        statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            result = resultSet.getInt(1);
        }

        return result;
    }

    private static void getMinionsByVillainId(Statement statement) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int villainId = Integer.parseInt(reader.readLine());

        String getVillainQuery = "SELECT name FROM villains WHERE villain_id=" + villainId;
        ResultSet villainResult = statement.executeQuery(getVillainQuery);

        String villainName = "";
        if (villainResult.next()) {
            villainName = villainResult.getString("name");
        }


        ResultSet minionsResult = statement.executeQuery("CALL udp_get_minion_name_by_villain_id(" + villainId + ")");

        int counter = 1;

        System.out.println("Villain: " + villainName);
        while (minionsResult.next()) {
            String minionName = minionsResult.getString("name");
            int age = minionsResult.getInt("age");
            System.out.printf(String.format("%d. %s %d\n", counter++, minionName, age));
        }
    }

    private static void getVillainNames(Statement statement) throws SQLException {

        String query = "SELECT name, COUNT(mv.minion_id) AS count FROM villains AS v " +
                "INNER JOIN minions_villains AS mv " +
                "ON v.villain_id = mv.villain_id " +
                "GROUP BY name " +
                "HAVING COUNT(mv.minion_id) > 0";

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            System.out.println(resultSet.getString("name") + " " + resultSet.getInt("count"));
        }
    }

    private static void createProcedureToFetchMinionsByVillainId(Statement statement) throws SQLException {
        String query = " CREATE PROCEDURE udp_get_minion_name_by_villain_id(villain_id INT)" +
                " BEGIN" +
                " SELECT m.name AS name, m.age AS age FROM minions AS m" +
                " INNER JOIN minions_villains AS mv" +
                " ON m.minion_id = mv.minion_id" +
                " WHERE mv.villain_id = villain_id;" +
                " END";

        statement.execute(query);
    }

}
