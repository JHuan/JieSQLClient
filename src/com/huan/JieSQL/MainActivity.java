
/*
 * Copyright (c) 2015.
 * @author barryjiang's.
 */

package com.huan.JieSQL;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {


    ActionBar               mActionBar;
    FragmentManager         mFragmentManager;
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

        mFragmentManager = getFragmentManager();

        mFragmentManager.beginTransaction().add(R.id.layout_main, new InsertStatementFragmrnt()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
