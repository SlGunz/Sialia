package com.slgunz.root.sialia.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.data.service.PollService;
import com.slgunz.root.sialia.di.scopes.ActivityScoped;
import com.slgunz.root.sialia.ui.addtweet.AddTweetActivity;
import com.slgunz.root.sialia.ui.base.BaseFragment;
import com.slgunz.root.sialia.ui.base.BasePresenter;
import com.slgunz.root.sialia.ui.common.EndlessRecyclerViewScrollListener;
import com.slgunz.root.sialia.ui.common.ScrollChildSwipyRefreshLayout;
import com.slgunz.root.sialia.ui.common.TweetAdapter;
import com.slgunz.root.sialia.ui.tweetdetail.TweetDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@ActivityScoped
public class HomeFragment extends BaseFragment implements HomeContract.View {

    private static final String SERVICE_STATUS_KEY = "serviceStatusKey";
    @Inject
    HomeContract.Presenter mPresenter;

    private boolean mCanSwipe;

    private boolean mIsServiceEnabled;

    private TweetAdapter mTweetAdapter;
    // third-party widget
    private ScrollChildSwipyRefreshLayout mSwipyRefreshLayout;

    private RecyclerView mRecyclerView;

    private ProgressBar mProgressBar;

    @Inject
    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTweetAdapter = new TweetAdapter(new ArrayList<>(0));
        setAsStartPage(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected BasePresenter getPresenter() {
        return (BasePresenter) mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.home_frag, container, false);

        mProgressBar = root.findViewById(R.id.home_progress_bar);

        setRecyclerViewContent(root);
        setSwipeRefreshLayoutContent(root);
        setFabContent();

        mIsServiceEnabled = PollService.isAlarmOn(getActivity());

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

        if (mIsServiceEnabled) {
            item.setTitle(R.string.menu_item_stop);
        } else {
            item.setTitle(R.string.menu_item_start);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.service_switcher:
                mIsServiceEnabled = !PollService.isAlarmOn(getActivity());
                PollService.startServiceAlarm(getActivity(), mIsServiceEnabled);
                getActivity().invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////////////////////////////////////////////////////
    // HomeContract.View interface
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void enableProgressBar(boolean isActive) {
        mProgressBar.setVisibility(isActive ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setAdapterList(List<Tweet> tweets) {
        mTweetAdapter.replaceData(tweets);
        mCanSwipe = true;
    }

    @Override
    public void insertInAdapterList(List<Tweet> tweets) {
        mTweetAdapter.replaceData(tweets);
        mSwipyRefreshLayout.setRefreshing(false);
        mCanSwipe = true;
    }

    @Override
    public void appendToAdapterList(List<Tweet> tweets) {
        mTweetAdapter.appendData(tweets);
        mSwipyRefreshLayout.setRefreshing(false);
        mCanSwipe = true;
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

    public void openAddTweetScreen(Tweet tweet) {
        Intent intent = new Intent(getActivity(), AddTweetActivity.class);
        selectItem(tweet);
        startActivity(intent);
    }

    public void openTweetDetailPage(Tweet tweet) {
        Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
        selectItem(tweet);
        startActivity(intent);
    }

    ///////////////////////////////////////////////////////////////////////////
    // set controller content
    ///////////////////////////////////////////////////////////////////////////

    private void setRecyclerViewContent(View root) {
        // set onClick callback; occurs when user select tweet
        mTweetAdapter.setOnClickListener(this::openTweetDetailPage);
        mTweetAdapter.addTweetReactionListener(
                this::setFavorite,
                this::setRetweeted,
                this::openAddTweetScreen
        );

        mRecyclerView = root.findViewById(R.id.home_timeline_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mTweetAdapter);
        // set custom ScrollListener for loading previous tweets
        EndlessRecyclerViewScrollListener listener =
                new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
                    }
                };
        mRecyclerView.addOnScrollListener(listener);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // item divider decoration
        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void setSwipeRefreshLayoutContent(View root) {
        //Setup both swipe refresh layout
        mSwipyRefreshLayout = root.findViewById(R.id.home_swipy_refresh_layout);
        mSwipyRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        mSwipyRefreshLayout.setScrollUpChild(mRecyclerView);

        mSwipyRefreshLayout.setOnRefreshListener(
                direction -> {
                    if (!mCanSwipe) {
                        return;
                    }
                    // set an action for swipe up
                    if (direction == SwipyRefreshLayoutDirection.TOP) {
                        mPresenter.loadRecentTweets(mTweetAdapter.getMaxId());
                        mCanSwipe = false;
                    }
                    // for swipe down
                    if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                        mPresenter.loadPreviousTweets(mTweetAdapter.getMinId());
                        mCanSwipe = false;
                    }
                }
        );
    }

    private void setFabContent() {
        // set up floating action button
        FloatingActionButton fab =
                getActivity().findViewById(R.id.fab_tweet);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(view -> openAddTweetScreen());
    }
}


