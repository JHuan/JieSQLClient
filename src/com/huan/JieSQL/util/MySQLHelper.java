/*
 * Copyright (c) 2015.
 * @author barryjiang's.
 */

package com.huan.JieSQL.util;

import android.util.Log;
import com.mysql.jdbc.*;
import org.apache.derby.jdbc.EmbeddedDriver;


import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * Well, as the same way, jdbc is the simplest way,so...
 * Created by barryjiang on 2015/2/26.
 */
public class MySQLHelper {

    private Connection mConnection;

    public boolean connect2DB(String url,String userName, String password){

        boolean connected = false;
        try {
            //com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
            org.apache.derby.jdbc.EmbeddedDriver driver = new EmbeddedDriver();
            mConnection = DriverManager.getConnection(url, userName, password);
            connected = true;
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return connected;
    }

    //get sql query result. sql statement require the format of "select ? where name = ?"
    public  List<Map<String,Object>> getQueryResult(String sql,List<String> para) throws SQLException{

        List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();

        PreparedStatement statment = mConnection.prepareStatement(sql);

        if(para!=null && para.size()>0){
            for(int i=0;i<para.size();i++){
                //NOTICE: sql statement slot seems it use 1 index rather than 0 index...
                statment.setObject(i+1,para.get(i));
            }
        }

        ResultSet resultSet = statment.executeQuery();
        ResultSetMetaData resultSetMetaData = statment.getMetaData();

        //get column's length
        int columnLen = resultSetMetaData.getColumnCount();
        if(resultSet.first()){
            do{
                Map<String,Object> map = new HashMap<String, Object>();
                for(int i = 0; i<columnLen; i++){
                    String columnName = resultSetMetaData.getColumnName(i+1);
                    Object object = resultSet.getObject(i);
                    map.put(columnName,object);
                }
                result.add(map);
            }while(resultSet.next());
        }
        return result;

    }

    public int excuteUpdateSql(String sql,List<String> para) throws SQLException{

        PreparedStatement statment = mConnection.prepareStatement(sql);

        if(para!=null && para.size()>0){
            for(int i=0;i<para.size();i++){
                //NOTICE: sql statement slot seems it use 1 index rather than 0 index...
                statment.setObject(i+1,para.get(i));
            }
        }

        return statment.executeUpdate();
    }


}
