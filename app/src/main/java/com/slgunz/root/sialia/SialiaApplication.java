package com.slgunz.root.sialia;

import android.os.Build;

import com.slgunz.root.sialia.data.service.PollService;
import com.slgunz.root.sialia.di.AppComponent;
import com.slgunz.root.sialia.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;


public class SialiaApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build();
        appComponent.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PollService.createNotificationChannel(getApplicationContext());
        }

        return appComponent;
    }
}
