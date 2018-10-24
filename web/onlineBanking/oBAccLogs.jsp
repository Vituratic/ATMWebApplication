<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="atm.servlet.Servlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    String kontonummer = null;
    for (Servlet.Connection connection : Servlet.authenticatedList) {
        if (connection.session.equals(request.getSession())) {
            kontonummer = connection.kontoNr;
            break;
        }
    }
    if (kontonummer != null) {
        out.println("Recent transactions of: " + kontonummer);
    } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }
%>
<p></p>
<%
    out.println("-----------------------------------------------------------------------------------<br/>");
    final String sql = "SELECT * FROM logs WHERE user=" + kontonummer;
    final ResultSet resultSet = DBUtil.executeSqlWithResultSet(sql);
    while (resultSet.next()) {
        final String log = "[" + resultSet.getString("time") + "] : " + resultSet.getString("log") + "<br/>";
        out.println(log);
    }
%>
</body>
</html>
