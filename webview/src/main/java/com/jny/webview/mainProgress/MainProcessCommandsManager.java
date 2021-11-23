package com.jny.webview.mainProgress;

import android.annotation.SuppressLint;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.jny.common.webview.Command;
import com.jny.common.webview.WebViewCallback;
import com.jny.webview.ICallbackMainProgressToWebProgressInterface;
import com.jny.webview.IWebviewProcessToMainProcessInterface;
import com.jny.webview.webviewProgress.bridge.WebViewResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class MainProcessCommandsManager extends IWebviewProcessToMainProcessInterface.Stub{

    private static volatile MainProcessCommandsManager instance;
    private final Map<String, Command> commands = new HashMap<>();
    private static final String TAG = "MainProcessCommandsManager";

    static {
        if (instance == null) {
            synchronized (MainProcessCommandsManager.class) {
                if (instance == null) {
                    instance = new MainProcessCommandsManager();
                }
            }
        }
    }

    private MainProcessCommandsManager() {
        ServiceLoader<Command> loads = ServiceLoader.load(Command.class);
        for (Command cmd : loads) {
            register(cmd.getName(), cmd);
        }
    }

    public void register(String name, Command command) {
        commands.put(name, command);
    }

    public void unregister(String name) {
        commands.remove(name);
    }

    public static MainProcessCommandsManager getInstance() {
        return instance;
    }

    @SuppressLint("LongLogTag")
    protected void executeCommand(String command, Map<String, String> params) {
        Command cmd = commands.get(command);
        if (cmd != null) {
            Log.d(TAG, "exec cmd " + cmd.getName());
            cmd.execute(params, null);
        }
    }

    protected void executeCommand(String command, Map<String, String> params, ICallbackMainProgressToWebProgressInterface webViewResponse) {
        Command cmd = commands.get(command);
        if (cmd != null) {
            cmd.execute(params, new WebViewCallback() {
                @Override
                public void onResult(String response) {
                    try {
                        webViewResponse.onResult(response);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void handleWebCommand(String commandName, String jsonParams) throws RemoteException {
        Map<String, String> params = new Gson().fromJson(jsonParams, Map.class);
        executeCommand(commandName, params);
    }

    @Override
    public void handleWebCommandWithCallback(String commandName, String jsParams, ICallbackMainProgressToWebProgressInterface callback) throws RemoteException {
        Map<String, String> params = new Gson().fromJson(jsParams, Map.class);
        executeCommand(commandName, params, callback);
    }
}
