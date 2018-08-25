package com.slgunz.root.sialia.ui.notification;

import com.slgunz.root.sialia.di.AndroidComponent;
import com.slgunz.root.sialia.di.scopes.ActivityScoped;

import dagger.Subcomponent;

@ActivityScoped
@Subcomponent(modules = NotificationModule.class)
public interface NotificationSubcomponent extends AndroidComponent<NotificationActivity> {
    @Subcomponent.Builder
    interface Builder extends AndroidComponent.Builder<NotificationActivity> {
    }
}
