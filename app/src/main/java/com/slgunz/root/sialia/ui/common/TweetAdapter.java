package com.slgunz.root.sialia.ui.common;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.google.common.collect.Iterables;
import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.model.subtype.Media;
import com.slgunz.root.sialia.util.ActivityUtils;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetHolder> implements ListPreloader.PreloadModelProvider<Tweet> {

    private List<Tweet> mTweets;

    private GlideRequest<Drawable> mFullRequest;

    private Consumer mOnItemClick;
    private TweetReaction mTweetReaction;

    public interface Consumer {
        void accept(Tweet tweet);
    }

    class TweetReaction {
        Consumer onFavoriteClick;
        Consumer onRetweetClick;
        Consumer onReplyClick;
    }

    public TweetAdapter(List<Tweet> tweets) {
        replaceData(tweets);
        mTweetReaction = new TweetReaction();
    }

    ///////////////////////////////////////////////////////////////////////////
    // RecyclerView.Adapter methods
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public TweetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.tweet_layout, parent, false);

        GlideRequests glideRequests = GlideApp.with(parent);
        mFullRequest = glideRequests.asDrawable().centerCrop();

        return new TweetHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TweetHolder holder, int position) {
        Tweet tweet = mTweets.get(position);

        holder.mImageViewMedia.setImageDrawable(null);
        holder.mImageViewAccountProfile.setImageDrawable(null);

        mFullRequest
                .load(tweet.getUser().getProfileImageUrl())
                .into(holder.mImageViewAccountProfile);

        String mediaUrl = getMediaUrl(tweet);
        holder.mImageViewMedia.setVisibility(mediaUrl == null ? View.GONE : View.VISIBLE);
        mFullRequest.load(mediaUrl).into(holder.mImageViewMedia);

        holder.bindTweet(tweet, mOnItemClick, mTweetReaction);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    ///////////////////////////////////////////////////////////////////////////
    // various interaction methods
    ///////////////////////////////////////////////////////////////////////////
    public void setOnClickListener(Consumer onItemClick) {
        mOnItemClick = onItemClick;
    }

    public void addTweetReactionListener(Consumer onFavoriteClick,
                                         Consumer onRetweetClick,
                                         Consumer onReplyClick) {
        mTweetReaction.onFavoriteClick = onFavoriteClick;
        mTweetReaction.onReplyClick = onReplyClick;
        mTweetReaction.onRetweetClick = onRetweetClick;
    }

    private String getMediaUrl(Tweet tweet) {
        List<Media> mediaList = tweet.getEntities().getMedia();
        if (mediaList != null) {
            Media media = Iterables.getFirst((mediaList), null);
            return media == null ? null : media.getMediaUrl();
        }
        return null;
    }

    public Long getMaxId() {
        Tweet tweet = Iterables.getFirst(mTweets, null);
        return tweet == null ? null : tweet.getId();
    }

    public Long getMinId() {
        Tweet tweet = Iterables.getLast(mTweets, null);
        return tweet == null ? null : tweet.getId();
    }

    public void appendData(List<Tweet> tweets) {
        if (tweets == null) {
            return;
        }
        int start = mTweets.size();
        mTweets.addAll(start, tweets);
        notifyItemRangeInserted(start, tweets.size());
    }

    public void replaceData(List<Tweet> tweets) {
        if (tweets == null) {
            return;
        }
        this.mTweets = (tweets);
        notifyDataSetChanged();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Glide preload methods
    ///////////////////////////////////////////////////////////////////////////
    @NonNull
    @Override
    public List<Tweet> getPreloadItems(int position) {
        return mTweets.subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull Tweet item) {
        return mFullRequest.load(getMediaUrl(item));
    }

    ///////////////////////////////////////////////////////////////////////////
    // RecyclerView.ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    public static class TweetHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageViewAccountProfile;
        ImageView mImageViewMedia;

        TextView mTextViewAuthor;
        TextView mTextViewAuthorScreenName;
        TextView mTextViewMessage;

        Button mButtonReply;
        Button mButtonRetweet;
        Button mButtonFavorite;

        private Drawable mDrawableFavoriteNormal;
        private Drawable mDrawableFavoriteSelected;

        private Drawable mDrawableRetweetNormal;
        private Drawable mDrawableRetweetSelected;

        private int mColorNormal;
        private int mColorBlue;

        private Consumer mItemCallback;
        private TweetReaction mTweetReaction;

        private Tweet mTweet;

        public TweetHolder(View itemView) {
            super(itemView);
            mImageViewAccountProfile = itemView.findViewById(R.id.account_profile_image_view);
            mImageViewMedia = itemView.findViewById(R.id.media_image_view);

            mTextViewAuthor = itemView.findViewById(R.id.tweet_author_name_text_view);
            mTextViewAuthorScreenName = itemView.findViewById(R.id.tweet_author_screen_name_text_view);
            mTextViewMessage = itemView.findViewById(R.id.tweet_message_text_view);

            mButtonFavorite = itemView.findViewById(R.id.btn_favorite);
            mButtonReply = itemView.findViewById(R.id.btn_reply);
            mButtonRetweet = itemView.findViewById(R.id.btn_retweet);

            mDrawableFavoriteNormal = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_favorite);
            mDrawableFavoriteSelected = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_favorite_colored);
            mDrawableRetweetNormal = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_retweet);
            mDrawableRetweetSelected = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_retweet_colored);

            mColorBlue = ContextCompat.getColor(itemView.getContext(), R.color.custom_blue);
            mColorNormal = ContextCompat.getColor(itemView.getContext(), R.color.custom_gray);

            itemView.setOnClickListener(this);
            mButtonFavorite.setOnClickListener(this);
            mButtonRetweet.setOnClickListener(this);
            mButtonReply.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_reply:
                    onReplyClick();
                    break;
                case R.id.btn_retweet:
                    onRetweetClick();
                    break;
                case R.id.btn_favorite:
                    onFavoriteClick();
                    break;
                default:
                    onItemClick();
            }
        }

        public void bindTweet(Tweet tweet, Consumer onItemClick, TweetReaction tr) {
            mTweet = tweet;

            mItemCallback = onItemClick;
            mTweetReaction = tr;

            mTextViewAuthor.setText(getAuthor());
            mTextViewAuthorScreenName.setText(getScreenName());
            mTextViewMessage.setText(getMessage());

            mButtonRetweet.setText(getRetweetCount());
            mButtonReply.setText(getReplyCount());
            mButtonFavorite.setText(getFavoriteCount());

            if (mTweet.getRetweeted()) {
                ActivityUtils.changeButtonDrawable(mButtonRetweet, mDrawableFavoriteSelected);
            }
            if (mTweet.getFavorited()) {
                ActivityUtils.changeButtonDrawable(mButtonFavorite, mDrawableFavoriteSelected);
            }
        }

        private String getMessage() {
            return mTweet.getText();
        }

        private String getAuthor() {
            return mTweet.getUser().getName();
        }

        private String getScreenName() {
            return "@" + mTweet.getUser().getScreenName();
        }

        private String getFavoriteCount() {
            return mTweet.getFavoriteCount().toString();
        }

        private String getRetweetCount() {
            return mTweet.getRetweetCount().toString();
        }

        private String getReplyCount() {
            return mTweet.getReplyCount().toString();
        }

        private void onReplyClick() {
            if (mTweetReaction == null) return;
            mTweetReaction.onReplyClick.accept(mTweet);
        }

        private void onRetweetClick() {
            if (mTweetReaction == null) return;
            mTweetReaction.onRetweetClick.accept(mTweet);
            boolean isRetweeted = mTweet.getRetweeted();
            ActivityUtils.changeButtonDrawable(mButtonRetweet,
                    isRetweeted ? mDrawableRetweetSelected : mDrawableRetweetNormal);
            mButtonRetweet.setTextColor(isRetweeted ? mColorBlue : mColorNormal);
        }

        private void onFavoriteClick() {
            if (mTweetReaction == null) return;
            mTweetReaction.onFavoriteClick.accept(mTweet);
            boolean isFavorite = mTweet.getFavorited();
            ActivityUtils.changeButtonDrawable(mButtonFavorite,
                    isFavorite ? mDrawableFavoriteSelected : mDrawableFavoriteNormal);
            mButtonRetweet.setTextColor(isFavorite ? mColorBlue : mColorNormal);
        }

        private void onItemClick() {
            if (mItemCallback != null) {
                mItemCallback.accept(mTweet);
            }
        }
    }
}
