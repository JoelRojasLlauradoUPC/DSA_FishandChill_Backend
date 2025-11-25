package edu.upc.dsa.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FactorySession {

    public static Session openSession() {
        Connection conn = getConnection();
        Session session = new SessionImpl(conn);
        return session;
    }



    public static Connection getConnection() {
        String db = "fish_and_chill";
        String host = "127.0.0.1";
        String port = "3306";
        String user = "root";
        String pass = "root";


        Connection connection = null;
        try {
             connection= DriverManager.getConnection("jdbc:mariadb://"+host+":"+port+"/"+
                    db+"?user="+user+"&password="+pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
//
//    public static Session openSession(String url, String user, String password) {
//        return null;
//    }
}
