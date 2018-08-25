package com.slgunz.root.sialia.di;


import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;
import com.slgunz.root.sialia.util.schedulers.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SchedulerProviderModule {

    @Binds
    abstract BaseSchedulerProvider provideScheduler(SchedulerProvider schedulerProvider);
}
