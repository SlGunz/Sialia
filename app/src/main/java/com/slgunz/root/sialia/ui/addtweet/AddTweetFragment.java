package com.slgunz.root.sialia.ui.addtweet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.ui.base.BaseFragment;
import com.slgunz.root.sialia.ui.base.BasePresenter;
import com.slgunz.root.sialia.ui.common.GlideApp;
import com.slgunz.root.sialia.ui.common.ImagePicker;

import java.io.File;

import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddTweetFragment extends BaseFragment implements AddTweetContract.View {

    private static final String TAG = "AddTweetFragment";
    private static final int REQUEST_CAMERA_OR_GALLERY = 1;
    private static final int REQUEST_FROM_CAMERA = 2;
    private static final int REQUEST_FROM_GALLERY = 3;
    @Inject
    AddTweetContract.Presenter mPresenter;

    private Button mNewTweetSendButton;
    private Button mGetImageButton;
    private ImageView mImageView;

    private EditText mNewTweetEditView;

    private Long mRetweetId;
    private String mMediaIds;
    private Uri mImageUri;

    @Inject
    public AddTweetFragment() {
    }

    @Override
    protected BasePresenter getPresenter() {
        return (BasePresenter) mPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.addtweet_frag, container, false);

        mNewTweetEditView = root.findViewById(R.id.addtweet_edit_text);

        mImageView = root.findViewById(R.id.image);
        mImageView.setVisibility(View.GONE);

        setupSendButtonContent(root);
        setupImageButtonContent(root);
        setupFabContent();

        mRetweetId = getPresenter().currentItem() != null ?
                getPresenter().currentItem().getId() : null;

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case REQUEST_CAMERA_OR_GALLERY:
                if (ImagePicker.isImageFromCamera(data)) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, REQUEST_FROM_CAMERA);
                }
                if (ImagePicker.isImageFromGallery(data)) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, REQUEST_FROM_GALLERY);
                }
                break;
            case REQUEST_FROM_GALLERY:
            case REQUEST_FROM_CAMERA:
                mImageUri = data.getData();
                if (mImageUri == null) {
                    String error = getString(R.string.error_message_fetching_image);
                    showErrorMessage(new Throwable(error));
                    return;
                }

                String name = mNewTweetEditView.getText().toString();
                mPresenter.uploadImage(new File(mImageUri.getPath()), name);
                break;
        }
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

    ///////////////////////////////////////////////////////////////////////////
    // setup content
    ///////////////////////////////////////////////////////////////////////////

    private void setupSendButtonContent(View root) {
        mNewTweetSendButton = root.findViewById(R.id.addtweet_send_button);
        mNewTweetSendButton.setOnClickListener(view -> {
            String message = mNewTweetEditView.getText().toString();
            mPresenter.sendTweet(message, mRetweetId, mMediaIds);
            // clear text
            mNewTweetEditView.setText("");
        });
    }

    private void setupImageButtonContent(View root) {
        mGetImageButton = root.findViewById(R.id.get_image_button);
        mGetImageButton.setOnClickListener(view -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            ImagePicker imagePicker = new ImagePicker();
            imagePicker.setTargetFragment(AddTweetFragment.this, REQUEST_CAMERA_OR_GALLERY);
            imagePicker.show(fm, TAG);
        });
    }

    private void setupFabContent() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(
                view -> {

                }
        );
    }

    ///////////////////////////////////////////////////////////////////////////
    // AddTweetContract.View implementation
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void enableSendTweetButton(boolean isEnable) {
        mNewTweetSendButton.setEnabled(isEnable);
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUploadedImage(Long mediaId) {
        mMediaIds = mediaId.toString();
        GlideApp.with(getActivity())
                .load(mImageUri)
                .into(mImageView);
        mImageView.setVisibility(View.VISIBLE);
    }
}
