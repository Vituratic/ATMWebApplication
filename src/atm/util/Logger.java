package atm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void log (final String accNumber, final String bank, final String keyword, final long amount, final String targetAccount, final String targetBank) {
        final Date timestamp = new Date(System.currentTimeMillis());
        final String logmsg = bank + " - " + keyword + " - " + amount + " - " + targetAccount + " - " + targetBank;
        final String sql = "INSERT INTO logs (user, log, time) VALUES (" + accNumber + ", '" + logmsg + "', '" + dateFormat.format(timestamp) + "')";
        DBUtil.executeSql(sql, targetBank);
    }
}
