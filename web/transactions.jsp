<%@ page import="atm.core.Servlet" %>
<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="atmStyle.css">
    <title>Recent Transactions</title>
</head>
<body>
<h1>

    Bank of Trust
</h1>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="backToATM" name="backToATM" value="Back to my ATM">
</form>
<p>
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
            out.println("Recent transactions of: " + accNumber);
            out.println("<p></p>-----------------------------------------------------------------------------------<br/>");
            int accNumberInt = Integer.parseInt(accNumber);
            final ResultSet resultSet = DBUtil.getLogs(accNumberInt, bank);
            while (resultSet.next()) {
                final String log = "[" + resultSet.getString("time") + "] : " + resultSet.getString("log") + "<br/>";
                out.println(log);
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
</p>
</body>
</html>
