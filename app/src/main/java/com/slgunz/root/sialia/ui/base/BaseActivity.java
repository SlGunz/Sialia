package com.slgunz.root.sialia.ui.base;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.data.source.local.PreferenceHelper;
import com.slgunz.root.sialia.ui.home.HomeActivity;
import com.slgunz.root.sialia.ui.login.LoginActivity;
import com.slgunz.root.sialia.ui.notification.NotificationActivity;
import com.slgunz.root.sialia.ui.settings.SettingsActivity;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    protected abstract DrawerLayout getDrawerLayout();

    protected abstract Context getContext();

    protected abstract int getLinkedMenuItem();

    protected void setDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                (menuItem -> {
                    if (menuItem.getItemId() != getLinkedMenuItem()) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_menu_item_home:
                                Intent home = new Intent(getContext(),
                                        HomeActivity.class);
                                startActivity(home);
                                break;
                            case R.id.nav_menu_item_notification:
                                Intent notification = new Intent(getContext(),
                                        NotificationActivity.class);
                                startActivity(notification);
                                break;
                            case R.id.nav_menu_favorite:
                                Toast.makeText(getContext(),
                                        getString(R.string.dialog_message_favorite),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_menu_item_settings:
                                Intent settings = new Intent(getContext(),
                                        SettingsActivity.class);
                                startActivity(settings);
                                break;
                            case R.id.nav_menu_item_logout:
                                PreferenceHelper.removeOAuthUserKey(getContext());
                                PreferenceHelper.removeOAuthUserKeySecret(getContext());
                                Intent login = new Intent(getContext(),
                                        LoginActivity.class);
                                startActivity(login);
                                break;
                            default:
                                break;
                        }
                    }
                    // Close the navigation drawer when an item is selected.
                    menuItem.setChecked(true);
                    getDrawerLayout().closeDrawers();
                    return true;
                })
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                getDrawerLayout().openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
