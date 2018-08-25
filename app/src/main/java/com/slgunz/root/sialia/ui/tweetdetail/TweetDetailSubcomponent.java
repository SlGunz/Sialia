package com.slgunz.root.sialia.ui.tweetdetail;

import com.slgunz.root.sialia.di.AndroidComponent;
import com.slgunz.root.sialia.di.scopes.ActivityScoped;

import dagger.Subcomponent;

@ActivityScoped
@Subcomponent(modules = TweetDetailModule.class)
public interface TweetDetailSubcomponent extends AndroidComponent<TweetDetailActivity> {
    @Subcomponent.Builder
    interface Builder extends AndroidComponent.Builder<TweetDetailActivity> {
    }
}
