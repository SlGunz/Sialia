package com.slgunz.root.sialia.util.schedulers;

/**
 * Created by root on 30.11.2017.
 */

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SchedulerProviderModule {
    @Singleton
    @Binds
    abstract BaseSchedulerProvider provideScheduler(SchedulerProvider schedulerProvider);
}
