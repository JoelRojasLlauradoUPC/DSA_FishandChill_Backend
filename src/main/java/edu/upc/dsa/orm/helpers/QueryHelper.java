package edu.upc.dsa.orm.helpers;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryHelper {

    public static String createQueryINSERT(Object theClass) {

        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(theClass.getClass().getSimpleName()).append(" ");
        sb.append("(");

        String [] fields = ObjectHelper.getFields(theClass);

        sb.append("id");
        for (String field: fields) {
            if (!field.equals("id")) sb.append(", ").append(field);
        }
        sb.append(") VALUES (?");

        for (String field: fields) {
            if (!field.equals("id"))  sb.append(", ?");
        }
        sb.append(")");
        // INSERT INTO Class (id, lastName, firstName, address, city) VALUES (0, ?, ?, ?, ?)
        return sb.toString();
    }

//    public static String createQuerySELECT(Object entity) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("SELECT * FROM ").append(entity.getClass().getSimpleName());
//        sb.append(" WHERE id = ?");
//
//        return sb.toString();
//    }

    public static String createQuerySELECT(Class theClass, HashMap<String, String> params) {

        Set<Map.Entry<String, String>> set = params.entrySet();

        StringBuffer sb = new StringBuffer("SELECT * FROM "+theClass.getSimpleName()+" WHERE 1=1");
        for (String key: params.keySet()) {
            sb.append(" AND "+key+"=?");
        }


        return sb.toString();

        // SELECT * FROM Class WHERE 1=1 AND a=? AND b=?
    }

    public static String createQueryUPDATE(Object theClass) {

        StringBuffer sb = new StringBuffer("UPDATE ");
        sb.append(theClass.getClass().getSimpleName()).append(" SET ");

        String[] fields = ObjectHelper.getFields(theClass);

        for (String field: fields) {
            if (!field.equals("id")) sb.append(field).append("= ?, ");
        }
        sb.delete(sb.length()-2, sb.length()); // remove last ", "

        sb.append(" WHERE id=?");

        return sb.toString();

        // UPDATE Class SET a=?, b=? WHERE id=Class.getId()
    }

    public static String createQueryDELETE(Object theClass) {

        StringBuffer sb = new StringBuffer("DELETE FROM ");
        sb.append(theClass.getClass().getSimpleName());
        sb.append(" WHERE id=?");

        // DELETE FROM Class WHERE id=?
        return sb.toString();
    }


}