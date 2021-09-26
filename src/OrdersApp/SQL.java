package OrdersApp;

import java.sql.*;

public class SQL {

	// Server Credentials
    static final String USERNAME = "...";
    static final String MYPASS = "...";

    public static Connection Connect(String BDname, String sgbd) {
        Connection conn = null;
        try {
            String cstr;
            if (sgbd.compareTo("1") == 0) {
                cstr = "jdbc:sqlite:" + BDname;
                conn = DriverManager.getConnection(cstr);
            } else {
                String driver = "com.mysql.jdbc.Driver";
                try {
                    Class.forName(driver);
                } catch (ClassNotFoundException ex) {
                    System.out.println("O driver MySql Connector não está instalado.");
                    return null;
                }
                cstr = "jdbc:mysql://.../" + BDname + "?useSSL=false";
                conn = DriverManager.getConnection(cstr, USERNAME, MYPASS);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static ResultSet executeQuery(Connection ligacao, String sqlcmd) {
        ResultSet rs = null;
        try {
            Statement stmt = ligacao.createStatement();
            rs = stmt.executeQuery(sqlcmd);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
}