
/*
 * Copyright (c) 2015.
 * @author barryjiang's.
 */

package com.huan.JieSQL;

import android.app.*;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.huan.JieSQL.Interface.SQLListener;
import com.huan.JieSQL.model.SQLClientSetting;
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

        mFragmentManager.beginTransaction().add(R.id.layout_main, new SQLStatementFragment(),SQLStatementFragment.TAG).commit();

    }

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {

        //it sucks.....But I can not find better way to do this.Why Fragment cannot get the top of its stack!

        Fragment fragment = mFragmentManager.findFragmentByTag(SettingFragment.TAG);
        if(fragment != null && fragment.isVisible()){
            menu.findItem(R.id.action_settings).setVisible(false);
            menu.findItem(R.id.action_add).setVisible(false);
        }

        fragment = mFragmentManager.findFragmentByTag(SQLStatementFragment.TAG);
        if(fragment!=null && fragment.isVisible()){
            menu.findItem(R.id.action_settings).setVisible(true);
            menu.findItem(R.id.action_add).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                mFragmentManager.beginTransaction().replace(R.id.layout_main,new SettingFragment(),SettingFragment.TAG)
                .addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case R.id.action_add:
                break;
            case R.id.action_retry:
                if(!JieSQLAppliaction.g_SQLJieSQLUtil.ismConnected())
                    JieSQLAppliaction.g_SQLJieSQLUtil.connect();
                else
                    Toast.makeText(this,"You have connected to DB now,boy.",Toast.LENGTH_SHORT).show();
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
