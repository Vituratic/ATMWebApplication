<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="atm.core.Servlet" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Account Logs</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="backToOb" name="backToOb" value="Back to Online Banking">
</form>
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
    if (accNumber != null) {
        out.println("Recent transactions of: " + accNumber);
    } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }
%>
<p></p>
<%
    out.println("-----------------------------------------------------------------------------------<br/>");
    final String sql = "SELECT * FROM logs WHERE user=" + accNumber;
    final ResultSet resultSet = DBUtil.executeSqlWithResultSet(sql, bank);
    try {
        while (resultSet.next()) {
            final String log = "[" + resultSet.getString("time") + "] : " + resultSet.getString("log") + "<br/>";
            out.println(log);
        }
    } catch (Exception e) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }
%>
</body>
</html>
