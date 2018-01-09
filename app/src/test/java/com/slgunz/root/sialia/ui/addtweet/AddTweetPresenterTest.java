package com.slgunz.root.sialia.ui.addtweet;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.ui.TestSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class AddTweetPresenterTest {

    private AddTweetPresenter presenter;

    @Mock
    private AddTweetContract.View view;

    @Mock
    private ApplicationDataManager appManager;

    @Before
    public void initialization(){
        MockitoAnnotations.initMocks(this);
        presenter = new AddTweetPresenter(appManager, new TestSchedulerProvider());
    }

    @Test
    public void sendTextMessage(){
        when(appManager.sendTweet(anyString())).thenReturn(Single.just(new Tweet()));
        presenter.setView(view);

        presenter.sendOnlyTextTweet(anyString());
        verify(view).enableSendTweetButton(false);
        verify(appManager).sendTweet(anyString());
        verify(view).enableSendTweetButton(true);
    }
}
