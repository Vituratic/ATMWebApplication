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
    public static ArrayList<Connection> connections = new ArrayList<>();
    public static ArrayList<Connection> authenticatedList = new ArrayList<>();
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
        if (request.getParameter("depositForward") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/deposit.jsp");
            dispatcher.forward(request, response);
        }
        if (request.getParameter("withdrawal") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/withdrawal.jsp");
            dispatcher.forward(request, response);
        }
        if (request.getParameter("transactions") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/transactions.jsp");
            dispatcher.forward(request, response);
        }

        //loginATM
        if (request.getParameter("loginATM") != null){
            String uname = request.getParameter("uname");
            String psw = request.getParameter("psw");

            if (authenticate(uname, psw)){
                authenticatedList.add(new Connection(uname, request.getSession()));
                RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
                dispatcher.forward(request, response);
            }
        }
        //loginOB
        if (request.getParameter("loginOB") != null){
            String uname = request.getParameter("uname");
            String psw = request.getParameter("psw");

            if (authenticate(uname, psw)){
                authenticatedList.add(new Connection(uname, request.getSession()));
                RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/onlineBanking.jsp");
                dispatcher.forward(request, response);
            }
        }

        if (request.getParameter("wireTransfer") != null){
            requestMethods.wireTransfer(request,response);
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
        String kontonummer = "";
        for (Connection connection : connections) {
            if (connection.session.equals(request.getSession())) {
                kontonummer = connection.kontoNr;
            }
        }
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
        String kontonummer = "";
        for (Connection connection : connections) {
            if (connection.session.equals(request.getSession())) {
                kontonummer = connection.kontoNr;
            }
        }
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
    public static class Connection {
        public String kontoNr;
        public HttpSession session;

        protected Connection(String kontoNr, HttpSession session){
            this.kontoNr = kontoNr;
            this.session = session;
            connections.add(this);
        }

        //Connection aus KontoNr schließen
        public static HttpSession getConnectionSession(String kontoNr){
            for (int i = 0; i < connections.size(); i++){
                if (connections.get(i).kontoNr.equals(kontoNr)){
                    return connections.get(i).session;
                }
            }
            return null;
        }

        public static String getConnectionAccId(HttpSession session){
            for (int i = 0; i < connections.size(); i++){
                if (connections.get(i).session.equals(session)){
                    return connections.get(i).kontoNr;
                }
            }
            return null;
        }
    }
}
