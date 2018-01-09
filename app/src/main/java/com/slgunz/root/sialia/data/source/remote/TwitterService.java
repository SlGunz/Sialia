package com.slgunz.root.sialia.data.source.remote;

import com.slgunz.root.sialia.data.model.Banners;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.model.User;

import java.util.List;

import io.reactivex.Single;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TwitterService {

    @GET("1.1/account/verify_credentials.json")
    Single<User> verifyCredentials();

    @GET("/1.1/statuses/home_timeline.json")
    Single<List<Tweet>> getStatusesHomeTimeline(@Query("count") Integer count,
                                                    @Query("since_id") Long since_id,
                                                    @Query("max_id") Long max_id);

    @GET("1.1/users/profile_banner.json")
    Single<Banners> getUserProfileBanner(@Query("user_id") Long user_id);

    @GET("1.1/statuses/retweets/{id}.json")
    Single<List<Tweet>> getStatusesRetweet(@Path("id") Long id,
                                           @Query("count") Integer count);

    @FormUrlEncoded
    @POST("1.1/statuses/update.json")
    Single<Tweet> postStatusUpdate(@Field("status") String status);


    @GET("1.1/account/settings.json")
    Call<String> settings();

}
