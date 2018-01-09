package com.slgunz.root.sialia.ui.addtweet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.slgunz.root.sialia.R;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddTweetFragment extends DaggerFragment implements AddTweetContract.View {

    @Inject
    AddTweetContract.Presenter mPresenter;

    private Button mNewTweetSendButton;

    private EditText mNewTweetEditView;

    @Inject
    public AddTweetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.addtweet_frag, container, false);

        mNewTweetEditView = root.findViewById(R.id.addtweet_edit_text);

        mNewTweetSendButton = root.findViewById(R.id.addtweet_send_button);
        mNewTweetSendButton.setOnClickListener(view -> {
            String message = mNewTweetEditView.getText().toString();
            mPresenter.sendOnlyTextTweet(message);
            // clear text
            mNewTweetEditView.setText("");
        });

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(
                view -> {
                    String message = mNewTweetEditView.getText().toString();
                    if(message.isEmpty()){
                        return;
                    }
                    mPresenter.sendOnlyTextTweet(message);
                }
        );

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
    public void enableSendTweetButton(boolean isEnable) {
        mNewTweetSendButton.setEnabled(isEnable);
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
