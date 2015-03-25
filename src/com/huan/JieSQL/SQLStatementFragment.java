package com.huan.JieSQL;/*
 * Copyright (c) 2015.
 * @author barryjiang's.
 */

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huan.JieSQL.Interface.SQLListener;
import com.huan.JieSQL.model.SQLTemplate;
import com.huan.JieSQL.util.MySQLHelper;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by barryjiang on 2015/2/15.
 */
public class SQLStatementFragment extends Fragment {

    public static final String         TAG="InsertStatementFragment";

    private View mRootView;

    private List<TextView>              mSQLStatementViews;

    private EditText                    mSQLResultText;
    private Button                      mCommitButton;
    private LinearLayout                mSQLTextViewsLayout;

    private SharedPreferences           mPreferences;

    private SQLListener                 mListener;
    private ProgressDialog              mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_sql_statment, container, false);

        getActivity().getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);

        //set result text HorizontallyScrolling
        mSQLResultText = (EditText)mRootView.findViewById(R.id.editTextSQLResult);
        mSQLResultText.setHorizontalFadingEdgeEnabled(true);
        mSQLResultText.setVerticalScrollBarEnabled(true);
        mSQLResultText.setHorizontallyScrolling(true);
        mSQLResultText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    v.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_text_frame));
                else
                    v.setBackgroundDrawable(getResources().getDrawable(R.drawable.edi_text_frame_focus));

            }
        });

        mCommitButton = (Button)mRootView.findViewById(R.id.buttonCommit);

        mPreferences = getActivity().getSharedPreferences(SQLTemplate.DB_NAME, Activity.MODE_PRIVATE);

        mCommitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sql = getSqlStatementFromView(mSQLStatementViews);
                asyGetSQLResult(sql);

            }
        });

        getActivity().getActionBar().setTitle(R.string.action_bar_title_sql_editing);

        mSQLTextViewsLayout = (LinearLayout)mRootView.findViewById(R.id.layoutTextViews);
        mSQLStatementViews = new LinkedList<TextView>();

        //get last time used template
        String index = mPreferences.getString(SQLTemplate.DB_LAST_INDEX,null);
        String template = SQLTemplate.DEAFAULT_TEMPLATE;
        if(index != null){
            //get last used template
            template = mPreferences.getString(index,null);
            if(template==null){
                Log.e(TAG,"last used sql is null!What the hell!!!");
                template = SQLTemplate.DEAFAULT_TEMPLATE;
                return mRootView;
            }
        }
        //according to the template we generate the textviews
        generateTextViewsFromTemplate(template);

        return mRootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onDestroyView(){
        super.onDestroyView();

        JieSQLAppliaction.g_SQLJieSQLUtil.removeListener(mListener);
    }

    //get String from list of sql text views
    private String getSqlStatementFromView(List<TextView> listViews){

        if(listViews==null || listViews.size()==0){
            Log.e(TAG,"the list of sql text views is null!");
            return null;
        }

        String sql = "";

        for(TextView view:listViews){
            sql+=view.getText().toString();
        }

        return sql;
    }

    private void generateTextViewsFromTemplate(String template) {

        int lastSoltIndex = 0;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0;i<template.length();i++){
            char c = template.charAt(i);
            if(c==SQLTemplate.TEMPLATE_KEY){
                //new a editText
                EditText editText = new EditText(getActivity());
                editText.setText(SQLTemplate.DEAFAULT_SLOT_TEXT);
                editText.setTextColor(Color.rgb(0, 0, 0));
                editText.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_text_frame));
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus)
                            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_text_frame));
                        else
                            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.edi_text_frame_focus));

                    }
                });
                editText.setTextSize(20);

                //generate the textviews before
                if(c>0){
                    String text = template.substring(lastSoltIndex,i);
                    TextView textView = new TextView(getActivity());
                    textView.setTextColor(Color.rgb(0,0,0));
                    textView.setText(text);
                    textView.setTextSize(20);
                    mSQLStatementViews.add(textView);
                    mSQLTextViewsLayout.addView(textView,layoutParams);
                }
                //add the slot
                mSQLStatementViews.add(editText);
                mSQLTextViewsLayout.addView(editText,layoutParams);

                //record last slot index
                lastSoltIndex = i;
            }

        }
    }

    private void asyGetSQLResult(String _sql){

        final String sql = _sql;
        new AlertDialog.Builder(getActivity())
        .setTitle("Notice").setMessage("Sure?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JieSQLAppliaction.g_SQLJieSQLUtil.sycGetSQLResult(sql,null);
                    }
                })
                .setNegativeButton(android.R.string.no,null)
                .show();
    }


    private class CommitListener implements SQLListener{

        @Override
        public void onBeforeConnectDB() {

        }

        @Override
        public void onGetConnectResult(Boolean isConnected) {

        }

        @Override
        public void onSendingCommit() {
            mProgressDialog = ProgressDialog.show(getActivity(), "Hey,man!", "Commit...");
            mProgressDialog.setCancelable(false);

        }

        @Override
        public void onGetCommitResult(List<Map<String, Object>> resltSet) {
            mProgressDialog.dismiss();
            String result = "";
            for(Map<String,Object> map : resltSet){
                for(Map.Entry<String,Object> entry:map.entrySet()){
                    result+=entry.getKey()+"\t"+entry.getValue()+"\n";
                }
            }
            mSQLResultText.setText(result);

        }
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