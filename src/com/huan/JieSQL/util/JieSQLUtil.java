package com.huan.JieSQL.util;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.huan.JieSQL.Interface.SQLListener;
import com.huan.JieSQL.JieSQLAppliaction;
import com.huan.JieSQL.R;
import com.huan.JieSQL.model.SQLClientSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/1.
 */
public class JieSQLUtil {

    private static final String TAG = "JieSQLUtil";

    private SharedPreferences           mPreferences;
    private boolean                     mConnected;
    private Context                     mContext;
    private MySQLHelper                 mSQLHepler;


    private List<SQLListener>           mListeners = new ArrayList<SQLListener>();

    public JieSQLUtil(Context context){

        if(context==null){
            throw new IllegalArgumentException("Context can't be null");
        }

        mPreferences = context.getSharedPreferences(SQLClientSetting.DB_NAME, Activity.MODE_PRIVATE);
        mContext = context;

        mSQLHepler = new MySQLHelper();
    }

    //just wired....I don't think is a good way to use it
    public void addListener(SQLListener listener) {
        mListeners.add(listener);
    }

    public boolean connect(){

        //get db url info and connect to it!
        final String url = mPreferences.getString(SQLClientSetting.DB_URL,null);
        final String user = mPreferences.getString(SQLClientSetting.DB_UERNAME,null);
        final String password = mPreferences.getString(SQLClientSetting.DB_PASSWORD,null);

        //WARNING!!! It can only use in main thread!!!!
        if(url!=null && user!=null && password!=null) {
            new AsyncTask<Void, Void, Boolean>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    if(mListeners.size()>0){
                        for(SQLListener listener:mListeners){
                            if(listener!=null)
                                listener.onBeforeConnectDB();
                        }
                    }
//                    progressDialog = ProgressDialog.show(mContext, "Hey,guy!", "Connecting to DB...");
//                    progressDialog.setCancelable(false);

                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    return JieSQLAppliaction.g_SQLJieSQLUtil.connect();

                }

                @Override
                protected void onPostExecute(Boolean connected) {
                    super.onPostExecute(connected);

                    if(mListeners.size()>0){
                        for(SQLListener listener:mListeners){
                            if(listener!=null)
                                listener.onGetConnectResult(connected);
                        }
                    }

                }

            }.execute();
        }
        else
            Log.e(TAG,"Get DB info null!"+"url:"+url+" userName:"+user+"password:"+password);

        return mConnected;
    }

    public List<Map<String,Object>> sycGetQueryResult(String sql,List<String> para){

        if(!mConnected){
            Toast.makeText(mContext, R.string.db_disconnected,Toast.LENGTH_SHORT).show();
            return null;
        }

        return mSQLHepler.getQueryResult(sql,para);
    }

    public int excuteUpdateSql(String sql,List<String> para){

        if(!mConnected){
            Toast.makeText(mContext, R.string.db_disconnected,Toast.LENGTH_SHORT).show();
            return 0;
        }

        return mSQLHepler.excuteUpdateSql(sql, para);
    }

    public void disconnect(){
        mSQLHepler.disconnectDB();
    }

    public boolean ismConnected() {
        return mConnected;
    }

}
