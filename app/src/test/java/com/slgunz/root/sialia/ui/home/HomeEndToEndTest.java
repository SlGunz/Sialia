package com.slgunz.root.sialia.ui.home;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.ui.TestSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HomeEndToEndTest {

    @Mock
    private ApplicationDataManager appManager;

    private HomePresenter presenter;
    @Mock
    private HomeFragment mView;

    private List<Tweet> mTweets;

    @Before
    public void initialization() {
        presenter = new HomePresenter(appManager, new TestSchedulerProvider());
        mTweets = Arrays.asList(new Tweet());
    }

    @Test
    public void openingHomeActivityFirstTime() {
        when(appManager.loadHomeTimeLineTweets()).thenReturn(Single.just(mTweets));
        // getting Twitter Home Timeline from DataManager
        // getting Twitter Account Data
        // event HomeFragment::onResume() causes loading tweets from timeline
        presenter.subscribe(mView);
//        verify(mView).onApplyAccountProfile(appManager.getAccountProfile());
        verify(appManager).loadHomeTimeLineTweets();
        // load tweets from service
        verify(appManager).loadHomeTimeLineTweets();
        verify(mView).enableProgressBar(true);
        // load tweets to RecyclerView
        verify(mView).setAdapterList(mTweets);
        verify(mView).enableProgressBar(false);
    }

    @Test
    public void swipeToUpdateHomeTimelineList() {
        when(appManager.loadHomeTimeLineTweets()).thenReturn(Single.just(mTweets));
        presenter.setHomeView(mView);
        // event SwipeRefreshLayout::onRefresh() cause loading new tweets;
        presenter.loadRecentTweets(123L);
        // send request to server
        verify(appManager).loadHomeTimeLineTweets();
        // send received data to RecyclerView Adapter
        verify(mView).insertInAdapterList(mTweets);
    }

    @Test
    public void scrollDownListOfTweetsUntilEndAndAppendOlderTimelineTweets() {
        when(appManager.loadPreviousHomeTimelineTweets(anyLong())).thenReturn(Single.just(mTweets));
        presenter.setHomeView(mView);
        // RecyclerView - ScrollListener cause to loading previous tweets
        presenter.loadPreviousTweets(anyLong());
        verify(appManager).loadPreviousHomeTimelineTweets(anyLong());
        // send received tweets to adapter
        verify(mView).appendToAdapterList(mTweets);
    }

}
