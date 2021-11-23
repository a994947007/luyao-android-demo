package com.jny.webview.webviewProgress.bridge;

import android.os.RemoteException;

import com.jny.webview.ICallbackMainProgressToWebProgressInterface;

public class WebViewResponse extends ICallbackMainProgressToWebProgressInterface.Stub {
    private WebViewResponseCallback callback;
    public WebViewResponse(WebViewResponseCallback callback) {
        this.callback = callback;
    }
    @Override
    public void onResult(String response) throws RemoteException {
        callback.onResult(response);
    }
}
