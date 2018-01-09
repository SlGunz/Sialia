package com.slgunz.root.sialia.ui.addtweet;

import com.slgunz.root.sialia.ui.BasePresenter;

/**
 * Created by root on 09.01.2018.
 */

public interface AddTweetContract {
    interface View {
        void enableSendTweetButton(boolean isEnable);

        void showErrorMessage(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View> {
        void sendOnlyTextTweet(String message);
    }
}
