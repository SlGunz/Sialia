package com.slgunz.root.sialia.ui.notification;


import com.slgunz.root.sialia.di.ActivityScoped;
import com.slgunz.root.sialia.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class NotificationModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract NotificationFragment notificationFragment();

    @ActivityScoped
    @Binds
    abstract NotificationContract.Presenter notificationPresenter(NotificationPresenter presenter);
}
