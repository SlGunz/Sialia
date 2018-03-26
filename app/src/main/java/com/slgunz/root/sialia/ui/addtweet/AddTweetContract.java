package com.slgunz.root.sialia.ui.addtweet;

import com.slgunz.root.sialia.ui.base.BaseContract;

import java.io.File;


public interface AddTweetContract {
    interface View {
        void enableSendTweetButton(boolean isEnable);

        void showErrorMessage(Throwable throwable);

        void showUploadedImage(Long mediaId);
    }

    interface Presenter extends BaseContract.Presenter {
        void sendTweet(String message, Long retweetId, String mediaIds);

        void uploadImage(File file, String name);
    }
}
