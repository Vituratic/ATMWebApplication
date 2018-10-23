<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Deposit</title>
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
        out.println("How much would you like to deposit?");
    %>
    <br>
    <input type="number" name="amountToDeposit" value=""><br>
    <input type="submit" id="deposit" name="deposit" value="Deposit">
</form>
<form method="post" action="atm.jsp">
    <input type="submit" name="cancel" value="Cancel Deposit">
</form>
</body>
</html>