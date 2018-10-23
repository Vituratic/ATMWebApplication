<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ATM</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<%
    ResultSet resultSet = DBUtil.executeSqlWithResultSet("SELECT Kontostand FROM user WHERE Kontonummer=1337");
    int balance = 0;
    if (resultSet.next()) {
        balance = (resultSet.getInt("Kontostand"));
    }
    out.println("Your balance: " + balance);
%>
<p></p>
<form method="post" action="withdrawal.jsp">
    <input type="submit" id="withdrawal" name="withdrawal" value="Withdraw"><br>
</form>
    <input type="submit" id="deposit" name="deposit" value="Deposit"><br>
    <input type="submit" id="transactions" name="transactions" value="Last transactions"><br>
</form>
</body>
</html>
