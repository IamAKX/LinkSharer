package com.akash.applications.linksharer;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

import Connector.Client;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by akash on 12/2/17.
 */

public class CommandReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ClipboardManager clipBoard = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = clipBoard.getPrimaryClip();
        ClipData.Item item = clipData.getItemAt(0);
        String textToDisplay = MainActivity.display.getText().toString()+CurrentTimeStamp.getTime()+" Launching browser > "+item.getText().toString()+"\n";
        MainActivity.display.setText(textToDisplay);
        MainActivity.scrollDownToBottomText();
        new Client(MainActivity.IPAddr).sendRequest(intent.getStringExtra("type")+"|"+item.getText().toString());
        if(new Setting(context).getSendSetting())
            vibrator(context);
    }

    private void vibrator(Context context) {
        Vibrator mVibrator  = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] vibratePattern = {100,100,200};
        mVibrator.vibrate(vibratePattern,-1);
    }
}
