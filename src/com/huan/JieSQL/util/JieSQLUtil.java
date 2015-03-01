package com.huan.JieSQL.util;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.huan.JieSQL.model.SQLClientSetting;

/**
 * Created by Administrator on 2015/3/1.
 */
public class JieSQLUtil {

    private SharedPreferences mPreferences;
    private Context mContext;

    public JieSQLUtil(Context context){

        mPreferences = context.getSharedPreferences(SQLClientSetting.DB_SQL_SETTING, Activity.MODE_PRIVATE);
        mContext = context;
        //
    }

    public boolean connect(){

        //get db url info and connect to it!
        String url = mPreferences.getString(SQLClientSetting.DB_URL,"hehe");
        String user = mPreferences.getString(SQLClientSetting.DB_UERNAME,"hehe");
        String password = mPreferences.getString(SQLClientSetting.DB_PASSWORD,"hehe");

        boolean isConnected;
        //start a asyTask to connect to DB
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }
        }.execute();
    }

    public String asyGetSQLResult(String sql){


    }
}
