package ru.ungeziefer.mainpackage.xmlUtils;

import java.io.*;
import java.util.*;

public class XMLReader {
    FileReader fr = null;
    String tableName;
    HashSet<TableRow> rows = new HashSet();
    Scanner scan = null;
    Iterator<TableRow> iterator;
    public XMLReader(String fileName, String tableName) {
        try {
            fr = new FileReader(fileName);
        } catch (FileNotFoundException ex){
            System.out.println("File not found.");
        }
        if (fr!=null){
            scan = new Scanner(fr);
        }
        this.tableName = tableName;
        loadRowsToList();

    }
    public TableRow nextRow(){
        if (iterator.hasNext()) return iterator.next();
        return null;
    }
    /*_______________PRIVATE______________*/
    private void loadRowsToList(){
        String temp = null;
        TableRow tempRow = new TableRow();
        int f = 0;
        while(scan.hasNextLine())
        {
            temp = scan.nextLine();
            if (temp.indexOf("/"+tableName)>0) {
                System.out.println(rows);
                rows.add(tempRow);
                tempRow = new TableRow();
                f = 0;
            }
            if (f > 0){
                int st, lt;
                st = temp.indexOf(">");
                lt = temp.indexOf("</");
                tempRow.setByTag(temp.substring(temp.indexOf("<")+1,st),temp.substring(st+1,lt));
                f++;
            }
            if (temp.indexOf("<"+tableName)>0) f = 1;
        }
        iterator = rows.iterator();
        try{
            fr.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}