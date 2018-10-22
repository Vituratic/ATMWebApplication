package atm.util;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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
}
