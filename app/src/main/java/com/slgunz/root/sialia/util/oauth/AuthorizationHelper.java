package com.slgunz.root.sialia.util.oauth;


import android.net.Uri;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface AuthorizationHelper {
    Single<String> retrieveRequestToken(String callbackUrl);

    Completable retrieveAccessToken(String oauthVerifier);

    String getOAuthVerifier(Uri uriOAuthVerifier);

    void setTokenAndSecret(String token, String tokenSecret);

    String getToken();

    String getTokenSecret();
}
