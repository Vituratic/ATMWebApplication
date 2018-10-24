<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../adminStyle.css">
    <title>Admin Login</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <div class="container">
        <label for="uname"><b>Account Number</b></label>
        <input type="text" placeholder="Account Number" name="uname" required><br>

        <label for="psw"><b>Password</b></label>
        <input type="password" placeholder="Password" name="psw" required><br>
        <label>Bank</label>
        <select name="bank">
            <option value="banka">Banka</option>
            <option value="bankb">Bankb</option>
            <option value="bankc">Bankc</option>
        </select><br>
        <button type="submit" id="login" name="loginAdmin" >Login</button>
    </div>
</form>
</body>
</html>