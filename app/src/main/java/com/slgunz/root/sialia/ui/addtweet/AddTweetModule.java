package com.slgunz.root.sialia.ui.addtweet;

import com.slgunz.root.sialia.di.ActivityScoped;
import com.slgunz.root.sialia.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AddTweetModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract AddTweetFragment addTweetFragment();

    @ActivityScoped
    @Binds
    abstract AddTweetContract.Presenter addTweetPresenter(AddTweetPresenter presenter);
}
