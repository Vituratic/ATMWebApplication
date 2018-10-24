<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ATM Login</title>
</head>
<body>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <div class="imgcontainer">
        <img src="img_avatar2.png" alt="ATM Login" class="avatar">
        <br>
    </div>
    <div class="container">
        <label for="uname"><b>Account Number</b></label>
        <input type="text" placeholder="Account Number" name="uname" required><br>

        <label for="psw"><b>Passwort</b></label>
        <input type="password" placeholder="Password" name="psw" required><br>
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
