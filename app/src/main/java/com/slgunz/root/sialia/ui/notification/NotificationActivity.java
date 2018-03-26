package com.slgunz.root.sialia.ui.notification;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.ui.base.BaseActivity;
import com.slgunz.root.sialia.util.ActivityUtils;

import javax.inject.Inject;

import dagger.Lazy;

public class NotificationActivity extends BaseActivity {

    @Inject
    Lazy<NotificationFragment> mNotificationFragment;

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setDrawerContent(navigationView);
        }

        NotificationFragment notificationFragment =
                (NotificationFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (notificationFragment == null) {
            notificationFragment = mNotificationFragment.get();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    notificationFragment, R.id.contentFrame);
        }
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    protected Context getContext() {
        return NotificationActivity.this;
    }

    @Override
    protected int getLinkedMenuItem() {
        return R.id.nav_menu_item_notification;
    }
}
