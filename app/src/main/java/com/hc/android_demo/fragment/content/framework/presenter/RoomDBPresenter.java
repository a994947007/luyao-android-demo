package com.hc.android_demo.fragment.content.framework.presenter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.room.Room;

import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.framework.db.User;
import com.hc.android_demo.fragment.content.framework.db.UserDB;
import com.hc.android_demo.fragment.content.framework.db.UserDao;
import com.hc.base.AppEnvironment;
import com.hc.support.mvps.Presenter;
import com.hc.support.rxJava.observable.Observable;
import com.hc.support.rxJava.observer.Consumer;
import com.hc.support.rxJava.schedule.Schedules;
import com.hc.support.util.TextUtils;
import com.hc.util.ThreadUtils;
import com.hc.util.ToastUtils;
import com.hc.util.Utils;

import java.util.concurrent.Callable;

public class RoomDBPresenter extends Presenter {

    private EditText mEditText;
    private Button mInsertBtn;
    private Button mUpdateBtn;
    private Button mDeleteBtn;
    private Button mQueryBtn;

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    public void doBindView(View rootView) {
        super.doBindView(rootView);
        mEditText = rootView.findViewById(R.id.edit_text);
        mInsertBtn = rootView.findViewById(R.id.insert_btn);
        mUpdateBtn = rootView.findViewById(R.id.update_btn);
        mDeleteBtn = rootView.findViewById(R.id.delete_btn);
        mQueryBtn = rootView.findViewById(R.id.query_btn);
    }

    @Override
    protected void onBind() {
        super.onBind();
        mInsertBtn.setOnClickListener(v -> {
            String text = mEditText.getText().toString();
            if (TextUtils.isEmpty(text)) {
                return;
            }
            User user = new User();
            user.username = text;
            // Log.d("ROOMDB", "" + UserDB.getInstance(AppEnvironment.getAppContext()).userDao());
            ThreadUtils.runOnNewThread(new Runnable() {
                @Override
                public void run() {
                    UserDB.getInstance(getActivity()).userDao().insertUser(user);
                }
            });
        });

        mQueryBtn.setOnClickListener(v -> {
            String text = mEditText.getText().toString();
            if (TextUtils.isEmpty(text)) {
                return;
            }
            Observable.fromCallable(() -> UserDB.getInstance(getActivity()).userDao().findUserByUsername(text))
            .subscribeOn(Schedules.IO)
            .observeOn(Schedules.MAIN)
            .subscribe(user -> ToastUtils.show(user.username));
        });
    }
}
