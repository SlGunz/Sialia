package com.slgunz.root.sialia.data.source.local;

import android.content.Context;
import android.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class SialiaLocalDataSourceImpl implements SialiaLocalDataSource {

    private static final String OAUTH_USER_KEY = "oauthUserKey";
    private static final String OAUTH_USER_SECRET = "oauthUserSecret";

    @Inject
    SialiaLocalDataSourceImpl() {
    }

    @Override
    public void setOAuthUserKey(Context context, String userKey) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(OAUTH_USER_KEY, userKey)
                .apply();
    }

    @Override
    public void setOAuthUserSecret(Context context, String userSecret) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(OAUTH_USER_SECRET, userSecret)
                .apply();
    }

    @Override
    public String getOAuthUserKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(OAUTH_USER_KEY, null);
    }

    @Override
    public String getOAuthUserSecret(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(OAUTH_USER_SECRET, null);
    }

    @Override
    public boolean hasUserKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .contains(OAUTH_USER_KEY);
    }

    @Override
    public boolean hasUserSecret(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .contains(OAUTH_USER_SECRET);
    }


}
