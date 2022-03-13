package ru.ungeziefer.mainpackage.sqlUtils;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;

public class Column {
    public String name;
    private SQLType tp;
    public Column(String name, SQLType tp) {
        this.name = name;
        this.tp = tp;
    }
    public void addParameter(int index, PreparedStatement st, Object value){
        try {
            st.setObject(index, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
