<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="atm.servlet.Servlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Deposit</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<%
    String bank = null;
    String kontonummer = null;
    for (Servlet.Connection connection : Servlet.authenticatedList) {
        if (connection.session.equals(request.getSession())) {
            kontonummer = connection.kontoNr;
            bank = connection.bank;
            break;
        }
    }
    if (kontonummer != null && bank != null) {
        ResultSet resultSet = DBUtil.executeSqlWithResultSet("SELECT Kontostand FROM user WHERE Kontonummer=" + kontonummer, bank);
        int balanceInCent = 0;
        int balanceEuro = 0;
        int balanceCents = 0;
        if (resultSet.next()) {
            balanceInCent = (resultSet.getInt("Kontostand"));
            balanceEuro = balanceInCent / 100;
            balanceCents = balanceInCent - balanceEuro * 100;
        }
        if (balanceCents > 9) {
            out.println("Your balance: " + balanceEuro + "," + balanceCents + "€");
        } else {
            out.println("Your balance: " + balanceEuro + ",0" + balanceCents + "€");
        }
    } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }
%>
<p></p>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <%
        out.println("How much would you like to deposit?");
    %>
    <br>
    <input type="number" min="0" step="0.01" data-number-to-be-fixed="2" data-number-stepfactor="100" name="amountToDeposit" value=""><br>
    <input type="submit" id="deposit" name="deposit" value="Deposit">
</form>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" name="backToATM" value="Cancel Deposit">
</form>
</body>
</html>