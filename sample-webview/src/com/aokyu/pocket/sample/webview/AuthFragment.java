package com.aokyu.pocket.sample.webview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthFragment extends Fragment {

    public static final String TAG = AuthFragment.class.getSimpleName();

    public final class Argument {
        private Argument() {}

        public static final String URL = "argument_url";
    }

    private AuthListener mListener;

    private WebView mWebView;

    public AuthFragment() {}

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AuthListener) {
            mListener = (AuthListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.panel_browser, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView = (WebView) view.findViewById(R.id.auth_view);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        WebViewClient client = new AuthClient(mListener);
        mWebView.setWebViewClient(client);
        CookieManager.getInstance().setAcceptCookie(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            String url = args.getString(Argument.URL);
            if (!TextUtils.isEmpty(url)) {
                loadUrl(url);
                return;
            }
        }

        throw new IllegalArgumentException("url must be set as argument");
    }

    private void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWebView.destroy();
        mWebView = null;
    }

    private static final class AuthClient extends WebViewClient {

        private static final String CALLBACK_SCHEME = "pocketapp";

        private AuthListener mListener;

        public AuthClient(AuthListener l) {
            mListener = l;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (TextUtils.isEmpty(url) || !url.startsWith(CALLBACK_SCHEME)) {
                webView.loadUrl(url);
            } else {
                if (mListener != null) {
                    mListener.onAuthorizationFinished();
                }
            }
            return true;
        }
    }

    public interface AuthListener {
        void onAuthorizationFinished();
    }
}
