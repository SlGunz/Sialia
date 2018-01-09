package com.slgunz.root.sialia.ui.tweetdetail;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.subtype.Banner;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class TweetDetailPresenter implements TweetDetailContract.Presenter {

    private final ApplicationDataManager mAppDataManager;

    private final BaseSchedulerProvider mScheduler;
    @Nullable
    private TweetDetailContract.View mView;

    private CompositeDisposable mDisposables = new CompositeDisposable();

    private Long mUserId;

    private Long mTweetId;

    @Inject
    TweetDetailPresenter(@NonNull ApplicationDataManager applicationDataManager,
                         @NonNull BaseSchedulerProvider schedulerProvider) {
        mAppDataManager = applicationDataManager;
        mScheduler = schedulerProvider;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void subscribe(TweetDetailContract.View view) {
        mView = view;
        receiveUserBanner();
        loadRetweetedTweets();
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }

    private void loadRetweetedTweets() {
        Disposable disposable = mAppDataManager.loadRetweetedTweets(mTweetId)
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribe(
                        tweets -> {
                            if (mView != null) {
                                mView.setAdapterList(tweets);
                            }
                        },
                        throwable -> {
                            if (mView != null) {
                                mView.showErrorMessage(throwable);
                            }
                        }
                );
        mDisposables.add(disposable);
    }

    private void receiveUserBanner() {
        Disposable disposable = mAppDataManager.loadUserProfileBanner(mUserId)
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribe(
                        // onSuccess
                        banners -> {
                            if (mView != null) {
                                Banner banner = banners.getMobile();
                                String url = banner == null ? null : banner.getUrl();
                                mView.setTweetOwnerBanner(url);
                            }
                        },
                        // onError
                        throwable -> {
                            if (mView != null) {
                                mView.showErrorMessage(throwable);
                            }
                        }
                );
        mDisposables.add(disposable);
    }

    @Override
    public void initialize(Long userId, Long tweetId) {
        mTweetId = tweetId;
        mUserId = userId;
    }

    @VisibleForTesting
    public void setView(TweetDetailContract.View view) {
        mView = view;
    }
}
