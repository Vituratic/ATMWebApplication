<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>HelloWorld</title>
  </head>
  <body>
  <form method="post" action="${pageContext.request.contextPath}/Servlet">
    <input type="submit" id="send" name="send" value="Send">
  </form>
  <%
    out.println("HelloWorld.");
  %>
  </body>
</html>
