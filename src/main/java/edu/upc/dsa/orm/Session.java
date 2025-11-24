package edu.upc.dsa.orm;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface Session {
    void save(Object entity);                                           // Crud
    List<Object> get(Class theClass, HashMap params);                   // cRud
    void update(Object entity);                                         // crUd
    void delete(Object entity);                                         // cruD

    void close();
//    List<Object> findAll(Class theClass, HashMap params) throws SQLException, InstantiationException, IllegalAccessException;
//    List<Object> query(String query, Class theClass, HashMap params);
}