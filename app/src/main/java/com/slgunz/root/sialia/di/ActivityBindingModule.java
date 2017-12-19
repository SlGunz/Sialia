package com.slgunz.root.sialia.di;

import com.slgunz.root.sialia.ui.home.HomeActivity;
import com.slgunz.root.sialia.ui.home.HomeModule;
import com.slgunz.root.sialia.ui.login.LoginActivity;
import com.slgunz.root.sialia.ui.login.LoginModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We created this module. This is a given module to dagger.
 * We map all our activities here.
 * And Dagger know our activities in compile time.
 * In our app we have Main and Detail activity.
 * So we map both activities here.
 */

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeActivity homeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();
}
