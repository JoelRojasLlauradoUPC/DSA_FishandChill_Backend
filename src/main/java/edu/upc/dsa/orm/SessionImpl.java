package edu.upc.dsa.orm;
import edu.upc.dsa.orm.helpers.ObjectHelper;
import edu.upc.dsa.orm.helpers.QueryHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SessionImpl implements Session {
    private final Connection conn;

    public SessionImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Object entity) {

        // Create INSERT query: INSERT INTO Class (id, b, c, d, e) VALUES (0, ?, ?, ?, ?)
        String insertQuery = QueryHelper.createQueryINSERT(entity);

        try {
            PreparedStatement pstm = null;
            pstm = conn.prepareStatement(insertQuery);
            pstm.setObject(1, 0);

            // Substitute ? with values from the entity
            int i = 2;
            for (String field: ObjectHelper.getFields(entity)) {
                if (field.equals("id")) continue;
                pstm.setObject(i, ObjectHelper.getter(entity, field));
                i++;
            }
            // Execute the query
            pstm.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public List<Object> get(Class theClass, HashMap params) {

        List<Object> objectList = new ArrayList<Object>();
        // Create SELECT query: SELECT * FROM Class WHERE a=? AND b=?
        String sql = QueryHelper.createQuerySELECT(theClass, params);

        try {
            // Substitute ? with values from the params
            PreparedStatement pstm = conn.prepareStatement(sql);
            int p=1;
            for (Object value : params.values()) {
                pstm.setObject(p++, value );
            }
            // Execute the query
            ResultSet res = pstm.executeQuery();

            while (res.next()) { //check if next row exists 1 row == an object
                Object object = theClass.getDeclaredConstructor().newInstance(); //create empty object of that class
                ResultSetMetaData rsmd = res.getMetaData();
                int numColumns = rsmd.getColumnCount(); //number of columns

                for (int i = 1; i <= numColumns; i++) {
                    String columnName = rsmd.getColumnName(i); //atribute name
                    int type = rsmd.getColumnType(i);
                    //System.out.println("type"+type);
                    Object value = res.getObject(i); //value of the atribute
                    if (type== Types.DECIMAL) {
                        value = Double.valueOf(value.toString());
                    }

                    //if (type==java.sql.Types.B
                    ObjectHelper.setter(object, columnName, value);  // llama al setter del helper
                }
                objectList.add(object);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectList;
    }

    @Override
    public void update(Object entity) {

        // SQL: UPDATE Class SET a=?, b=?, ... WHERE id=?
        String sql = QueryHelper.createQueryUPDATE(entity);

        try {
            PreparedStatement pstm = conn.prepareStatement(sql);


            int i = 1;
            for (String field: ObjectHelper.getFields(entity)) {
                if (field.equals("id")) continue;
                pstm.setObject(i, ObjectHelper.getter(entity, field));
                i++;
            }
            pstm.setObject(i, ObjectHelper.getter(entity, "id") );
            String query = pstm.toString();
            // Ejecuta UPDATE
            pstm.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Object entity) {

        // SQL: DELETE FROM Class WHERE id=?
        String sql = QueryHelper.createQueryDELETE(entity);

        try {
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setObject(1, ObjectHelper.getter(entity, "id"));
            pstm.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public List<Object> findAll(Class theClass) {
//        return null;
//    }
//
//    public List<Object> findAll(Class theClass, HashMap params) throws SQLException, InstantiationException, IllegalAccessException {
//        String theQuery = QueryHelper.createQuerySELECT(theClass, params);
//
//        PreparedStatement pstm = null;
//        pstm = conn.prepareStatement(theQuery);
//
//        int i=1;
//        for (Object value : params.values()) {
//            pstm.setObject(i++, value );
//        }
//        ResultSet rs = pstm.executeQuery();
//        theClass.newInstance();
//
//
//
//
//
//        return null;
//    }
//
//    public List<Object> query(String query, Class theClass, HashMap params) {
//        return null;
//    }
}