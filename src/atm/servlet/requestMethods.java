package atm.servlet;

import atm.util.DBUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static atm.servlet.Servlet.isAuthenticated;

public class requestMethods {
    public static void wireTransfer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        System.out.print(request.getParameter("amount"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        String accNumber = request.getParameter("accNumber");

        if (amount < 0){
            return;
        }

        //nach überweisung kommt nicht eingeloggt fehlermeldung obwol eingeloggt!
        if (isAuthenticated(request.getSession())){

            dispatcher = request.getRequestDispatcher("/onlineBanking/onlineBanking.jsp");
        }else{
            dispatcher = request.getRequestDispatcher("/onlineBanking/notLoggedIn.jsp");
        }

        String sql = "UPDATE user  SET Kontostand = Kontostand + " + amount + " WHERE Kontonummer=" + accNumber +";"+
                "UPDATE user  SET Kontostand = Kontostand - " + amount + " WHERE Kontonummer=" + Servlet.Connection.getConnectionAccId(request.getSession()) + ";";

        DBUtil.executeSql(sql);
        dispatcher.forward(request, response);
    }
}
