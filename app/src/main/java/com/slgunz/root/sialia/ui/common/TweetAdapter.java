package com.slgunz.root.sialia.ui.common;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Iterables;
import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.model.subtype.Media;
import com.slgunz.root.sialia.data.source.remote.GlideApp;
import com.slgunz.root.sialia.data.source.remote.GlideRequest;
import com.slgunz.root.sialia.data.source.remote.GlideRequests;

import java.util.List;


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetHolder> {

    public interface Callback {
        void selectTweetHolder(Tweet tweet);
    }

    private Callback mCallback;

    private List<Tweet> mTweets;

    private GlideRequest<Drawable> mFullRequest;

    public TweetAdapter(List<Tweet> tweets) {
        replaceData(tweets);
    }

    @Override
    public TweetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.tweet_layout, parent, false);

        GlideRequests glideRequests = GlideApp.with(parent);
        mFullRequest = glideRequests.asDrawable();

        return new TweetHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TweetHolder holder, int position) {
        Tweet tweet = mTweets.get(position);

        holder.mMediaImageView.setImageDrawable(null);
        holder.mAccountProfileImageView.setImageDrawable(null);

        mFullRequest
                .load(tweet.getUser().getProfileImageUrl())
                .into(holder.mAccountProfileImageView);

        List<Media> mediaList = tweet.getEntities().getMedia();
        if (mediaList != null) {
            Media media = Iterables.getFirst((mediaList), null);
            mFullRequest
                    .load(media.getMediaUrl())
                    .into(holder.mMediaImageView);
            holder.mMediaImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mMediaImageView.setVisibility(View.GONE);
        }

        holder.bindTweet(tweet, mCallback);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public Long getTheBiggestId() {
        Tweet tweet = Iterables.getFirst(mTweets, null);
        return tweet == null ? null : tweet.getId();
    }

    public Long getTheLowestId() {
        Tweet tweet = Iterables.getLast(mTweets, null);
        return tweet == null ? null : tweet.getId();
    }

    public void appendData(List<Tweet> tweets) {
        if(tweets == null){
            return;
        }
        int start = mTweets.size();
        mTweets.addAll(start, tweets);
        notifyItemRangeInserted(start, tweets.size());
    }

    public void replaceData(List<Tweet> tweets) {
        if(tweets == null){
            return;
        }
        this.mTweets = (tweets);
        notifyDataSetChanged();
    }

    ///////////////////////////////////////////////////////////////////////////
    // RecyclerView.ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    public static class TweetHolder extends RecyclerView.ViewHolder {

        ImageView mAccountProfileImageView;
        ImageView mMediaImageView;
        TextView mAuthorTextView;
        TextView mAuthorScreenNameTextView;
        TextView mMessageTextView;

        private Tweet mTweet;

        public TweetHolder(View itemView) {
            super(itemView);

            mAccountProfileImageView = itemView.findViewById(R.id.account_profile_image_view);
            mMediaImageView = itemView.findViewById(R.id.media_image_view);
            mAuthorTextView = itemView.findViewById(R.id.tweet_author_name_text_view);
            mAuthorScreenNameTextView = itemView.findViewById(R.id.tweet_author_screen_name_text_view);
            mMessageTextView = itemView.findViewById(R.id.tweet_message_text_view);
        }

        private String getTweetMessage() {
            return mTweet.getText();
        }

        private String getTweetAuthor() {
            return mTweet.getUser().getName();
        }

        private String getTweeterScreenName() {
            return "@" + mTweet.getUser().getScreenName();
        }

        public void bindTweet(Tweet tweet, Callback callback) {
            mTweet = tweet;

            itemView.setOnClickListener(
                    view -> {
                        if (callback != null) {
                            callback.selectTweetHolder(mTweet);
                        }
                    }
            );

            mAuthorTextView.setText(getTweetAuthor());
            mAuthorScreenNameTextView.setText(getTweeterScreenName());
            mMessageTextView.setText(getTweetMessage());
        }
    }
}
