package com.slgunz.root.sialia.ui.login;

import com.slgunz.root.sialia.di.AndroidComponent;
import com.slgunz.root.sialia.di.scopes.ActivityScoped;

import dagger.Subcomponent;

@ActivityScoped
@Subcomponent(modules = LoginModule.class)
public interface LoginSubcomponent extends AndroidComponent<LoginActivity>{
    @Subcomponent.Builder
    interface Builder extends AndroidComponent.Builder<LoginActivity>{}
}
