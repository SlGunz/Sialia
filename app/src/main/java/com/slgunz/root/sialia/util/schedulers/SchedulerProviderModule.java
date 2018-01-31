package com.slgunz.root.sialia.util.schedulers;


import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SchedulerProviderModule {
    @Singleton
    @Binds
    abstract BaseSchedulerProvider provideScheduler(SchedulerProvider schedulerProvider);
}
