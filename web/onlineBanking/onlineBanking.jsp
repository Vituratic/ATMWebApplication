<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="atm.servlet.Servlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Online Banking</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<%
    String kontonummer = null;
    String bank = null;
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
        int balanceEuros = 0;
        int balanceCents = 0;
        if (resultSet.next()) {
            balanceInCent = (resultSet.getInt("Kontostand"));
            balanceEuros = balanceInCent / 100;
            balanceCents = balanceInCent - balanceEuros * 100;
        }
        if (balanceCents > 9) {
            out.println("Your balance: " + balanceEuros + "," + balanceCents + "€");
        } else {
            out.println("Your balance: " + balanceEuros + ",0" + balanceCents + "€");
        }
    } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }
%><br>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <button type="submit" id="wireTransferPort" name="wireTransferPort" >Wire Transfer</button><br>
    <button type="submit" id="accLogs" name="accLogs" >Account Logs</button><br>
    <button type="submit" id="logout" name="logout" >Logout</button><br>
</form>
</body>
</html>

