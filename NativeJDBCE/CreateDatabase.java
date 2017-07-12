package NativeJDBCE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {


    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD)){
            createDB(connection);
            createTables(connection);
            insertDataIntoTables(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) {
        String countriesTableCreateSql = "CREATE TABLE IF NOT EXISTS `countries`(" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50))";

        String townsTableCreateSQL = "CREATE TABLE IF NOT EXISTS `towns`(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(50)," +
                "country_id INT," +
                "CONSTRAINT FOREIGN KEY (country_id) REFERENCES countries (id))";

        String minionsTableCreateSql = "CREATE TABLE IF NOT EXISTS `minions`(" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50)," +
                "age INT," +
                "town_id INT," +
                "CONSTRAINT FOREIGN KEY (town_id) REFERENCES towns (id));";

        String villainsTableCreateQuery = "CREATE TABLE IF NOT EXISTS `villains`(" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50)," +
                "evilness_factor VARCHAR(50) NOT NULL CHECK (evilness_factor IN ('good', 'bad', 'evil', 'super evil')))";

        String villainsMinionsTableCreateQuery = "CREATE TABLE IF NOT EXISTS villains_minions(" +
                "minion_id INT," +
                "villain_id INT," +
                "CONSTRAINT pk_villains_minions PRIMARY KEY(minion_id,villain_id)," +
                "CONSTRAINT fk_villains_minions_villains FOREIGN KEY(villain_id) REFERENCES villains (id)," +
                "CONSTRAINT fk_villains_minions_minions FOREIGN KEY(minion_id) REFERENCES minions (id))";

        try(Statement statement = connection.createStatement()){
            statement.executeQuery("USE minionsdb");
            statement.executeUpdate(countriesTableCreateSql);
            statement.executeUpdate(townsTableCreateSQL);
            statement.executeUpdate(minionsTableCreateSql);
            statement.executeUpdate(villainsTableCreateQuery);
            statement.executeUpdate(villainsMinionsTableCreateQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private static void createDB(Connection connection){
        String query = "CREATE DATABASE IF NOT EXISTS MinionsDB";

        try(Statement statement = connection.createStatement()){
            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void insertDataIntoTables(Connection connection){
        String insertIntoCountriesQuery = "INSERT INTO countries(name)" +
                "VALUES('Bulgaria'),('Austria'), ('Chech Republic'), ('Italy'), ('US')";

        String insertIntoTownsQuery = "INSERT INTO towns(name,country_id)" +
                "VALUES ('Sofia', 1)," +
                "('Vienna', 2)," +
                "('Praha', 3)," +
                "('Rome', 4)," +
                "('New York', 5);";

        String insertIntoMinionsQuery = "INSERT INTO minions(name,age,town_id)" +
                "VALUES" +
                "('Pesho',10,4)," +
                "('Kiro',20,2)," +
                "('Stefan',34,1)," +
                "('Miro',38,3)," +
                "('Penka',18,3)";

        String insertIntoVillainsQuery = "INSERT INTO villains(name, evilness_factor)" +
                "VALUES" +
                "('Evil Evilov', 'good')," +
                "('Cruela De Vil', 'bad')," +
                "('Bai Pesho', 'super evil')," +
                "('Lelq Kina', 'evil')," +
                "('Boiko Borisov', 'super evil');";

        String insertIntoMinionsVillains = "INSERT INTO villains_minions(minion_id, villain_id)" +
                "VALUES" +
                "(1,2)," +
                "(2,5)," +
                "(3,3)," +
                "(4,1)," +
                "(5,4)";


        try(Statement statement = connection.createStatement()){
            statement.executeUpdate(insertIntoCountriesQuery);
            statement.executeUpdate(insertIntoTownsQuery);
            statement.executeUpdate(insertIntoMinionsQuery);
            statement.executeUpdate(insertIntoVillainsQuery);
            statement.executeUpdate(insertIntoMinionsVillains);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
