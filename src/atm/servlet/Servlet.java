package atm.servlet;

import atm.util.DBUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    protected static ArrayList<Connection> connections = new ArrayList<>();
    protected static ArrayList<Object> authenticatedList = new ArrayList<>();
    public static boolean isAuthenticated(HttpSession session){
        if (authenticatedList.contains(session)){
            return true;
        }else{
            return false;
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("send") != null) {
            if (DBUtil.executeSql("INSERT INTO user VALUES (1337, 'Hans', 'Wurst', 'Pa55w0rt', 2000)")) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/entrySucc.jsp");
                dispatcher.forward(request, response);
            }
        }
        if (request.getParameter("withdraw") != null) {
            handleWithdrawal(request, response);
        }
        if (request.getParameter("deposit") != null) {
            handleDeposit(request, response);
        }
        //login
        if (request.getParameter("login") != null){
            String uname = request.getParameter("uname");
            String psw = request.getParameter("psw");
            if (authenticate(uname, psw)){
                authenticatedList.add(new Connection("1337", null));
                RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
                dispatcher.forward(request, response);
            }
        }
    }

    private void handleWithdrawal(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthenticated(request.getSession())) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
        final int amountToWithdraw = Integer.parseInt(request.getParameter("amountToWithdraw"));
        if (amountToWithdraw < 0) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
            return;
        }
        //TODO
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
                        dispatcher.forward(request, response);
                    }
                } else {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    dispatcher.forward(request, response);
                }
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
        dispatcher.forward(request, response);
    }

    private void handleDeposit(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthenticated(request.getSession())) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
        final int amountToDeposit = Integer.parseInt(request.getParameter("amountToDeposit"));
        if (amountToDeposit < 0) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
            return;
        }
        String kontonummer = "1337"; //TODO Remove
        String sql = "SELECT Kontostand FROM user WHERE Kontonummer=" + kontonummer;
        final ResultSet resultSet = DBUtil.executeSqlWithResultSet(sql);
        try {
            if (resultSet.next()) {
                final int availableMoney = resultSet.getInt("Kontostand");
                final int newBalance = availableMoney + amountToDeposit;
                sql = "UPDATE user SET Kontostand=" + newBalance + " WHERE Kontonummer=" + kontonummer;
                if (!DBUtil.executeSql(sql)) {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    dispatcher.forward(request, response);
                }
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
        dispatcher.forward(request, response);
    }

    protected boolean authenticate(String uname, String psw){
        String sql = "SELECT Passwort FROM user WHERE Kontonummer=" + uname;
        final ResultSet resultSet = DBUtil.executeSqlWithResultSet(sql);
        try {
            if (resultSet.next()) {
                if (psw.equals(resultSet.getString("Passwort"))) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthenticated(request.getSession())) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
        dispatcher.forward(request, response);
    }

    //Hilfsklasse
    protected static class Connection {
        private String kontoNr;
        private HttpSession session;

        protected Connection(String kontoNr, HttpSession session){
            this.kontoNr = kontoNr;
            this.session = session;
            connections.add(this);
        }

        //Connection aus KontoNr schließen
        public HttpSession getConnection(String kontoNr){
            for (int i = 0; i < connections.size(); i++){
                if (connections.get(i).kontoNr.equals(kontoNr)){
                    return connections.get(i).session;
                }
            }
            return null;
        }
    }



}
