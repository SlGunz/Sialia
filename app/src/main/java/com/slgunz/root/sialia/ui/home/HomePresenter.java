package com.slgunz.root.sialia.ui.home;


import android.support.annotation.NonNull;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.di.ActivityScoped;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


@ActivityScoped
class HomePresenter implements HomeContract.Presenter {

    private final ApplicationDataManager mAppDataManager;

    private final BaseSchedulerProvider mScheduler;

    @Nullable
    private HomeContract.View mHomeView;

    private CompositeDisposable mDisposables = new CompositeDisposable();

    @Inject
    HomePresenter(@NonNull ApplicationDataManager applicationDataManager,
                  @NonNull BaseSchedulerProvider schedulerProvider) {
        mAppDataManager = applicationDataManager;
        mScheduler = schedulerProvider;
    }

    interface RequestToServer {
        Single<List<Tweet>> execute();
    }

    interface ProcessData {
        void process(List<Tweet> tweets);
    }

    private void setWaitingIndicator(boolean isActive) {
        if(mHomeView != null) {
            mHomeView.setWaitingIndicator(isActive);
        }
    }

    private void fillHomeTimeline(RequestToServer request, ProcessData data) {
        Disposable disposable = request.execute()
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribe(
                        // onSuccess
                        data::process,
                        // onError
                        throwable -> {
                            if(mHomeView != null) {
                                mHomeView.showErrorMessage(throwable);
                            }
                            setWaitingIndicator(false);
                        }
                );
        mDisposables.add(disposable);
    }

    @Override
    public void loadTweets() {
        setWaitingIndicator(true);
        fillHomeTimeline(
                // request to server
                mAppDataManager::loadHomeTimeLineTweets,
                // update adapter
                tweets -> {
                    if (mHomeView != null) {
                        mHomeView.setAdapterList(tweets);
                    }
                    setWaitingIndicator(false);
                }
        );
    }

    @Override
    public void loadRecentTweets(Long biggestId) {
        // doesn't activate waiting indicator, because SwipeRefreshLayout has it's own.
        fillHomeTimeline(
                // request to server
                mAppDataManager::loadHomeTimeLineTweets,
                // update adapter
                tweets -> {
                    if (mHomeView != null) {
                        mHomeView.insertBeforeToAdapterList(tweets);
                    }
                }
        );

    }

    @Override
    public void loadPreviousTweets(Long lowestId) {
        setWaitingIndicator(true);
        fillHomeTimeline(
                // request to server
                () -> mAppDataManager.loadPreviousHomeTimelineTweets(lowestId),
                // update adapter
                tweets -> {
                    if (mHomeView != null) {
                        mHomeView.appendToAdapterList(tweets);
                    }
                    setWaitingIndicator(false);
                }
        );
    }

    @Override
    public void checkForNewTweets() {

    }

    @Override
    public void search(String request) {

    }

    @Override
    public void sendTweet(String message) {
        Disposable disposable = mAppDataManager.sendTweet(message)
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribe(
                        //onSuccess
                        tweet -> {
                            if (mHomeView != null) {
                                mHomeView.enableSendTweetButton(true);
                            }
                        },
                        //onError
                        throwable -> {
                            if (mHomeView != null) {
                                mHomeView.showErrorMessage(throwable);
                            }
                        }
                );
        mDisposables.add(disposable)      ;
    }

    @Override
    public void subscribe(HomeContract.View view) {
        this.mHomeView = view;
        if (mHomeView != null) {
            mHomeView.applyAccountProfile(mAppDataManager.getAccountProfile());
        }
        loadTweets();
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
