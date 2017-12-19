package com.slgunz.root.sialia.di;


import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    @Provides
    @Singleton
    Context provideContext(Application application){
        return application;
    }


}
