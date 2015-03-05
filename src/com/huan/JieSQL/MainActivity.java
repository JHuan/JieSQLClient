
/*
 * Copyright (c) 2015.
 * @author barryjiang's.
 */

package com.huan.JieSQL;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.huan.JieSQL.Interface.SQLListener;
import com.huan.JieSQL.util.JieSQLUtil;

import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {


    private ActionBar               mActionBar;
    private FragmentManager         mFragmentManager;

    private ProgressDialog          mProgressDialog;
    private SQLListener             mListener;

    private JieSQLUtil              mSQLUtil;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBar = getActionBar();
        mActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));


        //connect to DB Now
        mListener = new ConnectListener();
        JieSQLAppliaction.g_SQLJieSQLUtil.addListener(mListener);

        JieSQLAppliaction.g_SQLJieSQLUtil.connect();

        mFragmentManager = getFragmentManager();

        mFragmentManager.beginTransaction().add(R.id.layout_main, new SQLStatementFragment()).commit();

    }

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onPrepareOptionMenu(Menu menu){
       
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                mFragmentManager.beginTransaction().replace(R.id.layout_main,new SettingFragment())
                .addToBackStack(null).commit();
                break;
            case R.id.action_add:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        JieSQLAppliaction.g_SQLJieSQLUtil.removeListener(mListener);
    }


    private class ConnectListener implements SQLListener{

        @Override
        public void onBeforeConnectDB() {
            mProgressDialog = ProgressDialog.show(MainActivity.this,"Hey,man!","Connecting to DB...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        public void onGetConnectResult(Boolean isConnected) {
            if (!isConnected) {
                Toast.makeText(MainActivity.this, "Connect failed! Check your configure,dude~", Toast.LENGTH_LONG).show();
            }
            mProgressDialog.dismiss();
        }

        @Override
        public void onSendingCommit() {

        }

        @Override
        public void onGetCommitResult(List<Map<String, Object>> resltSet) {

        }
    }
}
