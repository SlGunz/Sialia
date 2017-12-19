package com.slgunz.root.sialia.ui.login;

import com.slgunz.root.sialia.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LoginModule {
    @ActivityScoped
    @Binds
    abstract LoginContract.Presenter loginPresenter(LoginPresenter presenter);
}
