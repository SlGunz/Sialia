package com.slgunz.root.sialia.ui.notification;

import android.support.annotation.NonNull;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.ui.base.BaseFragment;
import com.slgunz.root.sialia.ui.base.BasePresenter;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class NotificationPresenter extends BasePresenter implements NotificationContract.Presenter {

    private final ApplicationDataManager mAppDataManager;
    private final BaseSchedulerProvider mScheduler;

    @Nullable
    private NotificationContract.View mView;

    @Inject
    public NotificationPresenter(@NonNull ApplicationDataManager applicationDataManager,
                                 @NonNull BaseSchedulerProvider schedulerProvider) {
        super(applicationDataManager, schedulerProvider);
        mAppDataManager = applicationDataManager;
        mScheduler = schedulerProvider;
    }

    @Override
    public void subscribe(@NonNull BaseFragment view) {
        super.subscribe(view);
        mView = (NotificationContract.View) view;
        if (mView != null) {
            loadMentionsTweets();
        }
    }

    @Override
    public void loadMentionsTweets() {
        Disposable disposable = mAppDataManager.loadMentionsTweets()
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
