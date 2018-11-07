package atm.core;

import atm.util.DBUtil;
import atm.util.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.ResultSet;

import static atm.core.Servlet.isAuthenticated;

public class requestMethods {

    public static void wireTransfer(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        final String amountString = request.getParameter("amount");
        final String accNumber = request.getParameter("accNumber");
        final String targetBank = request.getParameter("bank");
        final String executingAccNumber = Servlet.Connection.getConnectionAccId(request.getSession());
        if (!accNumber.matches("\\d+") || !executingAccNumber.matches("\\d+")) {
            dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
            return;
        }
        final String executingBank = Servlet.Connection.getConnectionBank(request.getSession());
        String inputToDeposit;
        if (!amountString.contains(".")) {
            inputToDeposit = amountString + "00";
        } else {
            final String[] inputToDepositSplit = amountString.replace('.', 'a').split("a");
            inputToDeposit = inputToDepositSplit[0] + inputToDepositSplit[1];
        }
        long amountLong = -1;
        int amountInt;
        String amountEuro = "";
        String amountCent = "";
        try {
            amountLong = Long.parseLong(inputToDeposit);
            amountInt = (int)(amountLong/100);
            amountEuro = "" + amountInt;
            amountCent = "" + (amountLong - amountInt*100);
        } catch (Exception e) {
            dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
        if (amountLong < 0){
            return;
        }
        if (isAuthenticated(request.getSession())){
            dispatcher = request.getRequestDispatcher("/onlineBanking/onlineBanking.jsp");
        }else{
            dispatcher = request.getRequestDispatcher("/onlineBanking/notLoggedIn.jsp");
            dispatcher.forward(request, response);
            return;
        }
        final ResultSet sqlCheck = DBUtil.executeSqlWithResultSet("SELECT Passwort FROM user WHERE Kontonummer=" + accNumber ,targetBank);
        try{
            if(!sqlCheck.next()){
                dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                return;
            }

        }catch(Exception e){
            dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
            return;
        }
        // DB action for person receiving the transfer
        String sql = "UPDATE user  SET Kontostand = Kontostand + " + amountLong + " WHERE Kontonummer=" + accNumber;
        DBUtil.executeSql(sql, targetBank);
        // DB action for person executing the transfer
        sql = "UPDATE user  SET Kontostand = Kontostand - " + amountLong + " WHERE Kontonummer=" + executingAccNumber;
        DBUtil.executeSql(sql, Servlet.Connection.getConnectionBank(request.getSession()));
        // log for person receiving the transfer
        Logger.log(accNumber, "WireTransfer from " + executingAccNumber + " at " + executingBank + ": " + amountEuro + "," + amountCent + "€", targetBank);
        // log for person executing the transfer
        Logger.log(executingAccNumber, "WireTransfer to " + accNumber + " at " + targetBank + ": " + amountEuro + "," + amountCent + "€", executingBank);
        dispatcher.forward(request, response);
    }

    public static void adminWireTransfer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String amountString = request.getParameter("amount");
        final String originNumber = request.getParameter("accNumberFrom");
        final String targetNumber = request.getParameter("accNumberTo");
        if (!originNumber.matches("\\d+") ||!targetNumber.matches("\\d+")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
        final String originBank = request.getParameter("bankFrom");
        final String targetBank = request.getParameter("bankTo");

        String inputToDeposit;
        if (!amountString.contains(".")) {
            inputToDeposit = amountString + "00";
        } else {
            final String[] inputToDepositSplit = amountString.replace('.', 'a').split("a");
            inputToDeposit = inputToDepositSplit[0] + inputToDepositSplit[1];
        }
        long amountLong = -1;
        int amountInt;
        String amountEuro = "";
        String amountCent = "";
        try {
            amountLong = Long.parseLong(inputToDeposit);
            amountInt = (int)(amountLong/100);
            amountEuro = "" + amountInt;
            amountCent = "" + (amountLong - amountInt*100);
        } catch (Exception e) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
        if (amountLong < 0){
            return;
        }
        // DB action for person receiving the transfer
        String sql = "UPDATE user  SET Kontostand = Kontostand + " + amountLong + " WHERE Kontonummer=" + targetNumber;
        // DB action for person executing the transfer
        DBUtil.executeSql(sql, targetBank);
        sql = "UPDATE user  SET Kontostand = Kontostand - " + amountLong + " WHERE Kontonummer=" + originNumber;
        DBUtil.executeSql(sql, originBank);
        // log for person receiving the transfer
        Logger.log(targetNumber, "ADM | WireTransfer from " + originNumber + " at " + originBank + ": " + amountEuro + "," + amountCent + "€", targetBank);
        // log for person executing the transfer
        Logger.log(originNumber, "ADM | WireTransfer to " + targetNumber + " at " + targetBank + ": " + amountEuro + "," + amountCent + "€", originBank);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/adminInterface/adminPage.jsp");
        dispatcher.forward(request, response);
    }
}
