<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ATM Login</title>
</head>
<body>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="send" name="send" value="Send">
</form>
<%
out.println("Please Login");
%>
</body>
</html>
