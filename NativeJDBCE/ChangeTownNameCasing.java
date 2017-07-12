package NativeJDBCE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChangeTownNameCasing {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String countryName = reader.readLine();

        String updateTownsByCountryQuery = "UPDATE towns AS t \n" +
                "\tINNER JOIN countries AS c \n" +
                "\tON t.country_id = c.id \n" +
                "   SET t.name = UPPER(t.name) \n" +
                "   WHERE c.name = ?";

        String selectModifiedTowns = "SELECT t.name AS name FROM towns AS t\n" +
                "\tINNER JOIN countries AS c\n" +
                "\tON t.country_id = c.id\n" +
                "\tWHERE c.name = ?";


        try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD)) {

            PreparedStatement updateTownsStatement = connection.prepareStatement(updateTownsByCountryQuery);
            updateTownsStatement.setString(1, countryName);

            PreparedStatement selectTownsStatement = connection.prepareStatement(selectModifiedTowns);
            selectTownsStatement.setString(1, countryName);
            ResultSet selectUpdatedTowns = selectTownsStatement.executeQuery();

            List<String> towns = new ArrayList<>();

            int affectedRows = updateTownsStatement.executeUpdate();

            if (selectUpdatedTowns.next()) {
                towns.add(selectUpdatedTowns.getString("name").toUpperCase());

                System.out.printf("%d town names were affected.\n", affectedRows);
                System.out.println(towns);

                updateTownsStatement.close();
            } else {

                System.out.println("No town names were affected.");
            }
            selectTownsStatement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
