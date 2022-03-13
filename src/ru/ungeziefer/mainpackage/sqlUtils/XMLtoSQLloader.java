package ru.ungeziefer.mainpackage.sqlUtils;

import ru.ungeziefer.mainpackage.xmlUtils.XMLReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLType;
import java.util.ArrayList;

public abstract class XMLtoSQLloader {
    PreparedStatement stat = null;
    Connection connection = null;
    ArrayList<Column> columns = new ArrayList<>();
    String sql = null;
    XMLReader dataFile;
    String tableName;
    abstract void getConnection();
    public void addColumn(String name, SQLType tp){
        columns.add(new Column(name, tp));
    }
    abstract int pushData(int maxButchCount);
}
