package atm.core;

import atm.util.DBUtil;
import atm.util.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static atm.core.Servlet.isAuthenticated;

public class requestMethods {

    public static void wireTransfer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        System.out.print(request.getParameter("amount"));
        final String amount = request.getParameter("amount");
        final String accNumber = request.getParameter("accNumber");
        final String targetBank = request.getParameter("bank");

        String inputToDeposit;
        if (!amount.contains(".")) {
            inputToDeposit = amount + "00";
        } else {
            final String[] inputToDepositSplit = amount.replace('.', 'a').split("a");
            inputToDeposit = inputToDepositSplit[0] + inputToDepositSplit[1];
        }
        final int finalAmount = Integer.parseInt(inputToDeposit);

        if (finalAmount < 0){
            return;
        }

        if (isAuthenticated(request.getSession())){

            dispatcher = request.getRequestDispatcher("/onlineBanking/onlineBanking.jsp");
        }else{
            dispatcher = request.getRequestDispatcher("/onlineBanking/notLoggedIn.jsp");
            dispatcher.forward(request, response);
        }
        // DB action for person receiving the transfer
        String sql = "UPDATE user  SET Kontostand = Kontostand + " + finalAmount + " WHERE Kontonummer=" + accNumber;
        // DB action for person executing the transfer
        DBUtil.executeSql(sql, targetBank);
        sql = "UPDATE user  SET Kontostand = Kontostand - " + finalAmount + " WHERE Kontonummer=" + Servlet.Connection.getConnectionAccId(request.getSession());
        DBUtil.executeSql(sql, Servlet.Connection.getConnectionBank(request.getSession()));
        // log for person receiving the transfer
        Logger.log(accNumber, targetBank, "WireTransfer", finalAmount, Servlet.Connection.getConnectionAccId(request.getSession()), Servlet.Connection.getConnectionBank(request.getSession()));
        // log for person executing the transfer
        Logger.log(Servlet.Connection.getConnectionAccId(request.getSession()), Servlet.Connection.getConnectionBank(request.getSession()), "WireTransfer", -finalAmount, accNumber, targetBank);

        dispatcher.forward(request, response);
    }
}
