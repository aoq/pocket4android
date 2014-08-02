package com.hyperionics.PocketTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by greg on 11/5/13.
 */
public class SimpleBrowser extends Activity {
    protected WebView webView;

    private class SimpleWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url == null || !url.startsWith("pocketapp")) {
                view.loadUrl(url);
            } else {
                SimpleBrowser.this.finish();
                PocketActivity.getCurrent().onAuthorizationFinished(null);
            }
            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);
        webView = (WebView) findViewById(R.id.webkit);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new SimpleWebViewClient());
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);

        Intent intent = getIntent();
        if (intent != null) {
            String sUrl = intent.getStringExtra("url");
            if (sUrl != null)
                webView.loadUrl(sUrl);
        }
    }
}