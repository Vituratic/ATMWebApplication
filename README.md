# ATMWebApplication

In this project we used a MySQL-Database.(https://dev.mysql.com/downloads/windows/installer/8.0.html)
The MySQL Server, MySQL Workbench and Connect/J need to be installed.

The goto files to import into your Database can be found in the *database* folder. Before you are able to import these files, you need to manually create the schemas "banka", "bankb" and "bankc".
After setting up your Database, you need to reconfigure your login data. For this, you need to create a "dblogin.txt" inside the *bin* folder of your Tomcat directory. It should look like this:

> USER=YourUserHere<br/>
> PASS=YourPassHere

and contain the data you chose for your Database.

Now everything should be ready to go.
The application is supposed to be started in a Tomcat with the context */Servlet*.
