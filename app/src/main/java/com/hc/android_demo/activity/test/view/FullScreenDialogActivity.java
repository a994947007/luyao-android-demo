package com.hc.android_demo.activity.test.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hc.android_demo.R;
import com.hc.android_demo.dialog.FullScreenDialog;

public class FullScreenDialogActivity extends AppCompatActivity {

    private Button fullScreenDialogBtn;
    private Button fullScreenDialogFragmentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_dialog);

        fullScreenDialogBtn = findViewById(R.id.fullScreenDialog);
        fullScreenDialogFragmentBtn = findViewById(R.id.fullScreenDialogFragment);

        fullScreenDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenDialog fullScreenDialog = new FullScreenDialog(FullScreenDialogActivity.this, R.style.custom_dialog);
                fullScreenDialog.show();
            }
        });
    }
}