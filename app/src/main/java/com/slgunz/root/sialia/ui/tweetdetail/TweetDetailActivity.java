package com.slgunz.root.sialia.ui.tweetdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.util.ActivityUtils;

import dagger.android.support.DaggerAppCompatActivity;

public class TweetDetailActivity extends DaggerAppCompatActivity {

    private static final String EXTRA_TWEET_ID = "com.slgunz.root.sialia.ui.tweetdetail.tweet_id";
    private static final String EXTRA_USER_ID = "com.slgunz.root.sialia.ui.tweetdetail.user_id";
    private static final long DEFAULT_TWEET_ID = -1L;
    private static final long DEFAULT_USER_ID = -1L;

    public static Intent newIntent(Context context, Long tweetId, Long userId) {
        Intent intent = new Intent(context, TweetDetailActivity.class);
        intent.putExtra(EXTRA_TWEET_ID, tweetId);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweetdetail_act);

        Long tweetId = getIntent().getLongExtra(EXTRA_TWEET_ID, DEFAULT_TWEET_ID);
        Long userId = getIntent().getLongExtra(EXTRA_USER_ID, DEFAULT_USER_ID);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TweetDetailFragment tweetDetailFragment =
                (TweetDetailFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);
        if (tweetDetailFragment == null) {
            tweetDetailFragment = TweetDetailFragment.newInstance(tweetId, userId);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tweetDetailFragment, R.id.contentFrame
            );
        }
    }
}
