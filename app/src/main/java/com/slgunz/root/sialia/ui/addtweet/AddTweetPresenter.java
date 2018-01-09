package com.slgunz.root.sialia.ui.addtweet;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class AddTweetPresenter implements AddTweetContract.Presenter {

    private final ApplicationDataManager mAppDataManager;

    private final BaseSchedulerProvider mScheduler;
    @Nullable
    private AddTweetContract.View mView;

    private CompositeDisposable mDisposables = new CompositeDisposable();

    @Inject
    AddTweetPresenter(@NonNull ApplicationDataManager applicationDataManager,
                      @NonNull BaseSchedulerProvider schedulerProvider){
        mAppDataManager = applicationDataManager;
        mScheduler = schedulerProvider;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void subscribe(AddTweetContract.View view) {
        mView = view;
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }

    @Override
    public void sendOnlyTextTweet(String message) {
        if(mView != null){
            mView.enableSendTweetButton(false);
        }
        Disposable disposable = mAppDataManager.sendTweet(message)
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribe(
                        // onSuccess
                        tweet -> {
                            if(mView != null){
                                mView.enableSendTweetButton(true);
                            }
                        },
                        // onError
                        throwable->{
                            if(mView != null) {
                                mView.showErrorMessage(throwable);
                                mView.enableSendTweetButton(true);
                            }
                        }
                );
        mDisposables.add(disposable);
    }

    @VisibleForTesting
    public void setView(AddTweetContract.View view){
        mView = view;
    }
}
