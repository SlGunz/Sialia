package com.slgunz.root.sialia.ui.addtweet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.di.ActivityInjector;
import com.slgunz.root.sialia.util.ActivityUtils;

import javax.inject.Inject;

public class AddTweetActivity extends AppCompatActivity {

    @Inject
    AddTweetFragment mInjectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityInjector.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtweet_act);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AddTweetFragment addTweetFragment =
                (AddTweetFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (addTweetFragment == null) {
            addTweetFragment = mInjectedFragment;
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), addTweetFragment, R.id.contentFrame);
        }
    }

}
