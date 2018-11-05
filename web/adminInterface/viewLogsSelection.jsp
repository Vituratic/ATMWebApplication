<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="atm.core.Servlet" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../adminStyle.css">
    <title>Admin Log Page</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<br>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <button type="submit" id="viewLogs" name="viewLogs" >View Logs</button><br><br>
    <div class="container">
        <label for="uname"><b>Account Number</b></label>
        <input type="number" placeholder="Account Number" id="uname" name="uname"><br>
        <label>Bank</label>
        <select name="bank">
            <option value="banka">Banka</option>
            <option value="bankb">Bankb</option>
            <option value="bankc">Bankc</option>
        </select><br>
    </div>
</form>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <button type="submit" id="login" name="backToAdminPage" >Back to Admin Page</button>
</form>

<%
    String bank = null;
    if (request.getParameter("bank") != null) {
        bank = request.getParameter("bank");
    } else {
        bank = Servlet.Connection.getConnectionBank(request.getSession());
    }
    final String accNumber = request.getParameter("uname");
    if (!accNumber.matches("\\d+")) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }
    boolean authenticated = false;
    for (Servlet.Connection connection : Servlet.adminList) {
        if (connection.session.equals(request.getSession())) {
            authenticated = true;
            break;
        }
    }
    try {
        if (accNumber == null || accNumber.equals("")) {
            if (authenticated) {
                out.println("Recent transactions: ");
                out.println("<p></p>-----------------------------------------------------------------------------------<br/>");
                final String sql = "SELECT * FROM logs";
                final ResultSet resultSet = DBUtil.executeSqlWithResultSet(sql, bank);
                while (resultSet.next()) {
                    final String log = "[" + resultSet.getString("time") + "] : " + resultSet.getString("user") + " | " + resultSet.getString("log") + "<br/>";
                    out.println(log);
                }
            }
        } else {
            if (authenticated) {
                out.println("Recent transactions of: " + accNumber);
                out.println("<p></p>-----------------------------------------------------------------------------------<br/>");
                final String sql = "SELECT * FROM logs WHERE user=" + accNumber;
                final ResultSet resultSet = DBUtil.executeSqlWithResultSet(sql, bank);
                while (resultSet.next()) {
                    final String log = "[" + resultSet.getString("time") + "] : " + resultSet.getString("user") + " | " + resultSet.getString("log") + "<br/>";
                    out.println(log);
                }
            }
        }
    } catch (Exception e) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }
%>
</body>
</html>