package atm.servlet;

import atm.util.DBUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("send") != null) {
            if (DBUtil.executeSql("INSERT INTO user VALUES (1337, 'Hans', 'Wurst', 'Pa55w0rt', 2000)")) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/entrySucc.jsp");
                dispatcher.forward(request, response);
            }
        }
        if (request.getParameter("withdraw") != null) {
            final int amountToWithdraw = Integer.parseInt(request.getParameter("amountToWithdraw"));
            String kontonummer = "1337";
            String sql = "SELECT Kontostand FROM user WHERE Kontonummer=" + kontonummer;
            final ResultSet resultSet = DBUtil.executeSqlWithResultSet(sql);
            try {
                if (resultSet.next()) {
                   final int availableMoney = resultSet.getInt("Kontostand");
                   if (availableMoney > amountToWithdraw) {
                       final int newBalance = availableMoney - amountToWithdraw;
                       sql = "UPDATE user SET Kontostand=" + newBalance + " WHERE Kontonummer=" + kontonummer;
                       if (!DBUtil.executeSql(sql)) {
                           RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                       }
                   } else {
                       //TODO Not enough money on account
                   }
                } else {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String x = "";
            String y = "";
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
    }
}
