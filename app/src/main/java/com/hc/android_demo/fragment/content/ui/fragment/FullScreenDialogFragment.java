package com.hc.android_demo.fragment.content.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hc.android_demo.R;
import com.hc.android_demo.dialog.FullScreenDialog;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.FULL_SCREEN_DIALOG_TEST_FRAGMENT_ID,
        group = FragmentConstants.CUSTOM_VIEW)
public class FullScreenDialogFragment extends Fragment {

    private Button fullScreenDialogBtn;
    private Button fullScreenDialogFragmentBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_full_screen_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fullScreenDialogBtn = view.findViewById(R.id.fullScreenDialog);
        fullScreenDialogFragmentBtn = view.findViewById(R.id.fullScreenDialogFragment);

        fullScreenDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenDialog fullScreenDialog = new FullScreenDialog(getContext(), R.style.custom_dialog);
                fullScreenDialog.show();
            }
        });
    }
}
