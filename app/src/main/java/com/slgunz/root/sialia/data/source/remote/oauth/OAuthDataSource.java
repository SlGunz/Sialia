package com.slgunz.root.sialia.data.source.remote.oauth;


public interface OAuthDataSource {
    String getRequestTokenUrl();

    String getAccessTokenUrl();

    String getAuthorizeUrl();

    String getConsumerKey();

    String getConsumerSecret();

    String getBaseUrl();
}
