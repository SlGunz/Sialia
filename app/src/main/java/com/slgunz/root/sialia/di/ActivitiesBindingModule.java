package com.slgunz.root.sialia.di;

import android.app.Activity;

import com.slgunz.root.sialia.di.scopes.ActivityKey;
import com.slgunz.root.sialia.ui.addtweet.AddTweetActivity;
import com.slgunz.root.sialia.ui.addtweet.AddTweetSubcomponent;
import com.slgunz.root.sialia.ui.home.HomeActivity;
import com.slgunz.root.sialia.ui.home.HomeSubcomponent;
import com.slgunz.root.sialia.ui.login.LoginActivity;
import com.slgunz.root.sialia.ui.login.LoginSubcomponent;
import com.slgunz.root.sialia.ui.notification.NotificationActivity;
import com.slgunz.root.sialia.ui.notification.NotificationSubcomponent;
import com.slgunz.root.sialia.ui.tweetdetail.TweetDetailActivity;
import com.slgunz.root.sialia.ui.tweetdetail.TweetDetailSubcomponent;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module(subcomponents = {
        HomeSubcomponent.class,
        LoginSubcomponent.class,
        AddTweetSubcomponent.class,
        TweetDetailSubcomponent.class,
        NotificationSubcomponent.class
})
public abstract class ActivitiesBindingModule {
    @Binds
    @IntoMap
    @ActivityKey(HomeActivity.class)
    abstract AndroidComponent.Builder<? extends Activity>  bindHomeActivity(HomeSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(LoginActivity.class)
    abstract AndroidComponent.Builder<? extends Activity>  bindLoginActivity(LoginSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(AddTweetActivity.class)
    abstract AndroidComponent.Builder<? extends Activity>  bindAddTweetActivity(AddTweetSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(TweetDetailActivity.class)
    abstract AndroidComponent.Builder<? extends Activity>  bindTweetDetailActivity(TweetDetailSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(NotificationActivity.class)
    abstract AndroidComponent.Builder<? extends Activity>  bindNotificationActivity(NotificationSubcomponent.Builder builder);
}
