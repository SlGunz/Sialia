package com.slgunz.root.sialia.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.model.User;
import com.slgunz.root.sialia.data.source.local.SialiaLocalDataSource;
import com.slgunz.root.sialia.data.source.remote.AuthorizationRemoteDataSource;
import com.slgunz.root.sialia.data.source.remote.TwitterService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;


@Singleton
public class ApplicationDataManager {

    private static final int TWEET_COUNT = 30;
    private static final int TWEET_INCREMENT = 15;
    private final AuthorizationRemoteDataSource mAuthorizationDataManager;
    private final SialiaLocalDataSource mSialiaLocalDataSource;
    private final TwitterService mTwitterService;

    private User mUser;

    @Inject
    ApplicationDataManager(AuthorizationRemoteDataSource authorizationRemoteDataSource,
                           SialiaLocalDataSource sialiaLocalDataSource,
                           TwitterService twitterService) {
        mAuthorizationDataManager = checkNotNull(authorizationRemoteDataSource);
        mSialiaLocalDataSource = checkNotNull(sialiaLocalDataSource);
        mTwitterService = checkNotNull(twitterService);
    }

    public Single<User> verifyCredentials() {
        return mTwitterService.verifyCredentials();
    }

    public User getAccountProfile() {
        return mUser;
    }

    public void saveReceivedOAuthData(Context context) {
        String userKey = mAuthorizationDataManager.getToken();
        String userSecret = mAuthorizationDataManager.getTokenSecret();

        mSialiaLocalDataSource.setOAuthUserKey(context, userKey);
        mSialiaLocalDataSource.setOAuthUserSecret(context, userSecret);
    }

    /**
     * Set saved token to tokenSecret for OkHttp interceptor
     * @param context
     */
    public void setTokenAndSecret(Context context) {
        String token = checkNotNull(mSialiaLocalDataSource.getOAuthUserKey(context));
        String tokenSecret = checkNotNull(mSialiaLocalDataSource.getOAuthUserSecret(context));

        mAuthorizationDataManager.setTokenAndSecret(token, tokenSecret);
    }

    public boolean hasTokenAndSecret(Context context) {
        return mSialiaLocalDataSource.hasUserKey(context) &&
                mSialiaLocalDataSource.hasUserSecret(context);
    }

    public Single<String> openAuthenticatePage(String callbackUrl) {
        return mAuthorizationDataManager.retrieveRequestToken(callbackUrl);
    }

    public Completable retrieveAccessToken(Intent intent) {
        String oauthVerifier = mAuthorizationDataManager.getOAuthVerifier(intent.getData());
        return mAuthorizationDataManager.retrieveAccessToken(oauthVerifier);

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
     * @param biggestId is a value with an ID greater than (that is, more recent than) the specified ID.
     *                  There are limits to the number of Tweets which can be accessed through the API.
     *                  If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
     */
    public Single<List<Tweet>> loadRecentHomeTimelineTweets(Long biggestId) {
        return loadHomeTimelineTweets(TWEET_COUNT, biggestId + 1, null);
    }

    /**
     * @param lowestId is a value of an ID less than (that is, older than) or equal to the specified ID.
     */
    public Single<List<Tweet>> loadPreviousHomeTimelineTweets(Long lowestId) {
        return loadHomeTimelineTweets(TWEET_INCREMENT, null, lowestId - 1);
    }

    public Single<Tweet> sendTweet(String message) {
        return mTwitterService.postStatusUpdate(message);
    }
}
