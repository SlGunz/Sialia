package com.slgunz.root.sialia.ui.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.model.User;
import com.slgunz.root.sialia.data.service.PollService;

import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {

    private static final String TAG = "BaseFragment";

    protected ImageView background;
    protected ImageView accountProfileImage;

    protected TextView accountName;
    protected TextView accountScreenName;
    protected TextView statTweets;
    protected TextView statFollowing;
    protected TextView statFollower;

    private boolean mIsStartPage;

    protected abstract BasePresenter getPresenter();

    protected void setFavorite(Tweet tweet) {
        boolean isFavorite = !tweet.getFavorited();
        // update remote value;
        getPresenter().setTweetFavorite(this, isFavorite, tweet.getId());
        // update local value;
        tweet.setFavorited(isFavorite);
    }

    protected void setRetweeted(Tweet tweet) {
        boolean isRetweeted = !tweet.getRetweeted();
        // update remote value;
        getPresenter().setTweetRetweeted(this, isRetweeted, tweet.getId());
        // update local value;
        tweet.setRetweeted(isRetweeted);
    }

    protected void selectItem(Tweet tweet){
        getPresenter().selectItem(tweet);
    }

    @CallSuper
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (isStartPage()) {
            NavigationView navigationView = getActivity().findViewById(R.id.nav_view);

            android.view.View hView = navigationView.getHeaderView(0);

            background = hView.findViewById(R.id.nv_background);
            accountProfileImage = hView.findViewById(R.id.nav_header_profile_image);
            accountName = hView.findViewById(R.id.nav_header_account_name);
            accountScreenName = hView.findViewById(R.id.nav_header_account_screen_name);

            statTweets = hView.findViewById(R.id.tweets_counter);
            statFollowing = hView.findViewById(R.id.following_counter);
            statFollower = hView.findViewById(R.id.follower_counter);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // TODO substitute to eventbus
    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mCancelReceiver, intentFilter, PollService.PERM_PRIVATE, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mCancelReceiver);
    }

    private BroadcastReceiver mCancelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    ///////////////////////////////////////////////////////////////////////////
    // divider
    ///////////////////////////////////////////////////////////////////////////

    protected void setAsStartPage(Boolean isStartPage) {
        mIsStartPage = isStartPage;
    }

    public boolean isStartPage() {
        return mIsStartPage;
    }

    public void onApplyAccountProfile(User user) {
        if (!isStartPage()) return;

        Glide.with(getActivity())
                .load(user.getProfileBannerUrl())
                .into(background);

        Glide.with(getActivity())
                .load(user.getProfileImageUrl())
                .into(accountProfileImage);

        accountName.setText(user.getName());
        accountScreenName.setText(user.getScreenName());

        statTweets.setText(toString(user.getStatusesCount()));
        statFollowing.setText(toString(user.getFriendsCount()));
        statFollower.setText(toString(user.getFollowersCount()));
    }

    private String toString(Integer integer){
        return Integer.toString(integer);
    }
}
