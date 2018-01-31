package com.slgunz.root.sialia.ui.notification;

import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.ui.BasePresenter;

import java.util.List;


public interface NotificationContract {
    interface View {
        void replaceData(List<Tweet> tweets);

        void showErrorMessage(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View> {
        void loadMentionsTweets();
    }
}
