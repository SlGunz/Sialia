package com.slgunz.root.sialia.ui.tweetdetail;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Iterables;
import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.model.subtype.Media;
import com.slgunz.root.sialia.ui.common.GlideApp;
import com.slgunz.root.sialia.util.ActivityUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class TweetDetailActivity extends DaggerAppCompatActivity {

    @Inject
    Lazy<TweetDetailFragment> mDetailFragmentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweetdetail_act);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TweetDetailFragment tweetDetailFragment =
                (TweetDetailFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);
        if (tweetDetailFragment == null) {
            tweetDetailFragment = mDetailFragmentProvider.get();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tweetDetailFragment, R.id.contentFrame
            );
        }

        Tweet tweet = tweetDetailFragment.getPresenter().currentItem();

        ImageView banner = findViewById(R.id.toolbar_background);
        GlideApp.with(this)
                .load(tweet.getUser().getProfileBannerUrl())
                .into(banner);

        ImageView profile = findViewById(R.id.avatar);
        GlideApp.with(this)
                .load(tweet.getUser().getProfileImageUrl())
                .into(profile);

        TextView accountName = findViewById(R.id.account_name);
        accountName.setText(tweet.getUser().getName());

        TextView screenName = findViewById(R.id.account_screen_name);
        screenName.setText(tweet.getUser().getScreenName());

        TextView message = findViewById(R.id.tweet_message_text_view);
        message.setText(tweet.getText());

//        ImageView tweetMedia = findViewById(R.id.media_image_view);
//        if (getMediaUrl(tweet) == null) {
//            tweetMedia.setVisibility(View.GONE);
//        } else {
//            GlideApp.with(this)
//                    .load(getMediaUrl(tweet))
//                    .into(tweetMedia);
//            tweetMedia.setVisibility(View.VISIBLE);
//        }

        TextView reply = findViewById(R.id.tweets_counter);
        reply.setText(toString(tweet.getReplyCount()));
        TextView retweets = findViewById(R.id.following_counter);
        retweets.setText(toString(tweet.getRetweetCount()));
        TextView favorites = findViewById(R.id.follower_counter);
        favorites.setText(toString(tweet.getFavoriteCount()));
    }

    private String getMediaUrl(Tweet tweet) {
        List<Media> mediaList = tweet.getEntities().getMedia();
        if (mediaList != null) {
            Media media = Iterables.getFirst((mediaList), null);
            return media == null ? null : media.getMediaUrl();
        }
        return null;
    }

    private String toString(Integer integer){
      return Integer.toString(integer);
    }
}
