<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ATM Login</title>
</head>
<body>
<form action="action_page.php">
    <div class="imgcontainer">
        <img src="img_avatar2.png" alt="ATM Login" class="avatar">
        <br>
    </div>
    <div class="container">
        <label for="uname"><b>Kontonummer</b></label>
        <input type="text" placeholder="Kontonummer eingeben" name="uname" required><br>

        <label for="psw"><b>Passwort</b></label>
        <input type="password" placeholder="Passwort eingeben" name="psw" required><br>

        <button type="submit" id="login" name="login" >Login</button>
        <label>
            <input type="checkbox" checked="checked" name="remember"> Informationen Speichern
        </label>
    </div>
</form>
</body>
</html>
