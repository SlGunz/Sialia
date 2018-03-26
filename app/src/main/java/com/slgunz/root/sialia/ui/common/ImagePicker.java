package com.slgunz.root.sialia.ui.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.slgunz.root.sialia.R;


public class ImagePicker extends DialogFragment {

    private static final String EXTRA_RESULT = "com.slgunz.root.sialia.ui.common.Result";
    private static final int GALLERY_ID = 1;
    private static final int CAMERA_ID = 2;

    public static boolean isImageFromGallery(Intent intent) {
        if (intent != null) {
            return intent.getIntExtra(EXTRA_RESULT, 0) == GALLERY_ID;
        }
        return false;
    }

    public static boolean isImageFromCamera(Intent intent) {
        if (intent != null) {
            return intent.getIntExtra(EXTRA_RESULT, 0) == CAMERA_ID;
        }
        return false;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.imagedialog_frag, null);

        Button buttonGallery = view.findViewById(R.id.gallery);
        buttonGallery.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_RESULT, GALLERY_ID);
            setResultCode(intent);
        });

        Button buttonCamera = view.findViewById(R.id.camera);
        buttonCamera.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_RESULT, CAMERA_ID);
            setResultCode(intent);
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.layout_title_from))
                .create();
    }

    private void setResultCode(Intent intent) {
        Fragment fragment = getTargetFragment();
        if (fragment == null) dismiss();
        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
