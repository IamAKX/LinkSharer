package com.akash.applications.linksharer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import static com.akash.applications.linksharer.R.drawable.small_icon;

public class CommunicatorService extends Service {

    NotificationManager manager;
    private MediaSessionCompat mMediaSession;

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mMediaSession = new MediaSessionCompat(getBaseContext(),"Button");
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);


        Intent linkIntent = new Intent();
        linkIntent.setAction("com.akash.applications.SHARELINK");
        linkIntent.putExtra("type","link");
        PendingIntent pendingIntentforLink = PendingIntent.getBroadcast(this, 1 , linkIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent textIntent = new Intent();
        textIntent.setAction("com.akash.applications.SHARETEXT");
        textIntent.putExtra("type","text");
        PendingIntent pendingIntentforText = PendingIntent.getBroadcast(this, 1 , textIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews mContentView = new RemoteViews(getPackageName(), R.layout.notification_layout);


        mContentView.setOnClickPendingIntent(R.id.linkShareBtn, pendingIntentforLink);
        mContentView.setOnClickPendingIntent(R.id.textShareBtn, pendingIntentforText);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext())
//                    .setContentTitle("Share the copied link")
                    .setSmallIcon(small_icon)
                    .setAutoCancel(false)
                    .setOngoing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(mContentView);
        } else {
            builder = builder
                    .setContentTitle("Share the copied link")
                    .setSmallIcon(small_icon)
                    .setContentIntent(pendingIntentforText);
        }


        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.cancel(0);
    }
}
