<%@ page import="atm.core.Servlet" %>
<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="atmStyle.css">
    <title>Deposit</title>
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
    if (accNumber != null && bank != null) {
        try {
            int accNumberInt = Integer.parseInt(accNumber);
            ResultSet resultSet = DBUtil.getKontostand(accNumberInt, bank);
            long balanceInCent;
            long balanceEuro = 0;
            long balanceCents = 0;
            if (resultSet.next()) {
                balanceInCent = (resultSet.getLong("Kontostand"));
                balanceEuro = balanceInCent / 100;
                balanceCents = balanceInCent - balanceEuro * 100;
            }
            if (balanceCents > 9) {
                out.println("Your balance: " + balanceEuro + "," + balanceCents + "€");
            } else {
                out.println("Your balance: " + balanceEuro + ",0" + balanceCents + "€");
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
    <%
        out.println("How much would you like to deposit?");
    %>
    <br>
    <input type="number" min="0" step="0.01" data-number-to-be-fixed="2" data-number-stepfactor="100" name="amountToDeposit" maxlength="8" value=""><br>
    <input type="submit" id="deposit" name="deposit" value="Deposit">
</form>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" name="backToATM" value="Cancel Deposit">
</form>
</body>
</html>