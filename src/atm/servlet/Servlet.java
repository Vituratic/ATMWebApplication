package atm.servlet;

import atm.util.DBUtil;
import atm.util.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    public static ArrayList<Connection> connections = new ArrayList<>();
    public static ArrayList<Connection> authenticatedList = new ArrayList<>();

    public static boolean isAuthenticated(HttpSession session){
        for (Connection connection:authenticatedList) {
            if (connection.session.equals(session)){
                return true;
            }
        }
        return false;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        if (request.getParameter("backToATM") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
            dispatcher.forward(request, response);
        }
        if (request.getParameter("backToOb") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/onlineBanking.jsp");
            dispatcher.forward(request, response);
        }
        if (request.getParameter("accLogs") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/oBAccLogs.jsp");
            dispatcher.forward(request, response);
        }
        if (request.getParameter("loginATM") != null){
            final String uname = request.getParameter("uname");
            final String psw = request.getParameter("psw");
            final String bank = request.getParameter("bank");

            if (authenticate(uname, psw, bank)){
                authenticatedList.add(new Connection(uname, request.getSession(), bank));
                RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
                dispatcher.forward(request, response);
            }
        }
        if (request.getParameter("loginOB") != null){
            final String uname = request.getParameter("uname");
            final String psw = request.getParameter("psw");
            final String bank = request.getParameter("bank");

            if (authenticate(uname, psw, bank)){
                authenticatedList.add(new Connection(uname, request.getSession(),bank));
                RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/onlineBanking.jsp");
                dispatcher.forward(request, response);
            }
        }
        if (request.getParameter("wireTransferPort") != null){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/wireTransfer.jsp");
            dispatcher.forward(request, response);
        }
        if (request.getParameter("wireTransfer") != null){
            requestMethods.wireTransfer(request,response);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }

    private void handleWithdrawal(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthenticated(request.getSession())) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
        String inputToWithdraw;
        if (!request.getParameter("amountToWithdraw").contains(".")) {
            inputToWithdraw = request.getParameter("amountToWithdraw") + "00";
        } else {
            String[] inputToWithdrawSplit = request.getParameter("amountToWithdraw").replace('.', 'a').split("a");
            inputToWithdraw = inputToWithdrawSplit[0] + inputToWithdrawSplit[1];
        }
        final int amountToWithdraw = Integer.parseInt(inputToWithdraw);
        if (amountToWithdraw < 0) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
            return;
        }
        String bank = "";
        String accNumber = "";
        for (Connection connection : connections) {
            if (connection.session.equals(request.getSession())) {
                accNumber = connection.accNumber;
                bank = connection.bank;
                break;
            }
        }
        String sql = "SELECT Kontostand FROM user WHERE Kontonummer=" + accNumber;
        final ResultSet resultSet = DBUtil.executeSqlWithResultSet(sql, bank);
        try {
            if (resultSet.next()) {
                final int availableMoney = resultSet.getInt("Kontostand");
                if (availableMoney > amountToWithdraw) {
                    final int newBalance = availableMoney - amountToWithdraw;
                    sql = "UPDATE user SET Kontostand=" + newBalance + " WHERE Kontonummer=" + accNumber;
                    if (!DBUtil.executeSql(sql, bank)) {
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
        Logger.log(accNumber, "banka", "Withdraw", amountToWithdraw, accNumber, "banka");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
        dispatcher.forward(request, response);
    }

    private void handleDeposit(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthenticated(request.getSession())) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
        String inputToDeposit;
        if (!request.getParameter("amountToDeposit").contains(".")) {
            inputToDeposit = request.getParameter("amountToDeposit") + "00";
        } else {
            String[] inputToDepositSplit = request.getParameter("amountToDeposit").replace('.', 'a').split("a");
            inputToDeposit = inputToDepositSplit[0] + inputToDepositSplit[1];
        }
        final int amountToDeposit = Integer.parseInt(inputToDeposit);
        if (amountToDeposit < 0) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
            return;
        }
        String bank = "";
        String accNumber = "";
        for (Connection connection : connections) {
            if (connection.session.equals(request.getSession())) {
                accNumber = connection.accNumber;
                bank = connection.bank;
            }
        }
        String sql = "SELECT Kontostand FROM user WHERE Kontonummer=" + accNumber;
        final ResultSet resultSet = DBUtil.executeSqlWithResultSet(sql, bank);
        try {
            if (resultSet.next()) {
                final int availableMoney = resultSet.getInt("Kontostand");
                final int newBalance = availableMoney + amountToDeposit;
                sql = "UPDATE user SET Kontostand=" + newBalance + " WHERE Kontonummer=" + accNumber;
                if (!DBUtil.executeSql(sql, bank)) {
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
        Logger.log(accNumber, "banka", "Deposit", amountToDeposit, accNumber, "banka");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
        dispatcher.forward(request, response);
    }

    protected boolean authenticate(final String uname, final String psw, final String targetBank){
        final String sql = "SELECT Passwort FROM user WHERE Kontonummer=" + uname;
        final ResultSet resultSet = DBUtil.executeSqlWithResultSet(sql, targetBank);
        try {
            if (resultSet.next()) {
                return psw.equals(resultSet.getString("Passwort"));
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
        public String accNumber;
        public HttpSession session;
        public String bank;

        protected Connection(final String accNumber, final HttpSession session, final String bank){
            this.accNumber = accNumber;
            this.session = session;
            this.bank = bank;
            connections.add(this);
        }

        //Connection aus KontoNr schließen
        public static HttpSession getConnectionSession(final String accNumber){
            for (Connection connection : connections) {
                if (connection.accNumber.equals(accNumber)) {
                    return connection.session;
                }
            }
            return null;
        }

        public static String getConnectionAccId(final HttpSession session){
            for (Connection connection : connections) {
                if (connection.session.equals(session)) {
                    return connection.accNumber;
                }
            }
            return null;
        }

        public static String getConnectionBank(final HttpSession session) {
            for (Connection connection : connections) {
                if (connection.session.equals(session)) {
                    return connection.bank;
                }
            }
            return null;
        }

        public static boolean removeConnection(HttpSession session){
            for (int i = 0; i < connections.size(); i++){
                if (connections.get(i).session.equals(session)){
                    connections.remove(i);
                    return true;
                }
            }
            return false;
        }
    }
}
