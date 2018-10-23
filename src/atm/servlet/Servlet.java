package atm.servlet;

import atm.util.DBUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    protected static ArrayList<Connection> connections = new ArrayList<>();
    protected static ArrayList<Object> authenticatedList = new ArrayList<>();
    public static boolean isAuthenticated(HttpSession session){
        if (authenticatedList.contains(session)){
            return true;
        }else{
            return false;
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("send") != null) {
            if (DBUtil.executeSql("INSERT INTO user VALUES (1337, 'Hans', 'Wurst', 'Pa55w0rt', 2000)")) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/entrySucc.jsp");
                dispatcher.forward(request, response);
            }
        }
        //login
        if (request.getParameter("login") != null){
            String uname = request.getParameter("uname");
            String psw = request.getParameter("psw");
            if (authenticate(uname, psw)){
                authenticatedList.add(new Connection(uname, request.getSession()));
                //RequestDispatcher dispatcher = request.getRequestDispatcher("/atm.jsp");
                //dispatcher.forward(request, response);
            }
        }
    }

    protected boolean authenticate(String uname, String psw){
        if (psw.equals(DBUtil.executeSql("SELECT Passwort FROM user WHERE Kontonummer = " + uname))){
            return true;
        }else{
            return false;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    //Hilfsklasse
    protected static class Connection {
        private String kontoNr;
        private HttpSession session;

        protected Connection(String kontoNr, HttpSession session){
            this.kontoNr = kontoNr;
            this.session = session;
            connections.add(this);
        }

        //Connection aus KontoNr schließen
        public HttpSession getConnection(String kontoNr){
            for (int i = 0; i < connections.size(); i++){
                if (connections.get(i).kontoNr.equals(kontoNr)){
                    return connections.get(i).session;
                }
            }
            return null;
        }
    }



}
