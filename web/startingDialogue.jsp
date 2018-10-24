<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bank of Trust</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<p>
    What do you wish to do?
</p>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="startOnlineBanking" name="startOnlineBanking" value="Online Banking"><br>
    <input type="submit" id="startAtm" name="startAtm" value="ATM"><br>
    <input type="submit" id="startAdmin" name="startAdmin" value="AdminMenu">
</form>
</body>
</html>
