package com.akash.applications.linksharer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by akash on 13/2/17.
 */

public class CurrentTimeStamp {
    public static String getTime()
    {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int sec = Calendar.getInstance().get(Calendar.SECOND);
        String day = new SimpleDateFormat("EE", Locale.ENGLISH).format(Calendar.getInstance().getTime().getTime());
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String time = "["+formatTime(hour)+":"+formatTime(minute)+":"+formatTime(sec)+" | "+day+" "+formatTime(date)+"]";
        return time;
    }

    private static String formatTime(int hour) {
        String d = String.valueOf(hour);
        if(d.length()==1)
            d = "0"+d;
        return d;
    }


}
