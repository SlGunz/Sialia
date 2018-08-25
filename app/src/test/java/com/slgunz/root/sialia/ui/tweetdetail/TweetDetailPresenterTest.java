package com.slgunz.root.sialia.ui.tweetdetail;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.Banners;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.model.subtype.Banner;
import com.slgunz.root.sialia.ui.TestSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TweetDetailPresenterTest {

    TweetDetailPresenter presenter;
    @Mock
    TweetDetailFragment view;
    @Mock
    ApplicationDataManager appManager;
    @Mock
    Banners banners;
    @Mock
    Banner banner;

    List<Tweet> tweets;
    @Mock
    Tweet tweet;

    @Before
    public void initialization() {
        MockitoAnnotations.initMocks(this);
        presenter = new TweetDetailPresenter(appManager, new TestSchedulerProvider());
        tweet = new Tweet();
        tweets = Arrays.asList(tweet);
    }

    @Test
    public void openingTweetDetailScreen() {
        when(appManager.loadUserProfileBanner(anyLong())).thenReturn(Single.just(banners));
        when(appManager.loadRetweetedTweets(anyLong())).thenReturn(Single.just(tweets));
        when(banners.getMobile()).thenReturn(banner);
        when(banner.getUrl()).thenReturn("someUrl");
        when(presenter.currentItem()).thenReturn(tweet);
        presenter.setView(view);

        presenter.subscribe(view);
        verify(appManager).loadUserProfileBanner(anyLong());
        verify(view).setTweetOwnerBanner("someUrl");
        verify(appManager).loadRetweetedTweets(anyLong());
        verify(view).setAdapterList(tweets);
    }

}
