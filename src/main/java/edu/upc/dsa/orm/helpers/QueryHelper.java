package edu.upc.dsa.orm.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryHelper {

    public static String createQueryINSERT(Object entity) {

        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName()).append(" ");
        sb.append("(");

        String [] fields = ObjectHelper.getFields(entity);

        sb.append("id");
        for (String field: fields) {
            if (!field.equals("id")) sb.append(", ").append(field);
        }
        sb.append(") VALUES (?");

        for (String field: fields) {
            if (!field.equals("id"))  sb.append(", ?");
        }
        sb.append(")");
        // INSERT INTO User (id, lastName, firstName, address, city) VALUES (0, ?, ?, ?,?)
        return sb.toString();
    }

    public static String createQuerySELECT(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(entity.getClass().getSimpleName());
        sb.append(" WHERE id = ?");

        return sb.toString();
    }

    public static String createQuerySELECT(String entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(entity);
        sb.append(" WHERE id = ?");

        return sb.toString();
    }


    public static String createSelectFindAll(Class theClass, HashMap<String, String> params) {

        Set<Map.Entry<String, String>> set = params.entrySet();

        StringBuffer sb = new StringBuffer("SELECT * FROM "+theClass.getSimpleName()+" WHERE 1=1");
        for (String key: params.keySet()) {
            sb.append(" AND "+key+"=?");
        }


        return sb.toString();
    }

    public static String createSelectFindAll(String theClass, HashMap<String, String> params) {

        Set<Map.Entry<String, String>> set = params.entrySet();

        StringBuffer sb = new StringBuffer("SELECT * FROM "+theClass+" WHERE 1=1");
        for (String key: params.keySet()) {
            sb.append(" AND "+key+"=?");
        }


        return sb.toString();
    }
}