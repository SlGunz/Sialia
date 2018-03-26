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
import com.slgunz.root.sialia.ui.base.BaseFragment;
import com.slgunz.root.sialia.ui.base.BasePresenter;
import com.slgunz.root.sialia.ui.common.GlideApp;
import com.slgunz.root.sialia.ui.common.TweetAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class TweetDetailFragment extends BaseFragment implements TweetDetailContract.View {

    @Inject
    TweetDetailContract.Presenter mPresenter;

    TweetAdapter mTweetAdapter;

    ImageView mBannerImageView;

    RecyclerView mRecyclerView;

    @Inject
    public TweetDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTweetAdapter = new TweetAdapter(new ArrayList<>(0));
    }

    @Override
    protected BasePresenter getPresenter() {
        return (BasePresenter) mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.tweetdetail_frag, container, false);

        mRecyclerView = root.findViewById(R.id.tweetdetail_recycler_view);
        mRecyclerView.setAdapter(mTweetAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBannerImageView = getActivity().findViewById(R.id.toolbar_background);

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
