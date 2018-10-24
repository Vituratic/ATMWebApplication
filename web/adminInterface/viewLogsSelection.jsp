<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="atm.core.Servlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <%--@declare id="uname"--%>
        <label for="uname"><b>Account Number</b></label>
        <input type="text" placeholder="Account Number" name="uname"><br>

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
    String bank = Servlet.Connection.getConnectionBank(request.getSession());
    String accNumber = request.getParameter("uname");
    boolean authenticated = false;

    //if (bank != null) {
        for (Servlet.Connection connection : Servlet.adminList) {
            if (connection.session.equals(request.getSession())) {
                authenticated = true;
                break;
            }
        }
        if (accNumber == null){
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
        }else {
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
    //}
%>
</body>
</html>