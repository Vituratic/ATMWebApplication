package atm.util;

import com.mysql.jdbc.Driver;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/banka?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Best_Team1337";

    public static boolean executeSql(final String sql) {
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

    public static boolean verifyLogin(final String kontonummer, final String password) {
        final String sql = "SELECT * FROM user WHERE Kontonummer=" + kontonummer + " AND Passwort='" + password + "'";
        if (executeSql(sql)) {
            return true;
        }
        return false;
    }

    public static ResultSet executeSqlWithResultSet(final String sql) {
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
