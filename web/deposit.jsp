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
    String kontonummer = null;
    for (Servlet.Connection connection : Servlet.authenticatedList) {
        if (connection.session.equals(request.getSession())) {
            kontonummer = connection.kontoNr;
            break;
        }
    }
    if (kontonummer != null) {
        ResultSet resultSet = DBUtil.executeSqlWithResultSet("SELECT Kontostand FROM user WHERE Kontonummer=" + kontonummer);
        int balanceInCent = 0;
        int balanceEuro = 0;
        int balanceCents = 0;
        if (resultSet.next()) {
            balanceInCent = (resultSet.getInt("Kontostand"));
            balanceEuro = balanceInCent / 100;
            balanceCents = balanceInCent - balanceEuro * 100;
        }
        out.println("Your balance: " + balanceEuro + "," + balanceCents + "€");
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
    <input type="number" name="amountToDeposit" value=""><br>
    <input type="submit" id="deposit" name="deposit" value="Deposit">
</form>
<form method="post" action="atm.jsp">
    <input type="submit" name="cancel" value="Cancel Deposit">
</form>
</body>
</html>