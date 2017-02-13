package com.akash.applications.linksharer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import Connector.IPAddress;

public class MainActivity extends AppCompatActivity {
    public static TextView display;
    Button start, stop;
    Switch connectVibrate, sendVibrate;
    Context context;
    static String IPAddr = null;
    boolean enteredIP = false;
    static ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        display = (TextView) findViewById(R.id.statusDisplay);
        start = (Button) findViewById(R.id.btnStart);
        stop = (Button) findViewById(R.id.btnStop);
        connectVibrate = (Switch) findViewById(R.id.connectVibrateSwitch);
        sendVibrate = (Switch) findViewById(R.id.sendVibrateSwitch);
        scrollView = (ScrollView) findViewById(R.id.displayScroll);

        initSettings();

        connectVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    connectVibrate.setChecked(false);
                    new Setting(context).setInitSetting(false);
                }
                else
                {
                    connectVibrate.setChecked(true);
                    new Setting(context).setInitSetting(true);
                }
            }
        });

        sendVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    sendVibrate.setChecked(false);
                    new Setting(context).setSendSetting(false);
                }
                else
                {
                    sendVibrate.setChecked(true);
                    new Setting(context).setSendSetting(true);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToDisplay = display.getText().toString()+CurrentTimeStamp.getTime()+" Requesting for Server IP\n";
                display.setText(textToDisplay);
                scrollDownToBottomText();
                showIPEnterDialog();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToDisplay= display.getText().toString()+CurrentTimeStamp.getTime()+" Aborting communication service...\n";
                display.setText(textToDisplay);
                scrollDownToBottomText();
                stop.setEnabled(false);
                start.setEnabled(true);
                stopService(new Intent(context,CommunicatorService.class));
                textToDisplay= display.getText().toString()+CurrentTimeStamp.getTime()+" Closing PORT 444...\n";
                display.setText(textToDisplay);
                scrollDownToBottomText();
            }
        });


        if(isServiceRunning(CommunicatorService.class))
        {
            start.setEnabled(false);
            stop.setEnabled(true);
            display.setText("Service is running...\n");
            scrollDownToBottomText();
        }
        else
        {
            start.setEnabled(true);
            stop.setEnabled(false);
        }
    }

    private void initSettings() {
        if(new Setting(context).getInitSetting())
            connectVibrate.setChecked(true);
        if (new Setting(context).getSendSetting())
            sendVibrate.setChecked(true);
    }

    private void showIPEnterDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LinearLayout layout       = new LinearLayout(this);
        final EditText etInput    = new EditText(this);
        etInput.setSingleLine();
        etInput.setHint("0.0.0.0");
        etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        etInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(etInput);
        alert.setTitle("Enter server IP");
        alert.setView(layout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enteredIP = false;
                dialog.dismiss();
                String textToDisplay = display.getText().toString() + CurrentTimeStamp.getTime() + " IP request was refused, aborting initilization process";
                display.setText(textToDisplay);
                scrollDownToBottomText();
            }
        });

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IPAddr = etInput.getText().toString();
                if(!validateIP(IPAddr))
                {
                    Toast.makeText(context,"Invalid IP Address",Toast.LENGTH_SHORT).show();
                    return;
                }
                stop.setEnabled(true);
                start.setEnabled(false);
                startService(new Intent(context,CommunicatorService.class));
                String textToDisplay = display.getText().toString() + CurrentTimeStamp.getTime() + " Starting server at " + IPAddr + " on 4444\n";
                display.setText(textToDisplay);
                scrollDownToBottomText();
                if(new Setting(context).getInitSetting())
                    vibrator();
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private boolean validateIP(String ipAddr) {
        boolean isValid = false;
        if(ipAddr.equals(""))
            return isValid;
        int noOfDots = 0;
        for(char c : ipAddr.toCharArray())
        {
            if(c == '.')
                noOfDots++;
        }
        if(noOfDots != 3)
            return isValid;
        isValid = true;
        return isValid;
    }

    private boolean isServiceRunning(Class<CommunicatorService> communicatorServiceClass) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE))
        {
            if(communicatorServiceClass.getName().equals(serviceInfo.service.getClassName()))
                return true;
        }
        return false;
    }

    public static void scrollDownToBottomText()
    {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void vibrator()
    {
        Vibrator mVibrator  = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] vibratePattern = {100,100,200};
        mVibrator.vibrate(vibratePattern,-1);
    }
}
