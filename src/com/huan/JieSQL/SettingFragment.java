/*
 * Copyright (c) 2015.
 * @author barryjiang's.
 */

package com.huan.JieSQL;

import android.app.Activity;
import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


        //read the setting titles from xml
        mSettingTitles = getResources().getStringArray(R.array.setting_title);

        //read the setting from sharepreference
        mSharedPreferences = getActivity().getSharedPreferences(SQLClientSetting.DB_SQL_SETTING, Activity.MODE_PRIVATE);

        return mRootView;
    }


    public void onResume(){

        super.onResume();
        mListView = getListView();

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for(String title : mSettingTitles){
            HashMap<String,String> map = new HashMap<String, String>();
            map.put(MAIN_TITLE,title);
            map.put(SUB_TITLE,mSharedPreferences.getString(title,"呵呵"));
            data.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(),data,R.layout.list_setting,
                new String[]{MAIN_TITLE,SUB_TITLE},new int[]{R.id.textViewMainTitle,R.id.textViewSubTitle});

        mListView.setAdapter(adapter);
    }

}