/*
 * Copyright (c) 2015.
 * @author barryjiang's.
 */

package com.huan.JieSQL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.huan.JieSQL.model.SQLClientSetting;


import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by barryjiang on 2015/2/16.
 */
public class SettingFragment extends ListFragment {

    private View                mRootView;
    private ListView            mListView;

    private String[]            mSettingTitles;
    private SharedPreferences   mSharedPreferences;

    private final static String MAIN_TITLE  = "main_title";
    private final static String SUB_TITLE   = "sub_title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_setting, container, false);

        //read the setting from sharepreference
        mSharedPreferences = getActivity().getSharedPreferences(SQLClientSetting.DB_SQL_SETTING, Activity.MODE_PRIVATE);

        return mRootView;
    }


    public void onResume(){

        super.onResume();


        //setting up the setting list  [barryjiang 2015/2/22]

        //read the setting titles from xml
        mSettingTitles = getResources().getStringArray(R.array.setting_title);
        mListView = getListView();

        final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for(String title : mSettingTitles){
            HashMap<String,String> map = new HashMap<String, String>();
            map.put(MAIN_TITLE,title);
            map.put(SUB_TITLE,mSharedPreferences.getString(title,"呵呵"));
            data.add(map);
        }

        final SimpleAdapter adapter = new SimpleAdapter(getActivity(),data,R.layout.list_setting,
                new String[]{MAIN_TITLE,SUB_TITLE},new int[]{R.id.textViewMainTitle,R.id.textViewSubTitle});

        mListView.setAdapter(adapter);

        //setting list view's listener;

        final View settingView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_setting ,null);
        final EditText editText = (EditText)settingView.findViewById(R.id.editTextContent);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int index = i;
                new AlertDialog.Builder(getActivity())
                        .setTitle(mSettingTitles[index])
                        .setView(settingView)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String setting = editText.getText().toString();
                                if (setting != null && setting.length() > 0) {
                                    //store in database and notify the list view to refresh the data shown
                                    mSharedPreferences.edit().putString(mSettingTitles[index], setting).commit();
                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put(MAIN_TITLE,mSettingTitles[index]);
                                    map.put(SUB_TITLE,setting);
                                    data.set(index,map);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }
        });
    }

}