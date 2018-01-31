package com.slgunz.root.sialia.ui.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.ui.home.HomeActivity;
import com.slgunz.root.sialia.util.ActivityUtils;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class NotificationActivity extends DaggerAppCompatActivity {

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

    private void setDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                (menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_menu_item_home:
                            Intent intent = new Intent(NotificationActivity.this,
                                    HomeActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_menu_item_notification:
                            // Do nothing, we're already on that screen
                            break;
                        default:
                            break;
                    }
                    // Close the navigation drawer when an item is selected.
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                })
        );
    }

}
