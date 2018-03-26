package com.slgunz.root.sialia.ui.notification;

import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.ui.base.BaseContract;

import java.util.List;


public interface NotificationContract {
    interface View {
        void replaceData(List<Tweet> tweets);

        void showErrorMessage(Throwable throwable);
    }

    interface Presenter extends BaseContract.Presenter {
        void loadMentionsTweets();
    }
}
