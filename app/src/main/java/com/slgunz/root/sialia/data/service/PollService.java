package com.slgunz.root.sialia.data.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.source.local.PreferenceHelper;
import com.slgunz.root.sialia.ui.login.LoginActivity;

import java.io.IOException;

import javax.inject.Inject;

/**
 * An implementation of service for checking new tweets
 */

public class PollService extends IntentService {
    private static final String TAG = "PollService";

    private static final long TIME_INTERVAL = 1000 * 60;    // one minute

    public static final String ACTION_SHOW_NOTIFICATION =
            "com.slgunz.root.sialia.data.service.ACTION_SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE = "com.slgunz.root.sialia.data.service.PRIVATE";

    public static final String CHANNEL_SIALIA = "com.slgunz.root.sialia.data.service.SIALIA";

    public static final String EXTRA_NOTIFICATION = "extra_notification";

    @Inject
    ApplicationDataManager mDataManager;

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    @Inject
    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int tweetsCount = 0;

        try {
            tweetsCount = mDataManager.checkNewTweets(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (tweetsCount == 0) return;

        Resources res = getResources();
        String subject = res.getQuantityString(R.plurals.tweets, tweetsCount);
        String noteText = res.getString(R.string.note_content, tweetsCount, subject);

        Intent activityIntent = new Intent(this, LoginActivity.class);
        PendingIntent pIntent = PendingIntent.getService(this, 0, activityIntent, 0);

        Notification notification = new NotificationCompat
                .Builder(this, CHANNEL_SIALIA)
                .setTicker(res.getString(R.string.notification_tickle_main))
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(res.getString(R.string.notification_title_main))
                .setContentText(noteText)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        Intent noteIntent = new Intent(ACTION_SHOW_NOTIFICATION);
        noteIntent.putExtra(EXTRA_NOTIFICATION, notification);
        sendOrderedBroadcast(noteIntent, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }

    public static void startServiceAlarm(Context context, boolean isOn) {
        Intent intent = PollService.newIntent(context);
        PendingIntent pIntent = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if(alarmManager == null){
            Log.d(TAG, "Alarm Manager is null");
            return;
        }

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    TIME_INTERVAL, pIntent);
        } else {
            alarmManager.cancel(pIntent);
            pIntent.cancel();
        }
        Log.d(TAG, "startServiceAlarm: " + isOn);
        PreferenceHelper.setIsAlarmOn(context, isOn);
    }

    public static boolean isAlarmOn(Context context) {
        Intent intent = PollService.newIntent(context);
        // if created with FLAG_NO_CREATE return null, then AlarmManager is offline
        PendingIntent pIntent = PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_NO_CREATE);
        return pIntent != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context context) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(
                PollService.CHANNEL_SIALIA,
                NotificationChannel.DEFAULT_CHANNEL_ID,
                importance
        );
        channel.setDescription(context.getString(R.string.notificaton_channel_description));
        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.createNotificationChannel(channel);
        }
    }
}
