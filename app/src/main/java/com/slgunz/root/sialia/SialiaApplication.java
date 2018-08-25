package com.slgunz.root.sialia;

import android.app.Activity;
import android.app.Application;
import android.os.Build;

import com.slgunz.root.sialia.data.service.PollService;
import com.slgunz.root.sialia.di.AndroidComponent;
import com.slgunz.root.sialia.di.DaggerAppComponent;

import java.util.Map;

import javax.inject.Inject;


public class SialiaApplication extends Application {

    @Inject
    Map<Class<? extends Activity>, AndroidComponent.Builder<? extends Activity>> injectedComponents;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.create().inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PollService.createNotificationChannel(getApplicationContext());
        }
    }

    public AndroidComponent.Builder<? extends Activity> componentBuilder(Class<? extends Activity> aClass) {
        return injectedComponents.get(aClass);
    }
}
