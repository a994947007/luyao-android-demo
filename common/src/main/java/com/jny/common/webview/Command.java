package com.jny.common.webview;

import java.util.Map;

public interface Command {
    void execute(Map<String, String> params);
    String getName();
}
