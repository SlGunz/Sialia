package com.slgunz.root.sialia.ui.notifications;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.ui.TestSchedulerProvider;
import com.slgunz.root.sialia.ui.notification.NotificationContract;
import com.slgunz.root.sialia.ui.notification.NotificationPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class NotificationPresenterTest {

    NotificationPresenter mPresenter;

    @Mock
    NotificationContract.View mView;

    @Mock
    ApplicationDataManager mDataManager;

    List<Tweet> mTweetList;

    @Before
    public void initialization() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new NotificationPresenter(mDataManager, new TestSchedulerProvider());
        mTweetList = Collections.emptyList();
    }

    @Test
    public void whenFragmentOpening_loadingMentionTweetsFromServer() {
        when(mDataManager.loadMentionsTweets()).thenReturn(Single.just(mTweetList));

        mPresenter.subscribe(mView);
        verify(mDataManager).loadMentionsTweets();
        verify(mView).replaceData(mTweetList);
    }
}
