package com.slgunz.root.sialia.data.service;

import com.slgunz.root.sialia.di.ServiceScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PollServiceModule {
    @ServiceScoped
    @ContributesAndroidInjector
    abstract PollService pollService();
}
