package com.slgunz.root.sialia.ui.addtweet;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.ui.TestSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.net.URI;

import io.reactivex.Single;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddTweetPresenterTest {

    private AddTweetPresenter presenter;

    @Mock
    private AddTweetContract.View view;

    @Mock
    private ApplicationDataManager appManager;

    @Mock
    private File file;

    @Before
    public void initialization() {
        MockitoAnnotations.initMocks(this);
        presenter = new AddTweetPresenter(appManager, new TestSchedulerProvider());
    }

    @Test
    public void sendTextMessage() {
        when(appManager.sendTweet("message", 123L, "1,2")).thenReturn(Single.just(new Tweet()));
        presenter.setView(view);

        presenter.sendTweet("message", 123L, "1,2");
        verify(view).enableSendTweetButton(false);
        verify(appManager).sendTweet("message", 123L, "1,2");
        verify(view).enableSendTweetButton(true);
    }

    @Test
    public void addImage() {
        when(appManager.uploadImage(file, "name")).thenReturn(Single.just(1L));
        presenter.setView(view);

        presenter.uploadImage(file, "name");
        verify(view).enableSendTweetButton(false);
        verify(appManager).uploadImage(file, "name");
        verify(view).showUploadedImage(1L);
        verify(view).enableSendTweetButton(true);
    }
}
