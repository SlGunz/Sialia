package com.slgunz.root.sialia.ui.home;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * -------------------------------------------------
 * RecyclerView.Adapter
 * -------------------------------------------------
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.TweetHolder> {

    private List<Tweet> mHomeTimelineList;

    private GlideRequest<Drawable> fullRequest;

    public HomeAdapter(List<Tweet> homeTimelineList) {
        setList(homeTimelineList);
    }

    @Override
    public TweetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.tweet_layout, parent, false);

        GlideRequests glideRequests = GlideApp.with(parent);
        fullRequest = glideRequests
                .asDrawable();

        return new TweetHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TweetHolder holder, int position) {
        Tweet tweet = mHomeTimelineList.get(position);

        holder.mMediaImageView.setImageDrawable(null);
        holder.mAccountProfileImageView.setImageDrawable(null);

        fullRequest
                .load(tweet.getUser().getProfileImageUrl())
                .into(holder.mAccountProfileImageView);

        List<Media> mediaList = tweet.getEntities().getMedia();
        if (mediaList != null){
            Media media = Iterables.getFirst((mediaList), null);
            fullRequest
                    .load(media.getMediaUrl())
                    .into(holder.mMediaImageView);
            holder.mMediaImageView.setVisibility(View.VISIBLE);
        }else{
            holder.mMediaImageView.setVisibility(View.GONE);
        }

        holder.bindTweet(tweet);
        // TODO video
    }

    @Override
    public int getItemCount() {
        return mHomeTimelineList.size();
    }

    public Long getTheBiggestId() {
        Tweet tweet = Iterables.getFirst(mHomeTimelineList, null);
        return tweet == null ? null : tweet.getId();
    }

    public Long getTheLowestId() {
        Tweet tweet = Iterables.getLast(mHomeTimelineList, null);
        return tweet == null ? null : tweet.getId();
    }

    public void appendData(List<Tweet> tweets) {
        int start = mHomeTimelineList.size();
        mHomeTimelineList.addAll(start, tweets);
        notifyItemRangeInserted(start, tweets.size());
    }

    public void replaceData(List<Tweet> tweets) {
        setList(tweets);
    }

    private void setList(List<Tweet> tweets) {
        this.mHomeTimelineList = checkNotNull(tweets);
        notifyDataSetChanged();
    }

    /**
     * -------------------------------------------------
     *  RecyclerView.ViewHolder
     *  ------------------------------------------------
     */
    public static class TweetHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView mAccountProfileImageView;
        ImageView mMediaImageView;
        TextView mAuthorTextView;
        TextView mAuthorScreenNameTextView;
        TextView mMessageTextView;

        private Tweet mTweet;

        public TweetHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
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

        private String getTweeterScreenName(){
            return "@" + mTweet.getUser().getScreenName();
        }

        @Override
        public void onClick(View view) {
            // TODO click event
        }
        public void bindTweet(Tweet tweet) {
            this.mTweet = tweet;
            mAuthorTextView.setText(getTweetAuthor());
            mAuthorScreenNameTextView.setText(getTweeterScreenName());
            mMessageTextView.setText(getTweetMessage());
        }
    }
}
