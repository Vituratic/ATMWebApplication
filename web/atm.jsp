<%@ page import="atm.core.Servlet" %>
<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="atmStyle.css">
    <title>ATM</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<%
    String bank = null;
    String accNumber = null;
    for (Servlet.Connection connection : Servlet.authenticatedList) {
        if (connection.session.equals(request.getSession())) {
            accNumber = connection.accNumber;
            bank = connection.bank;
            break;
        }
    }
    if (accNumber != null && bank != null && Servlet.isAuthenticated(request.getSession())) {
        try {
            int accNumberInt = Integer.parseInt(accNumber);
            ResultSet resultSet = DBUtil.getKontostand(accNumberInt, bank);
            long balanceInCent;
            long balanceEuros = 0;
            long balanceCents = 0;
            if (resultSet.next()) {
                balanceInCent = (resultSet.getLong("Kontostand"));
                balanceEuros = balanceInCent / 100;
                balanceCents = balanceInCent - balanceEuros * 100;
            }
            if (balanceCents > 9) {
                out.println("Your balance: " + balanceEuros + "," + balanceCents + "€");
            } else {
                out.println("Your balance: " + balanceEuros + ",0" + balanceCents + "€");
            }
        } catch (Exception e) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }
%>
<p></p>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="withdrawal" name="withdrawal" value="Withdraw"><br>
</form>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="depositForward" name="depositForward" value="Deposit"><br>
</form>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="transactions" name="transactions" value="Last transactions"><br>
</form>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="logoutATM" name="logoutATM" value="Logout"><br>
</form>
</body>
</html>
