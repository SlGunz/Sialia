package com.slgunz.root.sialia.ui.home;

import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.model.User;
import com.slgunz.root.sialia.ui.BasePresenter;

import java.util.List;

public interface HomeContract {

    interface View {
        void setWaitingIndicator(boolean isActive);

        void applyAccountProfile(User user);

        void openSearchScreen();

        void showErrorMessage(Throwable throwable);

        void showNewTweetsNotification(int count);

        boolean isActive();

        void setAdapterList(List<Tweet> tweets);

        void insertBeforeToAdapterList(List<Tweet> tweets);

        void appendToAdapterList(List<Tweet> tweets);
    }

    interface Presenter extends BasePresenter<View> {

        void loadTweets();

        void loadRecentTweets(Long biggestId);

        void loadPreviousTweets(Long lowestId);

        void checkForNewTweets();

        void search(String request);
    }
}
