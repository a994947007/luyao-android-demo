package com.jny.webview;

interface IWebviewProcessToMainProcessInterface {
    void handleWebCommand(String commandName, String jsonParams);
}