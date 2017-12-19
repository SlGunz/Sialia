package com.slgunz.root.sialia.data.source.remote.oauth;


public class TwitterOAuthDataSourceImpl implements OAuthDataSource {

    private static String consumerKey = "03JcpH8sBICLcBWCx9vTSIWsk";
    private static String consumerSecret = "51XiylOqm1tN2we6eQYHrl8lEPKfVTzv25e3AcSQcWRPbcBetw";

    private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    private static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
    private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";

    private static final String BASE_URL = "https://api.twitter.com/";

    @Override
    public String getRequestTokenUrl() {
        return REQUEST_TOKEN_URL;
    }

    @Override
    public String getAccessTokenUrl() {
        return ACCESS_TOKEN_URL;
    }

    @Override
    public String getAuthorizeUrl() {
        return AUTHORIZE_URL;
    }

    @Override
    public String getConsumerKey() {
        return consumerKey;
    }

    @Override
    public String getConsumerSecret() {
        return consumerSecret;
    }

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }


}
