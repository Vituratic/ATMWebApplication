<%@ page import="atm.util.DBUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="atm.servlet.Servlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Online Banking</title>
</head>
<body>
<h1>
    Bank of Trust
</h1>
<%
    String kontonummer = null;
    for (Servlet.Connection connection : Servlet.authenticatedList) {
        if (connection.session.equals(request.getSession())) {
            kontonummer = connection.kontoNr;
            break;
        }
    }
    if (kontonummer != null) {
        ResultSet resultSet = DBUtil.executeSqlWithResultSet("SELECT Kontostand FROM user WHERE Kontonummer=" + kontonummer);
        int balance = 0;
        if (resultSet.next()) {
            balance = (resultSet.getInt("Kontostand"));
        }
        out.println("Your balance: " + balance);
    } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }
%><br>
<button type="submit" id="wireTransfer" name="wireTransfer" >Wire Transfer</button><br>
<button type="submit" id="accLogs" name="accLogs" >Account Logs</button><br>
<button type="submit" id="logout" name="logout" >Logout</button><br>
</body>
</html>

