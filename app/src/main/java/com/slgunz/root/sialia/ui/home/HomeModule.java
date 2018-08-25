package com.slgunz.root.sialia.ui.home;

import com.slgunz.root.sialia.di.scopes.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link HomePresenter}.
 */
@Module
public abstract class HomeModule {
    @ActivityScoped
    @Binds
    abstract HomeContract.Presenter homePresenter(HomePresenter presenter);
}