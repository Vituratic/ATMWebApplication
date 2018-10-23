<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Withdrawal</title>
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
    <%
        out.println("How much would you like to withdraw?");
    %>
    <br>
    <input type="number" name="amountToWithdraw" value=""><br>
    <input type="submit" id="withdraw" name="withdraw" value="Withdraw">
</form>
</body>
</html>
