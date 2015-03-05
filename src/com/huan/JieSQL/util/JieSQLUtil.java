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
import com.huan.JieSQL.model.SQLResultData;

import java.util.*;

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


    public void removeListener(SQLListener listener){
        mListeners.remove(listener);
    }



    public void connect(){

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
                    return mSQLHepler.connect2DB(url,user,password);

                }

                @Override
                protected void onPostExecute(Boolean connected) {
                    super.onPostExecute(connected);

                    mConnected = connected;

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


    }

    public void sycGetSQLResult(String _sql,List<String> _para){

        final String sql = _sql;
        final List<String> para = _para;

        if(!mConnected){
            Toast.makeText(mContext, R.string.db_disconnected,Toast.LENGTH_SHORT).show();
            return;
        }

        new AsyncTask<Void,Void,List<Map<String,Object>>>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(mListeners.size()>0){
                    for(SQLListener listener:mListeners){
                        if(listener!=null)
                            listener.onSendingCommit();
                    }
                }
            }


            @Override
            protected List<Map<String, Object>> doInBackground(Void... params) {
                //judge if is a query sql
                if(sql.indexOf("select",0)==0){
                    return mSQLHepler.getQueryResult(sql,para);
                }else {
                    return mSQLHepler.excuteUpdateSql(sql,para);
                }
            }

            @Override
            protected void onPostExecute(List<Map<String, Object>> result) {

                if(mListeners.size()>0){
                    for(SQLListener listener:mListeners){
                        if(listener!=null)
                            listener.onGetCommitResult(result);
                    }
                }

            }
        }.execute();

    }



    public void disconnect(){
        mSQLHepler.disconnectDB();
    }

    public boolean ismConnected() {
        return mConnected;
    }

}
