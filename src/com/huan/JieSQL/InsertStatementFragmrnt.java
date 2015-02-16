package com.huan.JieSQL;/*
 * Copyright (c) 2015.
 * @author barryjiang's.
 */

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by barryjiang on 2015/2/15.
 */
public class InsertStatementFragmrnt extends Fragment {

    View mRootView;

    ActionBar mActionBar;

    EditText mSQLResultText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_sql_statment, container, false);

        //Initialize editText.
        mSQLResultText = (EditText)mRootView.findViewById(R.id.editTextSQLResult);

        return mRootView;
    }
}