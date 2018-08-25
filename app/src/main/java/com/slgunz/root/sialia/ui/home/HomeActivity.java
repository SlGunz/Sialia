package com.slgunz.root.sialia.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.di.ActivityInjector;
import com.slgunz.root.sialia.ui.base.BaseActivity;
import com.slgunz.root.sialia.util.ActivityUtils;

import javax.inject.Inject;

import dagger.Lazy;

public class HomeActivity extends BaseActivity {

    @Inject
    Lazy<HomeFragment> mHomeFragment;

    private DrawerLayout mDrawerLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityInjector.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setDrawerContent(navigationView);
        }

        // Create view
        HomeFragment homeFragment =
                (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        // Load previous state if available
        if (homeFragment == null) {
            homeFragment = mHomeFragment.get();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), homeFragment, R.id.contentFrame);
        }
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    protected Context getContext() {
        return HomeActivity.this;
    }

    @Override
    protected int getLinkedMenuItem() {
        return R.id.nav_menu_item_home;
    }
}
