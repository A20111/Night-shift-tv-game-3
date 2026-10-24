package com.nightshift.tv;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends Activity {
    private WebView web;
    private GameServer server;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try {
            server = new GameServer(this, 8080);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        web = new WebView(this);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        web.setBackgroundColor(0xFF000000);
        web.setWebChromeClient(new WebChromeClient());
        setContentView(web);
        web.loadUrl("http://127.0.0.1:8080/tv");
    }

    @Override
    protected void onDestroy() {
        if (server != null) server.stop();
        if (web != null) web.destroy();
        super.onDestroy();
    }
}
