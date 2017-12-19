package com.slgunz.root.sialia.data.source.remote;

import com.slgunz.root.sialia.data.source.remote.oauth.OAuthDataSource;
import com.slgunz.root.sialia.data.source.remote.oauth.TwitterOAuthDataSourceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthProvider;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;


@Module
public class NetworkModule {

    @Provides
    @Singleton
    OAuthDataSource provideServiceOAuth() {
        return new TwitterOAuthDataSourceImpl();
    }

    @Provides
    @Singleton
    OkHttpOAuthProvider provideOkHttpOAuthProvider(OAuthDataSource oauth) {
        return new OkHttpOAuthProvider(
                oauth.getRequestTokenUrl(),
                oauth.getAccessTokenUrl(),
                oauth.getAuthorizeUrl()
        );
    }

    @Provides
    @Singleton
    OkHttpOAuthConsumer provideOkHttpOAuthConsumer(OAuthDataSource oauth) {
        return new OkHttpOAuthConsumer(
                oauth.getConsumerKey(),
                oauth.getConsumerSecret()
        );
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(OkHttpOAuthConsumer consumer) {
        return new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OAuthDataSource oauth, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(oauth.getBaseUrl())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    TwitterService provideTwitterService(Retrofit retrofit) {
        return retrofit.create(TwitterService.class);
    }
}
