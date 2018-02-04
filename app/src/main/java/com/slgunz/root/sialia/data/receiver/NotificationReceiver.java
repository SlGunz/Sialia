package com.slgunz.root.sialia.data.receiver;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.slgunz.root.sialia.data.service.PollService;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (getResultCode() != Activity.RESULT_OK) {
            return;
        }
        Notification notification = intent.getParcelableExtra(PollService.EXTRA_NOTIFICATION);
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(0, notification);
    }
}
