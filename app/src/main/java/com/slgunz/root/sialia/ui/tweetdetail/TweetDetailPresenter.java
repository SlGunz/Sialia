package com.slgunz.root.sialia.ui.tweetdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.subtype.Banner;
import com.slgunz.root.sialia.ui.base.BaseFragment;
import com.slgunz.root.sialia.ui.base.BasePresenter;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class TweetDetailPresenter extends BasePresenter implements TweetDetailContract.Presenter {

    private final ApplicationDataManager mDataManager;

    private final BaseSchedulerProvider mScheduler;
    @Nullable
    private TweetDetailContract.View mView;

    @Inject
    TweetDetailPresenter(@NonNull ApplicationDataManager dataManager,
                         @NonNull BaseSchedulerProvider scheduler) {
        super(dataManager, scheduler);
        mDataManager = dataManager;
        mScheduler = scheduler;
    }

    @Override
    public void subscribe(@NonNull BaseFragment view) {
        super.subscribe(view);
        mView = (TweetDetailContract.View) view;
        loadRetweetedTweets();
    }

    private void loadRetweetedTweets() {
        Long tweetId = currentItem().getId();
        Disposable disposable = mDataManager.loadRetweetedTweets(tweetId)
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
        addDisposable(disposable);
    }


    private void receiveUserBanner() {
        Long tweetId = currentItem().getId();
        Disposable disposable = mDataManager.loadUserProfileBanner(tweetId)
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
        addDisposable(disposable);
    }

    @VisibleForTesting
    public void setView(TweetDetailContract.View view) {
        mView = view;
    }
}
