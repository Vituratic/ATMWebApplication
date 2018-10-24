<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="atm.servlet.Servlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Wire Transfer</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<br>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
<div class="container">
    <label for="Account Number from"><b>Origin Number</b></label>
    <input type="text" placeholder="Account Number" id="Account Number from" name="accNumberFrom" required><br>
    <label>Origin Bank</label>
    <select name="bankFrom" required>
        <option value="banka">Banka</option>
        <option value="bankb">Bankb</option>
        <option value="bankc">Bankc</option>
    </select><br><br>
    <label for="Account Number to"><b>Target Number</b></label>
    <input type="text" placeholder="Account Number" id="Account Number to" name="accNumberTo" required><br>
    <label>Target Bank</label>
    <select name="bankTo" required>
        <option value="banka">Banka</option>
        <option value="bankb">Bankb</option>
        <option value="bankc">Bankc</option>
    </select><br><br>

    <label for="Amount"><b>Amount</b></label>
    <input type="number" min="0" step="0.01" data-number-to-be-fixed="2" data-number-stepfactor="100" placeholder="Amount in EUR" id="Amount" name="amount" required>€<br>

    <button type="submit" id="adminWireTransfer" name="adminWireTransfer" >confirm</button>
</div>
</form>
<form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="backToAdminPage" name="backToAdminPage" value="Cancel WireTransfer">
</form>
</body>
</html>