package ru.ungeziefer.mainpackage.xmlUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class TableRow {
    LinkedHashMap<String, Object> columns = new LinkedHashMap();
    public void setByTag(String tag, Object data){
        columns.put(tag, data);
        System.out.println(columns.get(tag));
    }
    public TableRow(){}
    public Object getByTag(String key){
        return columns.get(key);
    }
}
