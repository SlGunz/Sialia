package com.slgunz.root.sialia.ui.home;

import android.content.Context;

import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.ui.base.BaseContract;
import com.slgunz.root.sialia.ui.base.BaseFragment;

import java.util.List;

public interface HomeContract {

    interface View {
        void enableProgressBar(boolean isActive);

        void showErrorMessage(Throwable throwable);

        boolean isActive();

        void setAdapterList(List<Tweet> tweets);

        void insertInAdapterList(List<Tweet> tweets);

        void appendToAdapterList(List<Tweet> tweets);

        Context getContext();
    }

    interface Presenter extends BaseContract.Presenter{

        void loadTweets();

        void loadRecentTweets(Long maxId);

        void loadPreviousTweets(Long minId);
    }
}
