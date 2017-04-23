package com.xaskysab.gcomlper;


import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.VariableElement;

/**
 * Created by XaskYSab on 2017/4/18 0018.
 */

public class WriteUtil {


    public static void writeCst(Writer writer, String constructName, Map<String, String> genyMap, Modified modified) throws IOException {

        List<String> cf = new ArrayList<>();
        List<String> paramValue = new ArrayList<>();
        List<String> bodyParam = new ArrayList<>();
        List<String> all = new ArrayList<>();

        StringBuffer body = new StringBuffer("{");
        Iterator iterable = genyMap.keySet().iterator();

        String param = "";

        do {
            String cn = (String) iterable.next();

            param += "%s %s";
            body.append("this.%s = %s;\n");
            if (iterable.hasNext()) {
                param += ",";
            } else {
                body.append("}");
            }

            String fn = genyMap.get(cn);
            cf.clear();
            cf.add(fn);
            cf.add(fn);
            bodyParam.addAll(cf);
            cf.remove(0);
            cf.add(0, cn);
            paramValue.addAll(cf);

        } while (iterable.hasNext());

        all.add(modified.M);
        all.add(constructName);
        all.addAll(paramValue);
        all.addAll(bodyParam);


        writer.write(String.format("%s %s(" + param + ") " + body, all.toArray()));

        writer.write("\n");

    }


    public static void writeProper(Writer writer, String className, String fieldName, Modified modified) throws IOException {

        writer.write(modified.M  + className + " " + fieldName);
        writer.write(";\n");


    }


    public static void writeGet(Writer writer, String className, String fieldName) throws IOException {

        char[] charArray = fieldName.toCharArray();
        charArray[0] = (charArray[0] + "").toUpperCase().charAt(0);
        String getFieldName = new String(charArray);

        writer.write(" public " + className + " get" + getFieldName + "() {\n" +
                "        return " + fieldName + ";\n" + "    }");

        writer.write("\n");

    }


    public static void writeSet(Writer writer, String className, String fieldName) throws IOException {

        char[] charArray = fieldName.toCharArray();
        charArray[0] = (charArray[0] + "").toUpperCase().charAt(0);
        String setFieldName = new String(charArray);

        writer.write("    public void set" + setFieldName + "(" + className + " " + fieldName + ") {\n" +
                "        this." + fieldName + " = " + fieldName + ";\n" + "    }");
        writer.write("\n");


    }


    public enum Modified {
        PUBLIC(" public "),
        PROTECTD(" protected "),
        PRIVATE(" private ");

        public String M;

        private Modified(String m) {
            M = m;
        }

    }


}
