package com.slgunz.root.sialia.ui.tweetdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.source.remote.GlideApp;
import com.slgunz.root.sialia.ui.common.TweetAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class TweetDetailFragment extends DaggerFragment implements TweetDetailContract.View {

    private static final String TWEET_ID = "tweet_id";
    private static final String USER_ID = "user_id";
    @Inject
    TweetDetailContract.Presenter mPresenter;

    TweetAdapter mTweetAdapter;

    ImageView mBannerImageView;

    RecyclerView mRecyclerView;

    @Inject
    public TweetDetailFragment() {
    }

    public static TweetDetailFragment newInstance(Long tweetId, Long userId) {
        Bundle args = new Bundle();
        args.putLong(TWEET_ID, tweetId);
        args.putLong(USER_ID, userId);

        TweetDetailFragment fragment = new TweetDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTweetAdapter = new TweetAdapter(new ArrayList<>(0));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.tweetdetail_frag, container, false);

        Long tweetId = getArguments().getLong(TWEET_ID);
        Long userId = getArguments().getLong(USER_ID);
        mPresenter.initialize(userId, tweetId);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(
                view -> {

                }
        );

        mRecyclerView = root.findViewById(R.id.tweetdetail_recycler_view);
        mRecyclerView.setAdapter(mTweetAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBannerImageView = getActivity().findViewById(R.id.toolbar_imageview);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setTweetOwnerBanner(String url) {
        GlideApp.with(getActivity())
                .load(url)
                .into(mBannerImageView);
    }

    @Override
    public void setAdapterList(List<Tweet> tweets) {
        mTweetAdapter.replaceData(tweets);
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
