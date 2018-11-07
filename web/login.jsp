<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="atmStyle.css">
    <title>ATM Login</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <div class="container">
        <label for="uname"><b>Account Number</b></label>
        <input type="number" placeholder="Account Number" id="uname" name="uname" required><br>

        <label for="psw"><b>Password</b></label>
        <input type="password" placeholder="Password" id="psw" name="psw" autocomplete="OFF" required><br>
        <label>Bank</label>
        <select name="bank">
            <option value="banka">Banka</option>
            <option value="bankb">Bankb</option>
            <option value="bankc">Bankc</option>
        </select><br>
        <button type="submit" id="login" name="loginATM" >Login</button>
    </div>
</form>
</body>
</html>
