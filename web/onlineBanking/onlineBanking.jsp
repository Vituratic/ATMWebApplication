<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="atm.core.Servlet" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../onlineStyle.css">
    <title>Online Banking</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<%
    String accNumber = null;
    String bank = null;
    for (Servlet.Connection connection : Servlet.authenticatedList) {
        if (connection.session.equals(request.getSession())) {
            accNumber = connection.accNumber;
            bank = connection.bank;
            break;
        }
    }
    if (accNumber != null && bank != null) {
        try {
            ResultSet resultSet = DBUtil.executeSqlWithResultSet("SELECT Kontostand FROM user WHERE Kontonummer=" + accNumber, bank);
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
%><br>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <button type="submit" id="wireTransferPort" name="wireTransferPort" >Wire Transfer</button><br>
    <button type="submit" id="accLogs" name="accLogs" >Account Logs</button><br>
</form>
</body>
</html>

