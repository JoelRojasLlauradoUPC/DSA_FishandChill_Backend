package edu.upc.dsa.utils;
import java.sql.*; // JDBC

public class DBJDBC {

    public static void insert() throws SQLException{
        Connection connection = DBUtils.getConnection();
        String query = "INSERT INTO User (username, email, password, coins) VALUES (?, ?, ?, ?)";
        PreparedStatement statement1  = connection.prepareStatement(query);
        statement1.setString(1, "jairo");
        statement1.setString(2, "jairo@upd.edu");
        statement1.setString(3, "hola");
        statement1.setInt(4, 1111);
        statement1.execute();
        // i = x / 0
        connection.close();

/*
        Connection connection=null;
        try {
            connection = DBUtils.getConnection();


            Statement statement1  = connection.createStatement();
            statement1.execute("INSERT INTO User (ID, lastName, firstName, address, city) VALUES (0,'88888', 'thMceF\'irstName', 'theAddress', 'theCity')");
            //connection.close();

        }
        catch (Exception e) {
           // ...
        }
        finally {
            connection.close();
        }
*/
    }


    private static String getType (int type) {
        String ret = null;
        switch (type) {
            case java.sql.Types.VARCHAR:
                ret ="VARCHAR";
                break;
            case java.sql.Types.INTEGER:
                ret = "INTEGER";
                break;
            case java.sql.Types.DATE:
                ret = "DATE";
                break;

        }

        return ret;
    }


    public static void findAll()  throws Exception {
        Connection connection = null;
        try {
            connection = DBUtils.getConnection();
            Statement statement2 = connection.createStatement();
            ResultSet rs = statement2.executeQuery("SELECT *  FROM User WHERE 1=1");
            // INTROSPECCIÓ BBDD
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("cols "+rsmd.getColumnCount());
            int i = 1;
            while (i<=rsmd.getColumnCount()) {
                System.out.println("col  "+i+" "+rsmd.getColumnName(i)+" "+
                        rsmd.getColumnType(i)+ " "+DBJDBC.getType(rsmd.getColumnType(i)));
                i++;
            }

            int id, coins;
            String username, email, password;

            while (rs.next()) {
                id = (Integer) rs.getObject(1); // 0
                username = (String) rs.getObject(2);
                email = (String) rs.getObject(3);
                password = (String) rs.getObject(4);
                coins = (Integer) rs.getObject(5);
                System.out.println(id+" "+username+" "+email+" "+password+" "+coins);

                // Per cada propietat dins de la fila:
                // while (j<=rsmd.getNumColumn()) {
                // theValue = rs.getObject(j);
                // theProperty = rsmd.getColumnName(j);
                // ObjectHelper.setter(theObject, theProperty, theValue);
                // }
            }
        } catch (Exception e) {
            //log.d ("", e)
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public static void main(String[] args) throws Exception {
        insert();
        findAll();
    }

}
