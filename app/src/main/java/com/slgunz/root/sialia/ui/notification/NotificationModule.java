package com.slgunz.root.sialia.ui.notification;


import com.slgunz.root.sialia.di.scopes.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class NotificationModule {
    @ActivityScoped
    @Binds
    abstract NotificationContract.Presenter notificationPresenter(NotificationPresenter presenter);
}
