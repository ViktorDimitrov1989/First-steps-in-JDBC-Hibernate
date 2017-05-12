package fetching_results_exercises;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreator {

    public DatabaseCreator() {
    }

    public void createDataBase() {
        try(Connection connection = DriverManager.getConnection(Constants.URL, Constants.USER, Constants.PASS);
            Statement statement = connection.createStatement()){

            dropDb(statement);

            createMinionsDb(statement);

            useMinionsDb(statement);

            createTownsTable(statement);

            createMinionsTable(statement);

            createVillainsTable(statement);

            createMinionsVillainsRelationTable(statement);

            insertValuesIntoTowns(statement);
            insertValuesIntoMinions(statement);
            insertValuesIntoVillains(statement);
            insertValuesIntoMinionsVillainsTable(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertValuesIntoTowns(Statement statement) throws SQLException {
        String query;
        query = "INSERT INTO towns(name,country)" +
                "VALUES ('Sofia', 'Bulgaria')," +
                "('Vienna', 'Austria')," +
                "('Praha', 'Chech Republic')," +
                "('Rome','Italy')," +
                "('New York', 'US');";
        statement.execute(query);
    }

    private void insertValuesIntoMinions(Statement statement) throws SQLException {
        String query;
        query = "INSERT INTO minions(name,age,town_id)" +
                "VALUES" +
                "('Pesho',10,4)," +
                "('Kiro',20,2)," +
                "('Stefan',34,1)," +
                "('Miro',38,3)," +
                "('Penka',18,3)";
        statement.execute(query);
    }

    private void insertValuesIntoVillains(Statement statement) throws SQLException {
        String query;
        query = "INSERT INTO villains(name, evilness_factor)" +
                "VALUES" +
                "('Evil Evilov', 'good')," +
                "('Cruela De Vil', 'bad')," +
                "('Bai Pesho', 'super evil')," +
                "('Lelq Kina', 'evil')," +
                "('Boiko Borisov', 'super evil');";
        statement.execute(query);
    }

    private void insertValuesIntoMinionsVillainsTable(Statement statement) throws SQLException {
        String query;
        query = "INSERT INTO minions_villains(minion_id, villain_id)" +
                "VALUES" +
                "(1,2)," +
                "(2,5)," +
                "(3,3)," +
                "(4,1)," +
                "(5,4)";
        statement.execute(query);

    }

    private void createVillainsTable(Statement statement) throws SQLException {
        String query;
        query = "CREATE TABLE villains(" +
                "villain_id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(50)," +
                "evilness_factor ENUM('good','bad','evil','super evil'))";
        statement.execute(query);
    }

    private void createTownsTable(Statement statement) throws SQLException {
        String query;
        query = "CREATE TABLE towns(" +
                "town_id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(50)," +
                "country VARCHAR(50))";
        statement.execute(query);
    }

    private void createMinionsTable(Statement statement) throws SQLException {
        String query;
        query = "CREATE TABLE minions(" +
                "minion_id INT AUTO_INCREMENT," +
                "PRIMARY KEY(minion_id)," +
                "name VARCHAR(50)," +
                "age INT," +
                "town_id INT," +
                "CONSTRAINT fk_minions_towns FOREIGN KEY(town_id) REFERENCES towns(town_id))";
        statement.execute(query);
    }

    private void useMinionsDb(Statement statement) throws SQLException {
        String query;
        query = "USE minions_db";
        statement.execute(query);
    }

    private void createMinionsDb(Statement statement) throws SQLException {
        String query = "CREATE DATABASE minions_db";
        statement.executeUpdate(query);
    }

    private void dropDb(Statement statement) throws SQLException {
        String dropDBQuery = "DROP DATABASE IF EXISTS minions_db";
        statement.execute(dropDBQuery);
    }

    private void createMinionsVillainsRelationTable(Statement statement) throws SQLException {
        String query = "CREATE TABLE minions_villains(" +
                "minion_id INT," +
                "villain_id INT," +
                "PRIMARY KEY(minion_id, villain_id)," +
                "CONSTRAINT fk_minions_villains_minions FOREIGN KEY(minion_id)" +
                "REFERENCES minions(minion_id)," +
                "CONSTRAINT fk_minions_villains_villains FOREIGN KEY(villain_id)" +
                "REFERENCES villains(villain_id))";
        statement.execute(query);
    }
}
