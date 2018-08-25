package com.slgunz.root.sialia.ui.notification;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.ui.base.BaseFragment;
import com.slgunz.root.sialia.ui.base.BasePresenter;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;


import javax.inject.Inject;

import io.reactivex.disposables.Disposable;


public class NotificationPresenter extends BasePresenter implements NotificationContract.Presenter {

    private final ApplicationDataManager mDataManager;
    private final BaseSchedulerProvider mScheduler;
    @Nullable
    private NotificationContract.View mView;

    @Inject
    public NotificationPresenter(@NonNull ApplicationDataManager dataManager,
                                 @NonNull BaseSchedulerProvider scheduler) {
        super(dataManager, scheduler);
        mDataManager = dataManager;
        mScheduler = scheduler;
    }

    @Override
    public void subscribe(@NonNull BaseFragment view) {
        super.subscribe(view);
        mView = (NotificationContract.View) view;
        loadMentionsTweets();
    }

    @Override
    public void loadMentionsTweets() {
        Disposable disposable = mDataManager.loadMentionsTweets()
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribe(
                        tweets -> {
                            if (mView != null) {
                                mView.replaceData(tweets);
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
}
