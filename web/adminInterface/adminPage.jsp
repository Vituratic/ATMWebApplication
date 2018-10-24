<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="atm.servlet.Servlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Page</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<br>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <button type="submit" id="viewLogs" name="viewLogs" >View Logs</button><br>
    <button type="submit" id="makeWireTransaction" name="makeWireTransaction" >Wire Transaction</button><br>
</form>
</body>
</html>

