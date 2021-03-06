package com.slgunz.root.sialia.util.oauth;

import android.net.Uri;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;

import io.reactivex.Single;

import oauth.signpost.OAuth;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthProvider;


public class AuthorizationHelperImpl implements AuthorizationHelper {
    @Inject
    OkHttpOAuthProvider mOkHttpOAuthProvider;
    @Inject
    OkHttpOAuthConsumer mOkHttpOAuthConsumer;

    @Inject
    AuthorizationHelperImpl() {
    }

    public Single<String> retrieveRequestToken(final String callbackUrl) {
        return Single.fromCallable(
                () -> mOkHttpOAuthProvider.retrieveRequestToken(mOkHttpOAuthConsumer, callbackUrl)
        );
    }

    @Override
    public Completable retrieveAccessToken(final String oauthVerifier) {
        return Completable.fromAction(
                () -> mOkHttpOAuthProvider.retrieveAccessToken(mOkHttpOAuthConsumer, oauthVerifier)
        );
    }

    @Override
    public String getOAuthVerifier(Uri uriOAuthVerifier) {
        return uriOAuthVerifier.getQueryParameter(OAuth.OAUTH_VERIFIER);
    }

    @Override
    public void setTokenAndSecret(String token, String tokenSecret) {
        mOkHttpOAuthConsumer.setTokenWithSecret(token, tokenSecret);
    }

    @Override
    public String getToken() {
        return mOkHttpOAuthConsumer.getToken();
    }

    @Override
    public String getTokenSecret() {
        return mOkHttpOAuthConsumer.getTokenSecret();
    }
}
