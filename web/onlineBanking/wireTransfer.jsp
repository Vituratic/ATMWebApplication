<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Wire Transfer</title>
</head>
<body>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <div class="container">
        <label for="Account Number"><b>Account Number</b></label>
        <input type="text" placeholder="Account Number" name="accNumber" required>

        <label for="Amount"><b>Amount</b></label>
        <input type="text" placeholder="AmountinEUR" name="amount" required>€<br>

        <button type="submit" id="wireTransfer" name="wireTransfer" >confirm</button>
    </div>
</form>
</body>
</html>
