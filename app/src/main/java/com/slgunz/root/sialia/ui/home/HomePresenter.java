package com.slgunz.root.sialia.ui.home;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.di.ActivityScoped;
import com.slgunz.root.sialia.ui.base.BaseFragment;
import com.slgunz.root.sialia.ui.base.BasePresenter;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


@ActivityScoped
class HomePresenter extends BasePresenter implements HomeContract.Presenter {

    private final ApplicationDataManager mAppDataManager;

    private final BaseSchedulerProvider mScheduler;

    @Nullable
    private HomeContract.View mHomeView;

    @Inject
    HomePresenter(@NonNull ApplicationDataManager applicationDataManager,
                  @NonNull BaseSchedulerProvider schedulerProvider) {
        super(applicationDataManager, schedulerProvider);
        mAppDataManager = applicationDataManager;
        mScheduler = schedulerProvider;
    }

    // TODO delete and substitute on io.reactive interface
    interface RequestToServer {
        Single<List<Tweet>> execute();
    }

    interface ProcessData {
        void process(List<Tweet> tweets);
    }

    private void setWaitingIndicator(boolean isActive) {
        if (mHomeView != null) {
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
                            if (mHomeView != null) {
                                mHomeView.showErrorMessage(throwable);
                            }
                            setWaitingIndicator(false);
                        }
                );
        addDisposable(disposable);
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
    public void loadRecentTweets(Long maxId) {
        // doesn't activate waiting indicator, because SwipeRefreshLayout has it's own.
        if (mHomeView != null) {
            mAppDataManager.setLastLoadedTweetId(mHomeView.getContext(), maxId);
        }
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
    public void loadPreviousTweets(Long minId) {
        fillHomeTimeline(
                // request to server
                () -> mAppDataManager.loadPreviousHomeTimelineTweets(minId),
                // update adapter
                tweets -> {
                    if (mHomeView != null) {
                        mHomeView.appendToAdapterList(tweets);
                    }
                }
        );
    }

    @Override
    public void subscribe(BaseFragment view) {
        super.subscribe(view);
        this.mHomeView = (HomeContract.View) view;
        loadTweets();
    }

    @VisibleForTesting
    public void setHomeView(HomeContract.View homeView) {
        this.mHomeView = homeView;
    }
}
