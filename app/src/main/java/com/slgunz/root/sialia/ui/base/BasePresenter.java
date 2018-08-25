package com.slgunz.root.sialia.ui.base;


import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter implements BaseContract.Presenter {

    private final ApplicationDataManager mDataManager;

    private final BaseSchedulerProvider mScheduler;

    private CompositeDisposable mDisposables;

    public BasePresenter(@NonNull ApplicationDataManager dataManager,
                         @NonNull BaseSchedulerProvider scheduler) {
        mDataManager = dataManager;
        mScheduler = scheduler;
        mDisposables = new CompositeDisposable();
    }

    protected void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }

    @CallSuper
    @Override
    public void subscribe(@NonNull BaseFragment view) {
        view.onApplyAccountProfile(mDataManager.getAccountProfile());
    }

    @CallSuper
    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }

    @Override
    public void selectItem(Tweet tweet) {
        mDataManager.setSelectedItem(tweet);
    }

    @Override
    public Tweet currentItem() {
        return mDataManager.getSelectedItem();
    }

    protected void setTweetFavorite(@NonNull BaseFragment view, boolean isFavorite, Long tweetId) {
        Disposable disposable = mDataManager.setFavorite(isFavorite, tweetId)
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribe(
                        tweet -> {

                        },
                        throwable -> {

                        });
        mDisposables.add(disposable);
    }

    protected void setTweetRetweeted(@NonNull BaseFragment view, boolean isRetweeted, Long tweetId) {
        Disposable disposable = mDataManager.setRetweeted(isRetweeted, tweetId)
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribe(
                        tweet -> {
                        },
                        throwable -> {
                        });
        mDisposables.add(disposable);
    }
}
