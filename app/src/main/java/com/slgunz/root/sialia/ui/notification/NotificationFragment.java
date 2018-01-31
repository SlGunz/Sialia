package com.slgunz.root.sialia.ui.notification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.ui.common.TweetAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class NotificationFragment extends DaggerFragment implements NotificationContract.View {

    @Inject
    NotificationContract.Presenter mPresenter;

    RecyclerView mRecyclerView;
    TweetAdapter mTweetAdapter;

    @Inject
    public NotificationFragment(){};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTweetAdapter = new TweetAdapter(new ArrayList<>());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notification_frag, container, false);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(
                view -> {
                    //TODO
                }
        );

        mRecyclerView = root.findViewById(R.id.mention_recycler_view);
        mRecyclerView.setAdapter(mTweetAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
    public void replaceData(List<Tweet> tweets) {
        mTweetAdapter.replaceData(tweets);
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
