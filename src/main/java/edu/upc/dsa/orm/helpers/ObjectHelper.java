package edu.upc.dsa.orm.helpers;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectHelper {
    public static String[] getFields(Object entity) {

        Class theClass = entity.getClass();

        Field[] fields = theClass.getDeclaredFields();

        String[] sFields = new String[fields.length];
        int i=0;

        for (Field f: fields) sFields[i++]=f.getName();

        return sFields;

    }


    public static void setter(Object object, String property, Object value) {
        Class theClass = object.getClass();
        Field[] fields = theClass.getDeclaredFields();
        for (Field f: fields) {
            if (f.getName().equals(property)) {
                f.setAccessible(true); // turn off access check for private fields
                try {
                    f.set(object, value);
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static Object getter(Object object, String property) {
        Class theClass = object.getClass();
        Field[] fields = theClass.getDeclaredFields();
        for (Field f: fields) {

            if (f.getName().equals(property)) {
                f.setAccessible(true);
                try {
                    return f.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
