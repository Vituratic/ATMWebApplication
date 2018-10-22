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
    out.println("Your balance: ");
%>
<p></p>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="withdraw" name="withdraw" value="Withdraw"><br>
    <input type="submit" id="deposit" name="deposit" value="Deposit"><br>
    <input type="submit" id="transactions" name="transactions" value="Last transactions"><br>
</form>
</body>
</html>
