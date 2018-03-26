package com.slgunz.root.sialia.ui.tweetdetail;

import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.ui.base.BaseContract;

import java.util.List;


public interface TweetDetailContract {
    interface View {

        void setTweetOwnerBanner(String url);

        void setAdapterList(List<Tweet> tweets);

        void showErrorMessage(Throwable throwable);
    }

    interface Presenter extends BaseContract.Presenter {

    }
}
