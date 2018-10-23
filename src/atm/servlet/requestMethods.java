package atm.servlet;

import atm.util.DBUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;

import static atm.servlet.Servlet.isAuthenticated;

public class requestMethods {
    public static void wireTransfer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        System.out.print(request.getParameter("amount"));
        String amount = request.getParameter("amount");
        String accNumber = request.getParameter("accNumber");

        String inputToDeposit = null;
        if (!amount.contains(".")) {
            inputToDeposit = amount + "00";
        } else {
            String[] inputToDepositSplit = amount.replace('.', 'a').split("a");
            inputToDeposit = inputToDepositSplit[0] + inputToDepositSplit[1];
        }
        final int finalAmount = Integer.parseInt(inputToDeposit);

        if (finalAmount < 0){
            return;
        }

        //nach überweisung kommt nicht eingeloggt fehlermeldung obwol eingeloggt!
        if (isAuthenticated(request.getSession())){

            dispatcher = request.getRequestDispatcher("/onlineBanking/onlineBanking.jsp");
        }else{
            dispatcher = request.getRequestDispatcher("/onlineBanking/notLoggedIn.jsp");
        }
        String sql1 = "UPDATE user  SET Kontostand = Kontostand + " + finalAmount + " WHERE Kontonummer=" + accNumber;
        String sql2 = "UPDATE user  SET Kontostand = Kontostand - " + finalAmount + " WHERE Kontonummer=" + Servlet.Connection.getConnectionAccId(request.getSession());
        
        DBUtil.executeSql(sql1);
        DBUtil.executeSql(sql2);

        dispatcher.forward(request, response);
    }
}
