package com.slgunz.root.sialia.ui.home;

import android.graphics.drawable.Drawable;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.ui.TestSchedulerProvider;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests don't work for now. Some method checking inner private field. Watch "PowerMock" later.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class HomeEndToEndTest {

    @Mock
    private ApplicationDataManager appManager;

    private HomePresenter presenter;

    @Mock
    private HomeContract.View mView;

    @Mock
    private Drawable drawable;

    @Mock
    Tweet tweet;

    List<Tweet> mTweets;

    @Before
    public void initialization() {
        BaseSchedulerProvider scheduler = new TestSchedulerProvider();
        presenter = new HomePresenter(appManager, scheduler);
        mTweets = Arrays.asList(tweet);
    }

    @Test
    public void openingHomeActivityFirstTime() {
        when(appManager.loadHomeTimeLineTweets()).thenReturn(Single.just(mTweets));
        // getting Twitter Home Timeline from DataManager
        // getting Twitter Account Data
        // event HomeFragment::onResume() causes loading tweets from timeline
        presenter.subscribe(mView);
        verify(mView).applyAccountProfile(appManager.getAccountProfile());
        verify(appManager).loadHomeTimeLineTweets();
        // load tweets from service
        verify(appManager).loadHomeTimeLineTweets();
        verify(mView).setWaitingIndicator(true);
        // load tweets to RecyclerView
        verify(mView).setAdapterList(mTweets);
        verify(mView).setWaitingIndicator(false);
    }

    @Test
    public void swipeToUpdateHomeTimelineList() {
        // event SwipeRefreshLayout::onRefresh() cause loading new tweets;
        presenter.loadRecentTweets(9999L);
        // send request to server
//        verify(appManager).loadRecentHomeTimelineTweets(9999L);      // now we don't use it
        verify(appManager).loadHomeTimeLineTweets();
        // send received data to RecyclerView Adapter
        verify(mView).insertBeforeToAdapterList(mTweets);
    }

    @Test
    public void scrollDownListOfTweetsUntilEndAndAppendOlderTimelineTweets() {
        // RecyclerView - ScrollListener cause to loading previous tweets
        presenter.loadPreviousTweets(1234L);
        verify(mView).setWaitingIndicator(true);
        verify(appManager).loadPreviousHomeTimelineTweets(1234L);
        // send received tweets to adapter
        verify(mView).appendToAdapterList(mTweets);
        verify(mView).setWaitingIndicator(false);
    }

    @Test
    public void checkForNewTweetsOnServerByTimer() {
        presenter.checkForNewTweets();
        mView.showNewTweetsNotification(3);
    }

    @Test
    public void successfulSendingTweetViaFAB() {
        String message = "Hello, World";
        presenter.sendTweet(message);
        verify(mView).enableSendTweetButton(false);
        verify(appManager).sendTweet(message);
        verify(mView).enableSendTweetButton(true);
    }

    @Test
    public void search() {
        presenter.search("search request");
        mView.openSearchScreen();
    }
}
