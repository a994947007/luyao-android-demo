package com.jny.webview;

import com.jny.webview.ICallbackMainProgressToWebProgressInterface;

interface IWebviewProcessToMainProcessInterface {
    void handleWebCommand(String commandName, String jsonParams);

    void handleWebCommandWithCallback(String commandName, String jsParams, in ICallbackMainProgressToWebProgressInterface callback);
}