package com.slgunz.root.sialia.ui.tweetdetail;

import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.ui.BasePresenter;

import java.util.List;

/**
 * Created by root on 09.01.2018.
 */

public interface TweetDetailContract {
    interface View{

        void setTweetOwnerBanner(String url);

        void setAdapterList(List<Tweet> tweets);

        void showErrorMessage(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View>{

        void initialize(Long userId, Long tweetId);
    }
}
