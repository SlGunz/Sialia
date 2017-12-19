package com.slgunz.root.sialia.data.source.remote;


import android.net.Uri;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface AuthorizationRemoteDataSource {
    Single<String> retrieveRequestToken(String callbackUrl);

    Completable retrieveAccessToken(String oauthVerifier);

    String getOAuthVerifier(Uri uriOAuthVerifier);

    void setTokenAndSecret(String token, String tokenSecret);

    String getToken();

    String getTokenSecret();
}
