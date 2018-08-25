package com.slgunz.root.sialia.ui.addtweet;

import com.slgunz.root.sialia.di.AndroidComponent;
import com.slgunz.root.sialia.di.scopes.ActivityScoped;
import com.slgunz.root.sialia.ui.login.LoginModule;

import dagger.Subcomponent;

@ActivityScoped
@Subcomponent(modules = AddTweetModule.class)
public interface AddTweetSubcomponent extends AndroidComponent<AddTweetActivity> {
    @Subcomponent.Builder
    interface Builder extends AndroidComponent.Builder<AddTweetActivity> {}
}
