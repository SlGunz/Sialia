package com.slgunz.root.sialia.ui.addtweet;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.ui.base.BaseFragment;
import com.slgunz.root.sialia.ui.base.BasePresenter;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;

import java.io.File;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class AddTweetPresenter extends BasePresenter implements AddTweetContract.Presenter {

    private final ApplicationDataManager mAppDataManager;

    private final BaseSchedulerProvider mScheduler;
    @Nullable
    private AddTweetContract.View mView;

    @Inject
    AddTweetPresenter(@NonNull ApplicationDataManager applicationDataManager,
                      @NonNull BaseSchedulerProvider schedulerProvider) {
        super(applicationDataManager, schedulerProvider);
        mAppDataManager = applicationDataManager;
        mScheduler = schedulerProvider;
    }

    @Override
    public void subscribe(@NonNull BaseFragment view) {
        super.subscribe(view);
        mView = (AddTweetContract.View) view;
    }

    @Override
    public void sendTweet(String message, Long retweetId, String mediaIds) {
        if (mView != null) {
            mView.enableSendTweetButton(false);
        }
        Disposable disposable = mAppDataManager.sendTweet(message, retweetId, mediaIds)
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribe(
                        // onSuccess
                        tweet -> {
                            if (mView != null) {
                                mView.enableSendTweetButton(true);
                            }
                        },
                        // onError
                        throwable -> {
                            if (mView != null) {
                                mView.showErrorMessage(throwable);
                                mView.enableSendTweetButton(true);
                            }
                        }
                );
        addDisposable(disposable);
    }

    @Override
    public void uploadImage(File file, String name) {
        if (mView != null) {
            mView.enableSendTweetButton(false);
        }
        Disposable disposable = mAppDataManager.uploadImage(file, name)
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribe(
                        mediaId -> {
                            if (mView != null) {
                                mView.showUploadedImage(mediaId);
                                mView.enableSendTweetButton(true);
                            }
                        },
                        throwable -> {
                            if (mView != null) {
                                mView.showErrorMessage(throwable);
                                mView.enableSendTweetButton(true);
                            }
                        }
                );

        addDisposable(disposable);
    }

    @VisibleForTesting
    public void setView(AddTweetContract.View view) {
        mView = view;
    }
}
