package com.slgunz.root.sialia.ui.home;

import com.slgunz.root.sialia.di.AndroidComponent;
import com.slgunz.root.sialia.di.scopes.ActivityScoped;

import dagger.Subcomponent;

@ActivityScoped
@Subcomponent(modules = HomeModule.class)
public interface HomeSubcomponent extends AndroidComponent<HomeActivity> {
    @Subcomponent.Builder
    interface Builder extends AndroidComponent.Builder<HomeActivity>{}
}
