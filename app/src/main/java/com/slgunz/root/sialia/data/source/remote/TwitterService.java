package com.slgunz.root.sialia.data.source.remote;

import com.slgunz.root.sialia.data.model.Banners;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.model.User;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("1.1/statuses/mentions_timeline.json")
    Single<List<Tweet>> getStatusesMentionsTimeline(@Query("count") Integer count,
                                                    @Query("since_id") Long since_id,
                                                    @Query("max_id") Long max_id);

    @GET("/1.1/statuses/home_timeline.json")
    Call<List<Tweet>> getStatusesHomeTimelineRecent(@Query("count") Integer count,
                                                    @Query("since_id") Long since_id);

    @GET("1.1/account/settings.json")
    Call<String> settings();

    /**
     * @param status    is the text of the status update.
     * @param id        (in_reply_to_status_id) is an id of tweet to reply, status = "@username"
     * @param media_ids is a comma-delimited list of media_ids to associate with Tweet.
     */
    @FormUrlEncoded
    @POST("1.1/statuses/update.json")
    Single<Tweet> postStatusUpdate(@Field("status") String status,
                                   @Field("in_reply_to_status_id") Long id,
                                   @Field("media_ids") String media_ids);

    @FormUrlEncoded
    @POST("1.1/favorites/create.json")
    Single<Tweet> postFavoriteCreate(@Field("id") Long id);

    @FormUrlEncoded
    @POST("1.1/favorites/destroy.json")
    Single<Tweet> postFavoriteDestroy(@Field("id") Long id);

    @POST("1.1/statuses/retweet/{id}.json")
    Single<Tweet> postStatusRetweet(@Path("id") Long id);

    @POST("1.1/statuses/unretweet/{id}.json")
    Single<Tweet> postStatusUnretweet(@Path("id") Long id);

    /**
     * @param media is the raw binary file content being uploaded.
     */
    @Multipart
    @POST("1.1/media/upload.json")
    Single<Long> postMediaUpload(@Part MultipartBody.Part media);

}
