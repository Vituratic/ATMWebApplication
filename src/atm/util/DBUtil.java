package atm.util;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {

    private static final String URLHOLDER = "jdbc:mysql://localhost:3306/PLACEHOLDER?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Best_Team1337";

    public static boolean executeSql(final String sql, String targetBank) {
        final String URL = URLHOLDER.replace("PLACEHOLDER", targetBank);
        try {
            Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            final Connection connection = DriverManager.getConnection(URL, USER, PASS);
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean verifyLogin(final String kontonummer, final String password, String targetBank) {
        final String sql = "SELECT * FROM user WHERE Kontonummer=" + kontonummer + " AND Passwort='" + password + "'";
        if (executeSql(sql, targetBank)) {
            return true;
        }
        return false;
    }

    public static ResultSet executeSqlWithResultSet(final String sql, String targetBank) {
        final String URL = URLHOLDER.replace("PLACEHOLDER", targetBank);
        try {
            final Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            final Connection connection = DriverManager.getConnection(URL, USER, PASS);
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (Exception e) {
            return null;
        }
    }
}
