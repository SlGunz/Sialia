package com.slgunz.root.sialia.ui.addtweet;

import com.slgunz.root.sialia.di.scopes.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AddTweetModule {
    @ActivityScoped
    @Binds
    abstract AddTweetContract.Presenter addTweetPresenter(AddTweetPresenter presenter);
}
