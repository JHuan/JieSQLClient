package com.huan.JieSQL;/*
 * Copyright (c) 2015.
 * @author barryjiang's.
 */

import android.app.ActionBar;
import android.app.Fragment;
import android.os.AsyncTask;
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


        new AsyncTask<Void,Void,Void>(){

            String result = "";
            @Override
            protected Void doInBackground(Void... para){

                String url = "jdbc:mysql://113.29.229.38/hsmim_test";
                String user = "hsmim_test";
                String pwd = "hsmim_test";

                MySQLHelper sqlHelper = new MySQLHelper();
                if(sqlHelper.connect2DB(url,user,pwd)){

                    //test query

                    List<Map<String,Object>> queryResult = null;
                    List<String> paraQuery = new LinkedList<String>();
                    paraQuery.add("1");
                    queryResult = sqlHelper.getQueryResult("select * from test",null);



                    for(Map<String,Object> map : queryResult){
                        for(Map.Entry<String,Object> entry:map.entrySet()){
                            result+=entry.getKey()+"\t"+entry.getValue()+"\n";
                        }
                    }

                }
                return null;
            }

            protected void onPostExecute(Void para){
                mSQLResultText.setText(result);
            }
        }.execute();





    }
}