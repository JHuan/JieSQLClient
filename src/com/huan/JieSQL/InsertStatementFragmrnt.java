package com.huan.JieSQL;/*
 * Copyright (c) 2015.
 * @author barryjiang's.
 */

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.huan.JieSQL.util.MySQLHelper;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by barryjiang on 2015/2/15.
 */
public class InsertStatementFragmrnt extends Fragment {

    View mRootView;


    EditText        mSQLResultText;
    Button          mCommitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_sql_statment, container, false);

        mSQLResultText = (EditText)mRootView.findViewById(R.id.editTextSQLResult);
        mCommitButton = (Button)mRootView.findViewById(R.id.buttonCommit);

        mCommitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testmySQL();
            }
        });

        return mRootView;
    }

    private void testmySQL(){

        String url = "jdbc:derby:C:\\Users\\barryjiang\\MyDB";
        String user = "admin";
        String pwd = "";

        MySQLHelper sqlHelper = new MySQLHelper();
        if(sqlHelper.connect2DB(url,user,pwd)){

            //test query

            List<Map<String,Object>> queryResult = null;
            List<String> paraQuery = new LinkedList<String>();
            paraQuery.add("Ted");
            try{
                queryResult = sqlHelper.getQueryResult("select * from MyDb.Student where name = ?",paraQuery);
            }catch (SQLException e){
                e.printStackTrace();
            }

            String result = "";
            for(Map<String,Object> map : queryResult){
                for(Map.Entry<String,Object> entry:map.entrySet()){
                    result+=entry.getKey()+"\t"+entry.getValue()+"\n";
                }
            }

            mSQLResultText.setText(result);
        }
    }
}