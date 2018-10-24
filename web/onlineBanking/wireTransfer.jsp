<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Wire Transfer</title>
</head>
<body>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <div class="container">
        <label for="Account Number"><b>Account Number</b></label>
        <input type="text" placeholder="Account Number" name="accNumber" required><br>
        <label>Target Bank</label>
        <select name="bank" required>
            <option value="banka">Banka</option>
            <option value="bankb">Bankb</option>
            <option value="bankc">Bankc</option>
        </select><br>

        <label for="Amount"><b>Amount</b></label>
        <input type="number" min="0" step="0.01" data-number-to-be-fixed="2" data-number-stepfactor="100" placeholder="Amount in EUR" name="amount" required>€<br>

        <button type="submit" id="wireTransfer" name="wireTransfer" >confirm</button>
    </div>
</form>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="backToOb" name="backToOb" value="Cancel WireTransfer">
</form>
</body>
</html>
