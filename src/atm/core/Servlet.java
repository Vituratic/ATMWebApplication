package atm.core;

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
import java.util.ArrayList;


@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    public static ArrayList<Connection> connections = new ArrayList<>();
    public static ArrayList<Connection> authenticatedList = new ArrayList<>();
    public static ArrayList<Connection> adminList = new ArrayList<>();

    public static boolean isAuthenticated(HttpSession session){
        for (Connection connection:authenticatedList) {
            if (connection.session.equals(session)){
                return true;
            }
        }
        return false;
    }
    public static boolean isAdmin(HttpSession session){
        for (Servlet.Connection connection : Servlet.adminList) {
            if (connection.session.equals(session)) {
                return true;
            }
        }
        return false;
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("withdraw") != null) {
            handleWithdrawal(request, response);
        } else if (request.getParameter("deposit") != null) {
            handleDeposit(request, response);
        } else if (request.getParameter("depositForward") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/deposit.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("withdrawal") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/withdrawal.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("transactions") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/transactions.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("backToATM") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("backToOb") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/onlineBanking.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("accLogs") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/oBAccLogs.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("loginATM") != null){
            final String uname = request.getParameter("uname");
            final String psw = request.getParameter("psw");
            final String bank = request.getParameter("bank");
            if (authenticate(uname, psw, bank)){
                if (!isAuthenticated(request.getSession())) {
                    authenticatedList.add(new Connection(uname, request.getSession(), bank));
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
                dispatcher.forward(request, response);
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } else if (request.getParameter("loginOB") != null){
            final String uname = request.getParameter("uname");
            final String psw = request.getParameter("psw");
            final String bank = request.getParameter("bank");
            if (authenticate(uname, psw, bank)){
                if (!isAuthenticated(request.getSession())) {
                    authenticatedList.add(new Connection(uname, request.getSession(), bank));
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/onlineBanking.jsp");
                dispatcher.forward(request, response);
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } else if (request.getParameter("wireTransferPort") != null){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/wireTransfer.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("wireTransfer") != null){
            requestMethods.wireTransfer(request,response);
        } else if (request.getParameter("adminWireTransfer") != null){
            if (isAdmin(request.getSession())){
                requestMethods.adminWireTransfer(request,response);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/adminInterface/adminPage.jsp");
                dispatcher.forward(request, response);
            }else{
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } else if (request.getParameter("loginAdmin") != null){
            final String uname = request.getParameter("uname");
            final String psw = request.getParameter("psw");
            final String bank = request.getParameter("bank");
            if (authenticate(uname, psw, bank) && uname.equals("999999999")){
                if (!isAdmin(request.getSession())) {
                    adminList.add(new Connection(uname, request.getSession(), bank));
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/adminInterface/adminPage.jsp");
                dispatcher.forward(request, response);
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } else if (request.getParameter("viewLogs") != null){
            if (isAdmin(request.getSession())){
                RequestDispatcher dispatcher = request.getRequestDispatcher("/adminInterface/viewLogsSelection.jsp");
                dispatcher.forward(request, response);
            }else{
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } else if (request.getParameter("backToAdminPage") != null){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/adminInterface/adminPage.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("makeWireTransaction") != null){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/adminInterface/makeWireTransaction.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("startOnlineBanking") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/oBLogin.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("startAtm") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("startAdmin") != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/adminInterface/adminLogin.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("logoutATM") != null){
            for (Connection connection:authenticatedList) {
                if (connection.session.equals(request.getSession())){
                    authenticatedList.remove(connection);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
                    dispatcher.forward(request, response);
                }
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("logoutOb") != null){
            for (Connection connection:authenticatedList) {
                if (connection.session.equals(request.getSession())){
                    authenticatedList.remove(connection);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/oBLogin.jsp");
                    dispatcher.forward(request, response);
                }
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/onlineBanking/oBLogin.jsp");
            dispatcher.forward(request, response);
        } else if (request.getParameter("adminLogout") != null){
            for (Connection connection:adminList) {
                if (connection.session.equals(request.getSession())){
                    adminList.remove(connection);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/adminInterface/adminLogin.jsp");
                    dispatcher.forward(request, response);
                }
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/adminInterface/adminLogin.jsp");
            dispatcher.forward(request, response);
        }else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void handleWithdrawal(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthenticated(request.getSession())) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
            return;
        }
        String inputToWithdraw;
        if (!request.getParameter("amountToWithdraw").contains(".")) {
            inputToWithdraw = request.getParameter("amountToWithdraw") + "00";
            if (inputToWithdraw.length() > 8) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else {
            String[] inputToWithdrawSplit = request.getParameter("amountToWithdraw").replace('.', 'a').split("a");
            inputToWithdraw = inputToWithdrawSplit[0] + inputToWithdrawSplit[1];
            if (inputToWithdraw.length() > 8) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }
        long amountToWithdraw = -1;
        int amountInt;
        String amountEuro = "";
        String amountCent = "";
        try {
            amountToWithdraw = Long.parseLong(inputToWithdraw);
            amountInt = (int)(amountToWithdraw/100);
            amountEuro = "" + amountInt;
            amountCent = "" + (amountToWithdraw - amountInt*100);
        } catch (Exception e) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
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
        int accNumberInt = Integer.parseInt(accNumber);
        final ResultSet resultSet = DBUtil.getKontostand(accNumberInt, bank);
        try {
            if (resultSet.next()) {
                final long availableMoney = resultSet.getLong("Kontostand");
                if (availableMoney - amountToWithdraw >= -200000) {
                    int amountToWithdrawInt = java.lang.Math.toIntExact(amountToWithdraw);
                    if (!DBUtil.updateKontostand(-amountToWithdrawInt, accNumberInt, bank)) {
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                        dispatcher.forward(request, response);
                        return;
                    }
                } else {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } catch (Exception e) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
        Logger.log(accNumber, "Withdrawal: " + amountEuro + "," + amountCent + "€", bank);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
        dispatcher.forward(request, response);
    }

    private void handleDeposit(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthenticated(request.getSession())) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
            return;
        }
        String inputToDeposit;
        if (!request.getParameter("amountToDeposit").contains(".")) {
            inputToDeposit = request.getParameter("amountToDeposit") + "00";
            if (inputToDeposit.length() > 8) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else {
            String[] inputToDepositSplit = request.getParameter("amountToDeposit").replace('.', 'a').split("a");
            inputToDeposit = inputToDepositSplit[0] + inputToDepositSplit[1];
            if (inputToDeposit.length() > 8) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }
        long amountToDeposit = -1 ;
        int amountInt;
        String amountEuro = "";
        String amountCent = "";
        try {
            amountToDeposit = Long.parseLong(inputToDeposit);
            amountInt = (int)(amountToDeposit/100);
            amountEuro = "" + amountInt;
            amountCent = "" + (amountToDeposit - amountInt*100);
        } catch (Exception e) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
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
        try {
            int accNumberInt = Integer.parseInt(accNumber);
            final ResultSet resultSet = DBUtil.getKontostand(accNumberInt, bank);
            if (resultSet.next()) {
                int amountToDepositInt = java.lang.Math.toIntExact(amountToDeposit);
                if (!DBUtil.updateKontostand(amountToDepositInt, accNumberInt, bank)) {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } catch (Exception e) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
        Logger.log(accNumber, "Deposit: " + amountEuro + "," + amountCent + "€", bank);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
        dispatcher.forward(request, response);
    }

    protected boolean authenticate(final String uname, final String psw, final String targetBank){
        try {
            int accNumber = Integer.parseInt(uname);
            final ResultSet resultSet = DBUtil.getPassword(accNumber, targetBank);
            if (resultSet.next()) {
                return psw.equals(resultSet.getString("Passwort"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/startingDialogue.jsp");
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
            for (Connection connection : connections){
                if (connection.session.equals(session)){
                    connections.remove(connection);
                    return true;
                }
            }
            return false;
        }
    }
}
