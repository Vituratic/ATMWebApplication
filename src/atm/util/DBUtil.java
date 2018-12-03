package atm.util;

import com.mysql.jdbc.Driver;

import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {

    private static final String URLHOLDER = "jdbc:mysql://localhost:3306/PLACEHOLDER?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USER = readLoginData("USER");
    private static final String PASS = readLoginData("PASS");

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

    public static ResultSet getLogs(final int accNumber, final String targetBank) {
        final String URL = URLHOLDER.replace("PLACEHOLDER", targetBank);
        try {
            final Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            final Connection connection = DriverManager.getConnection(URL, USER, PASS);
            final String sql = "SELECT * FROM logs WHERE user = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accNumber);
            return preparedStatement.executeQuery();
        } catch(Exception e) {
            return null;
        }
    }

    public static ResultSet getKontostand(final int accNumber, final String targetBank) {
        final String URL = URLHOLDER.replace("PLACEHOLDER", targetBank);
        try {
            final Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            final Connection connection = DriverManager.getConnection(URL, USER, PASS);
            final String sql = "SELECT Kontostand FROM user WHERE Kontonummer = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accNumber);
            return preparedStatement.executeQuery();
        } catch(Exception e) {
            return null;
        }
    }

    public static ResultSet getPassword(final int accNumber, final String targetBank) {
        final String URL = URLHOLDER.replace("PLACEHOLDER", targetBank);
        try {
            final Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            final Connection connection = DriverManager.getConnection(URL, USER, PASS);
            final String sql = "SELECT Passwort FROM user WHERE Kontonummer = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accNumber);
            return preparedStatement.executeQuery();
        } catch(Exception e) {
            return null;
        }
    }

    public static boolean updateKontostand(final int amount, final int accNumber, final String targetBank) {
        final String URL = URLHOLDER.replace("PLACEHOLDER", targetBank);
        try {
            final Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            final Connection connection = DriverManager.getConnection(URL, USER, PASS);
            final String sql = "UPDATE user SET Kontostand = Kontostand + ? WHERE Kontonummer = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, accNumber);
            preparedStatement.execute();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    private static String readLoginData(final String data) {
        if (!data.equals("PASS") && !data.equals("USER")) {
            return null;
        }
        try {
            final File dataFile = new File("dblogin.txt");
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            String fromFile;
            while ((fromFile = br.readLine()) != null) {
                if (fromFile.startsWith(data)) {
                    String[] result = fromFile.split("=");
                    return result[1];
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
