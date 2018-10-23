<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="atm.servlet.Servlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Withdrawal</title>
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
        int balance = 0;
        if (resultSet.next()) {
            balance = (resultSet.getInt("Kontostand"));
        }
        out.println("Your balance: " + balance);
    } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }
%>
<p></p>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <%
        out.println("How much would you like to withdraw?");
    %>
    <br>
    <input type="number" name="amountToWithdraw" value=""><br>
    <input type="submit" id="withdraw" name="withdraw" value="Withdraw">
</form>
<form method="post" action="atm.jsp">
    <input type="submit" id="cancelWithdrawal" name="cancelWithdrawal" value="Cancel Withdrawal">
</form>
</body>
</html>
