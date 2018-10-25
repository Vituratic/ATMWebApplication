package atm.core;

import atm.util.DBUtil;
import atm.util.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static atm.core.Servlet.isAuthenticated;

public class requestMethods {

    public static void wireTransfer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        final String amount = request.getParameter("amount");
        final String accNumber = request.getParameter("accNumber");
        final String targetBank = request.getParameter("bank");
        final String executingAccNumber = Servlet.Connection.getConnectionAccId(request.getSession());
        final String executingBank = Servlet.Connection.getConnectionBank(request.getSession());
        String inputToDeposit;
        if (!amount.contains(".")) {
            inputToDeposit = amount + "00";
        } else {
            final String[] inputToDepositSplit = amount.replace('.', 'a').split("a");
            inputToDeposit = inputToDepositSplit[0] + inputToDepositSplit[1];
        }
        final long finalAmount = Long.parseLong(inputToDeposit);
        if (finalAmount < 0){
            return;
        }
        if (isAuthenticated(request.getSession())){
            dispatcher = request.getRequestDispatcher("/onlineBanking/onlineBanking.jsp");
        }else{
            dispatcher = request.getRequestDispatcher("/onlineBanking/notLoggedIn.jsp");
            dispatcher.forward(request, response);
        }
        ResultSet sqlCheck = DBUtil.executeSqlWithResultSet("SELECT Passwort FROM user WHERE Kontonummer=" + accNumber ,targetBank);
        try{
            if(sqlCheck.next() == false){
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
        String sql = "UPDATE user  SET Kontostand = Kontostand + " + finalAmount + " WHERE Kontonummer=" + accNumber;
        // DB action for person executing the transfer
        DBUtil.executeSql(sql, targetBank);
        sql = "UPDATE user  SET Kontostand = Kontostand - " + finalAmount + " WHERE Kontonummer=" + executingAccNumber;
        DBUtil.executeSql(sql, Servlet.Connection.getConnectionBank(request.getSession()));
        // log for person receiving the transfer
        Logger.log(accNumber, "WireTransfer from " + executingAccNumber + " at " + executingBank + ": " + amount, targetBank);
        // log for person executing the transfer
        Logger.log(executingAccNumber, "WireTransfer to " + accNumber + " at " + targetBank + ": " + amount, executingBank);
        dispatcher.forward(request, response);
    }
    public static void adminWireTransfer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String amount = request.getParameter("amount");
        final String originNumber = request.getParameter("accNumberFrom");
        final String targetNumber = request.getParameter("accNumberTo");
        final String originBank = request.getParameter("bankFrom");
        final String targetBank = request.getParameter("bankTo");

        String inputToDeposit;
        if (!amount.contains(".")) {
            inputToDeposit = amount + "00";
        } else {
            final String[] inputToDepositSplit = amount.replace('.', 'a').split("a");
            inputToDeposit = inputToDepositSplit[0] + inputToDepositSplit[1];
        }
        final long finalAmount = Long.parseLong(inputToDeposit);
        if (finalAmount < 0){
            return;
        }
        // DB action for person receiving the transfer
        String sql = "UPDATE user  SET Kontostand = Kontostand + " + finalAmount + " WHERE Kontonummer=" + targetNumber;
        // DB action for person executing the transfer
        DBUtil.executeSql(sql, targetBank);
        sql = "UPDATE user  SET Kontostand = Kontostand - " + finalAmount + " WHERE Kontonummer=" + originNumber;
        DBUtil.executeSql(sql, originBank);
        // log for person receiving the transfer
        Logger.log(targetNumber, "ADM | WireTransfer from " + originNumber + " at " + originBank + ": " + amount, targetBank);
        // log for person executing the transfer
        Logger.log(originNumber, "ADM | WireTransfer to " + targetNumber + " at " + targetBank + ": " + amount, originBank);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/adminInterface/adminPage.jsp");
        dispatcher.forward(request, response);
    }
}
