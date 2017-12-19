package com.slgunz.root.sialia;

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
        return appComponent;
    }
}
