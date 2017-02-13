package com.akash.applications.linksharer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by akash on 13/2/17.
 */

public class Setting {

    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    final String FILE_NAME = "setting_saver";
    final String INIT_SETTING = "init_setting";
    final String SEND_SETTING = "send_setting";

    public Setting(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
    }

    public void setInitSetting(boolean state)
    {
        editor = preferences.edit();
        editor.putBoolean(INIT_SETTING,state);
        editor.commit();
    }

    public boolean getInitSetting()
    {
        return preferences.getBoolean(INIT_SETTING,false);
    }

    public void setSendSetting(boolean state)
    {
        editor = preferences.edit();
        editor.putBoolean(SEND_SETTING,state);
        editor.commit();
    }

    public boolean getSendSetting()
    {
        return preferences.getBoolean(SEND_SETTING,false);
    }
}
