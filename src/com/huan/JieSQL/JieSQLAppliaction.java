package com.huan.JieSQL;

import android.app.Application;
import android.content.Intent;
import android.content.ServiceConnection;
import com.huan.JieSQL.util.JieSQLUtil;

/**
 * Created by barryjiang on 2015/3/4.
 */
public class JieSQLAppliaction extends Application {

    public static JieSQLUtil        g_SQLJieSQLUtil;

    public void onCreate() {
        g_SQLJieSQLUtil = new JieSQLUtil(this);

    }

    public void onTerminate(){
        g_SQLJieSQLUtil.disconnect();
    }
}
