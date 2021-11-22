package com.jny.webview.webviewProgress.webclient;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hc.base.AppEnvironment;
import com.jny.webview.IWebviewProcessToMainProcessInterface;
import com.jny.webview.mainProgress.MainProcessCommandService;

public class WebViewProcessCommandDispatcher {
    private static volatile WebViewProcessCommandDispatcher instance = null;
    private MainProgressConnection connection = new MainProgressConnection();

    static {
        if (instance == null) {
            synchronized (WebViewProcessCommandDispatcher.class) {
                if (instance == null) {
                    instance = new WebViewProcessCommandDispatcher();
                }
            }
        }
    }

    public static WebViewProcessCommandDispatcher getInstance() {
        return instance;
    }

    /**
     * webView在另外一个进程，通过Service，调用主线程的方法
     */
    public void initAidlConnection() {
        Intent intent = new Intent(AppEnvironment.getAppContext(), MainProcessCommandService.class);
        AppEnvironment.getAppContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);   // 在Main线程启动
    }

    @SuppressLint("LongLogTag")
    public void executeCommand(String commandName, String jsonParams) {
        connection.executeCommand(commandName, jsonParams);
    }

    private static class MainProgressConnection implements ServiceConnection {
        private IWebviewProcessToMainProcessInterface mainProcessInterface;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mainProcessInterface = IWebviewProcessToMainProcessInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mainProcessInterface = null;
        }

        @Override
        public void onBindingDied(ComponentName name) {
            mainProcessInterface = null;
        }

        public void executeCommand(String command, String jsonParams) {
            if (mainProcessInterface != null) {
                try {
                    mainProcessInterface.handleWebCommand(command, jsonParams);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
