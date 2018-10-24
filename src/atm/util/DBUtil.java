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

    public static boolean executeSql(final String sql, final String targetBank) {
        final String URL = URLHOLDER.replace("PLACEHOLDER", targetBank);
        try {
            final Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            final Connection connection = DriverManager.getConnection(URL, USER, PASS);
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean verifyLogin(final String kontonummer, final String password, final String targetBank) {
        final String sql = "SELECT * FROM user WHERE Kontonummer=" + kontonummer + " AND Passwort='" + password + "'";
        return executeSql(sql, targetBank);
    }

    public static ResultSet executeSqlWithResultSet(final String sql, final String targetBank) {
        final String URL = URLHOLDER.replace("PLACEHOLDER", targetBank);
        try {
            final Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            final Connection connection = DriverManager.getConnection(URL, USER, PASS);
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (Exception e) {
            return null;
        }
    }
}
