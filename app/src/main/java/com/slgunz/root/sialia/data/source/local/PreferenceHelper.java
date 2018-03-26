package com.slgunz.root.sialia.data.source.local;

import android.content.Context;
import android.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;


public class PreferenceHelper {

    private static final String OAUTH_USER_KEY = "oauthUserKey";
    private static final String OAUTH_USER_SECRET = "oauthUserSecret";

    private static final String ALARM_STATUS_KEY = "alarmStatusKey";
    private static final String KEY_LAST_TWEET_ID = "lastTweetId";

    public static void setOAuthUserKey(Context context, String userKey) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(OAUTH_USER_KEY, userKey)
                .apply();
    }

    public static void setOAuthUserSecret(Context context, String userSecret) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(OAUTH_USER_SECRET, userSecret)
                .apply();
    }

    public static String getOAuthUserKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(OAUTH_USER_KEY, null);
    }

    public static void removeOAuthUserKey(Context context){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(OAUTH_USER_KEY)
                .apply();
    }

    public static String getOAuthUserSecret(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(OAUTH_USER_SECRET, null);
    }

    public static void removeOAuthUserKeySecret(Context context){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(OAUTH_USER_SECRET)
                .apply();
    }

    public static boolean hasUserKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .contains(OAUTH_USER_KEY);
    }

    public static boolean hasUserSecret(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .contains(OAUTH_USER_SECRET);
    }

    public static void setLastLoadedTweetId(Context context, Long tweetId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(KEY_LAST_TWEET_ID, tweetId)
                .apply();
    }

    public static Long getLastLoadedTweetId(Context context, long defaultId) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(KEY_LAST_TWEET_ID, defaultId);
    }

    public static boolean isAlarmOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(ALARM_STATUS_KEY, false);
    }

    public static void setIsAlarmOn(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(ALARM_STATUS_KEY, isOn)
                .apply();
    }
}
