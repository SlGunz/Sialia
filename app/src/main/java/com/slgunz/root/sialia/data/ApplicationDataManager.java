package com.slgunz.root.sialia.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.slgunz.root.sialia.data.model.Banners;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.model.User;
import com.slgunz.root.sialia.data.source.local.PreferenceHelper;
import com.slgunz.root.sialia.data.source.remote.TwitterService;
import com.slgunz.root.sialia.util.oauth.AuthorizationHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;


@Singleton
public class ApplicationDataManager {

    private static final int TWEET_COUNT = 30;
    private static final int TWEET_INCREMENT = 15;
    private static final long DEFAULT_TWEET_ID = -1;
    private final AuthorizationHelper mAuthorizationHelper;
    private final TwitterService mTwitterService;

    private User mUser;
    private Tweet mSelectedItem;

    @Inject
    ApplicationDataManager(AuthorizationHelper authorizationHelper,
                           TwitterService twitterService) {
        mAuthorizationHelper = checkNotNull(authorizationHelper);
        mTwitterService = checkNotNull(twitterService);
    }

    public Single<User> verifyCredentials() {
        return mTwitterService.verifyCredentials();
    }

    public User getAccountProfile() {
        return mUser;
    }

    public void setSelectedItem(Tweet tweet) {
        mSelectedItem = tweet;
    }

    public Tweet getSelectedItem() {
        return mSelectedItem;
    }

    public void saveReceivedOAuthData(Context context) {
        String userKey = mAuthorizationHelper.getToken();
        String userSecret = mAuthorizationHelper.getTokenSecret();

        PreferenceHelper.setOAuthUserKey(context, userKey);
        PreferenceHelper.setOAuthUserSecret(context, userSecret);
    }

    /**
     * Set saved token and tokenSecret for OkHttp interceptor
     *
     * @param context
     */
    public void setTokenAndSecret(Context context) {
        String token = checkNotNull(PreferenceHelper.getOAuthUserKey(context));
        String tokenSecret = checkNotNull(PreferenceHelper.getOAuthUserSecret(context));

        mAuthorizationHelper.setTokenAndSecret(token, tokenSecret);
    }

    public boolean hasTokenAndSecret(Context context) {
        return PreferenceHelper.hasUserKey(context) &&
                PreferenceHelper.hasUserSecret(context);
    }

    public Single<String> openAuthenticatePage(String callbackUrl) {
        return mAuthorizationHelper.retrieveRequestToken(callbackUrl);
    }

    public Completable retrieveAccessToken(Intent intent) {
        String oauthVerifier = mAuthorizationHelper.getOAuthVerifier(intent.getData());
        return mAuthorizationHelper.retrieveAccessToken(oauthVerifier);

    }

    public boolean isNotCorrectVerifier(String callback_url, Intent intent) {
        Uri uri = intent.getData();
        return uri == null || !uri.toString().startsWith(callback_url);
    }

    public void setAccountProfile(User user) {
        this.mUser = user;
    }

    private Single<List<Tweet>> loadHomeTimelineTweets(Integer count, Long sinceId, Long maxId) {
        return mTwitterService.getStatusesHomeTimeline(count, sinceId, maxId);
    }

    public Single<List<Tweet>> loadHomeTimeLineTweets() {
        return loadHomeTimelineTweets(TWEET_COUNT, null, null);
    }

    /**
     * @param maxId is a value with an ID greater than (that is, more recent than) the specified ID.
     *                  There are limits to the number of Tweets which can be accessed through the API.
     *                  If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
     */
    public Single<List<Tweet>> loadRecentHomeTimelineTweets(Long maxId) {
        return loadHomeTimelineTweets(TWEET_COUNT, maxId + 1, null);
    }

    /**
     * @param minId is a value of an ID less than (that is, older than) or equal to the specified ID.
     */
    public Single<List<Tweet>> loadPreviousHomeTimelineTweets(Long minId) {
        return loadHomeTimelineTweets(TWEET_INCREMENT, null, minId - 1);
    }

    /**
     * tweet and retweet messages
     * @param message - message or @username (when retweet)
     * @param retweetId
     * @param mediaIds - media_id (use uploadImage() to attach picture)
     * @return
     */
    public Single<Tweet> sendTweet(String message, Long retweetId, String mediaIds) {
        return mTwitterService.postStatusUpdate(message, retweetId, mediaIds);
    }

    public Single<List<Tweet>> loadRetweetedTweets(Long id) {
        return mTwitterService.getStatusesRetweet(id, TWEET_COUNT);
    }

    public Single<Banners> loadUserProfileBanner(Long userId) {
        return mTwitterService.getUserProfileBanner(userId);
    }

    public Single<List<Tweet>> loadMentionsTweets() {
        return mTwitterService.getStatusesMentionsTimeline(TWEET_COUNT, null, null);
    }

    public int checkNewTweets(Context context) throws IOException {
        Long lastId = PreferenceHelper.getLastLoadedTweetId(context, DEFAULT_TWEET_ID);
        if (lastId == DEFAULT_TWEET_ID) {
            return 0;
        }

        Response<List<Tweet>> response = mTwitterService
                // increment since_id according to Twitter API
                .getStatusesHomeTimelineRecent(TWEET_COUNT, lastId + 1).execute();
        if (response.isSuccessful() && response.body() != null) {
            int counter = 0;
            for (Tweet tweet : response.body()) {
                if (tweet.getId() >= lastId) {
                    counter++;
                }
            }
            return counter;
        }
        return 0;
    }

    public void setLastLoadedTweetId(Context context, Long biggestId) {
        PreferenceHelper.setLastLoadedTweetId(context, biggestId);
    }

    public Single<Long> uploadImage(File file, String name) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part media =
                MultipartBody.Part.createFormData(name, file.getName(), requestFile);

        return mTwitterService.postMediaUpload(media);
    }

    public Single<Tweet> setFavorite(boolean isFavorite, Long tweetId) {
        if (isFavorite) {
            return mTwitterService.postFavoriteCreate(tweetId);
        }
        return mTwitterService.postFavoriteDestroy(tweetId);
    }

    public Single<Tweet> setRetweeted(boolean isRetweeted, Long tweetId) {
        if(isRetweeted){
            return mTwitterService.postStatusRetweet(tweetId);
        }
        return mTwitterService.postStatusUnretweet(tweetId);
    }
}
