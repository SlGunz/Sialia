package com.slgunz.root.sialia.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.service.PollService;
import com.slgunz.root.sialia.data.source.local.PreferenceHelper;

public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isOn = PreferenceHelper.isAlarmOn(context);
        PollService.startServiceAlarm(context, isOn);
    }
}
