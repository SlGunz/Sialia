package com.slgunz.root.sialia.ui.tweetdetail;

import com.slgunz.root.sialia.di.ActivityScoped;
import com.slgunz.root.sialia.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TweetDetailModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract TweetDetailFragment tweetDetailFragment();
    @ActivityScoped
    @Binds
    abstract TweetDetailContract.Presenter tweetDetailPresenter(TweetDetailPresenter presenter);
}
