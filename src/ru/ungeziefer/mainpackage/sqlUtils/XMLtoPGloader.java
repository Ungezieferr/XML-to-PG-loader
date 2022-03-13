package ru.ungeziefer.mainpackage.sqlUtils;

import ru.ungeziefer.mainpackage.xmlUtils.TableRow;
import ru.ungeziefer.mainpackage.xmlUtils.XMLReader;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class XMLtoPGloader extends XMLtoSQLloader {
    final String USER;
    final String URL_BD;
    final String PASS;
    ArrayList<String> conflStats = new ArrayList<>();
    String updateAction = null;
    public XMLtoPGloader(String user, String url_bd, String pass) {
        USER = user;
        URL_BD = url_bd;
        PASS = pass;
    }
    public int uploadDATA(){
        getConnection();
        if ((connection!=null)&&(columns.size()>0)){
            createSQLString();
            return pushData(500);
        }
        return -1;
    }
    public void addConflictStat(String stateName){
        conflStats.add(stateName);
    }
    public void setUpdateAction(String sqlStartFromDoUpdate){
        updateAction = sqlStartFromDoUpdate;
    }
    void createSQLString(){
        StringBuilder str = new StringBuilder();
        str.append("INSERT INTO ");
        str.append(tableName);
        str.append("(");
        for (int i = 0;i<columns.size();i++) {
            if (i>=1)str.append(", ");
            str.append(columns.get(i).name);
        }
        str.append(") VALUES(");
        for (int i = 0;i<columns.size();i++) {
            if (i>=1)str.append(", ");
            str.append(" ?");
        }
        str.append(") ON CONFLICT(");
        for (int i = 0;i<conflStats.size();i++) {
            if (i>=1)str.append(", ");
            str.append(conflStats.get(i));
        }
        str.append(") ");
        if(updateAction==null){
            str.append("DO NOTHING");
        } else {
            str.append(updateAction);
        }
        this.sql = str.toString();
    }

    public void setTableName(String name){
        tableName = name;
    }
    public int setDataFile(String fileName){
        if(tableName!=null){
            openDataFileName(fileName, tableName);
            return 0;
        } else System.out.println("table name must be not null.");
        return 1;
    }
    private void openDataFileName(String name, String tableName){
        dataFile= new XMLReader(name, tableName);

    }
    @Override
    void getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver don't found.");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL_BD, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }

    @Override
    int pushData(int maxBatchCount) {
        int rowsCounter = 0;
        try {

            stat = connection.prepareStatement(sql);
            TableRow a = dataFile.nextRow();
            connection.setAutoCommit(false);
            int batchCounter = 0;
            while (a != null){
                for(int i = 0; i< columns.size(); i++){
                    Column cl = columns.get(i);
                    cl.addParameter(i+1, stat, a.getByTag(cl.name));
                }
                if(batchCounter>=maxBatchCount) {
                    rowsCounter += Arrays.stream(stat.executeBatch()).sum();
                    batchCounter = 0;
                }
                System.out.println(stat);
                stat.addBatch();
                a = dataFile.nextRow();
            }
            rowsCounter += Arrays.stream(stat.executeBatch()).sum();
            stat.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsCounter;
    }
}
