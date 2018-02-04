package com.slgunz.root.sialia.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.model.User;
import com.slgunz.root.sialia.data.service.PollService;
import com.slgunz.root.sialia.di.ActivityScoped;
import com.slgunz.root.sialia.ui.addtweet.AddTweetActivity;
import com.slgunz.root.sialia.ui.common.EndlessRecyclerViewScrollListener;
import com.slgunz.root.sialia.ui.common.ScrollChildSwipyRefreshLayout;
import com.slgunz.root.sialia.ui.common.TweetAdapter;
import com.slgunz.root.sialia.ui.tweetdetail.TweetDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


@ActivityScoped
public class HomeFragment extends DaggerFragment implements HomeContract.View {

    private static final String SERVICE_STATUS_KEY = "serviceStatusKey";
    @Inject
    HomeContract.Presenter mPresenter;

    private boolean mCantRequestNewTweets = true;

    private boolean mIsServiceOn;

    private TweetAdapter mTweetAdapter;

    private ScrollChildSwipyRefreshLayout mSwipyRefreshLayout;

    private RecyclerView mHomeRecyclerView;

    private ProgressBar mProgressBar;

    private ImageView mAccountProfileImage;

    private TextView mAccountName;

    private TextView mAccountScreenName;

    @Inject
    public HomeFragment() {
        // requires empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTweetAdapter = new TweetAdapter(new ArrayList<>(0));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_frag, container, false);

        // set onClick callback; occurs when user select tweet
        mTweetAdapter.setCallback(this::openTweetDetailPage);

        mHomeRecyclerView = root.findViewById(R.id.home_timeline_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mHomeRecyclerView.setAdapter(mTweetAdapter);
        // set custom ScrollListener for loading previous tweets
        EndlessRecyclerViewScrollListener listener =
                new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
                    }
                };
        mHomeRecyclerView.addOnScrollListener(listener);
        mHomeRecyclerView.setLayoutManager(linearLayoutManager);

        //Setup both swipe refresh layout
        mSwipyRefreshLayout = root.findViewById(R.id.home_swipy_refresh_layout);
        mSwipyRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        mSwipyRefreshLayout.setScrollUpChild(mHomeRecyclerView);

        mSwipyRefreshLayout.setOnRefreshListener(
                direction -> {
                    if (mCantRequestNewTweets) {
                        return;
                    }
                    // set an action for swipe up
                    if (direction == SwipyRefreshLayoutDirection.TOP) {
                        mPresenter.loadRecentTweets(mTweetAdapter.getTheBiggestId());
                        mCantRequestNewTweets = true;
                    }
                    // for swipe down
                    if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                        mPresenter.loadPreviousTweets(mTweetAdapter.getTheLowestId());
                        mCantRequestNewTweets = true;
                    }
                }
        );

        // set up floating action button
        FloatingActionButton fab =
                getActivity().findViewById(R.id.fab_tweet);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(view -> openAddTweetScreen());

        mProgressBar = root.findViewById(R.id.home_progress_bar);

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);

        View hView = navigationView.getHeaderView(0);
        mAccountProfileImage = hView.findViewById(R.id.nav_header_profile_image);
        mAccountName = hView.findViewById(R.id.nav_header_account_name);
        mAccountScreenName = hView.findViewById(R.id.nav_header_account_screen_name);

        mIsServiceOn = PollService.isAlarmOn(getActivity());
        setHasOptionsMenu(true);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_actions, menu);
        MenuItem item = menu.findItem(R.id.service_switcher);

        if (mIsServiceOn) {
            item.setTitle(R.string.stop_service);
        } else {
            item.setTitle(R.string.start_service);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.service_switcher:
                mIsServiceOn = !PollService.isAlarmOn(getActivity());
                PollService.startServiceAlarm(getActivity(), mIsServiceOn);
                getActivity().invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////////////////////////////////////////////////////
    // HomeContract.View interface
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void setWaitingIndicator(boolean isActive) {
        mProgressBar.setVisibility(isActive ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void applyAccountProfile(User user) {
        Context context = getActivity();
        if (context != null && mAccountScreenName != null) {
            Glide.with(context)
                    .load(user.getProfileImageUrl())
                    .into(mAccountProfileImage);
        }

        if (mAccountName != null) {
            mAccountName.setText(user.getName());
        }

        if (mAccountScreenName != null) {
            mAccountScreenName.setText(user.getScreenName());
        }
    }

    @Override
    public void openSearchScreen() {

    }

    @Override
    public void showErrorMessage(Throwable throwable) {

    }

    @Override
    public void showNewTweetsNotification(int count) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setAdapterList(List<Tweet> tweets) {
        mTweetAdapter.replaceData(tweets);
        mCantRequestNewTweets = false;
    }

    @Override
    public void insertBeforeToAdapterList(List<Tweet> tweets) {
        mTweetAdapter.replaceData(tweets);
        mSwipyRefreshLayout.setRefreshing(false);
        mCantRequestNewTweets = false;
    }

    @Override
    public void appendToAdapterList(List<Tweet> tweets) {
        mTweetAdapter.appendData(tweets);
        mSwipyRefreshLayout.setRefreshing(false);
        mCantRequestNewTweets = false;
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    public void openAddTweetScreen() {
        Intent intent = new Intent(getActivity(), AddTweetActivity.class);
        startActivity(intent);
    }

    public void openTweetDetailPage(Tweet tweet) {
        Intent intent = TweetDetailActivity.newIntent(getActivity(),
                tweet.getId(), tweet.getUser().getId());
        startActivity(intent);
    }
}
