package com.slgunz.root.sialia.ui.tweetdetail;

import com.slgunz.root.sialia.di.scopes.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class TweetDetailModule {
    @ActivityScoped
    @Binds
    abstract TweetDetailContract.Presenter tweetDetailPresenter(TweetDetailPresenter presenter);
}
