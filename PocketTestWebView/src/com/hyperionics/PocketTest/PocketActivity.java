package com.hyperionics.PocketTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.aokyu.dev.pocket.*;
import com.aokyu.dev.pocket.content.Page;
import com.aokyu.dev.pocket.error.InvalidRequestException;
import com.aokyu.dev.pocket.error.PocketException;
import com.aokyu.dev.pocket.http.*;
import com.aokyu.dev.pocket.util.PocketUtils;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by greg on 7/31/2014.
 * To run this, insert your actual Pocket Consumer Key below,
 * at the line which says:
 *     ConsumerKey consumerKey = new ConsumerKey("Your consumer key");
 */
public class PocketActivity extends Activity implements AuthorizationCallback {
    private ConsumerKey mConsumerKey = new ConsumerKey("Your consumer key");
    private static RequestToken mRequestToken;

    PocketClient pocketClient = new PocketClient(mConsumerKey);
    AccessToken myAccessToken = null;
    ArrayList<Page> myPages = null;
    private static PocketActivity current = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocket_activity);
        current = this;
        pocketLogin(null);
    }

    static PocketActivity getCurrent() {
        return current;
    }

    @Override
    public void onStart() {
        overridePendingTransition(0, 0);
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);
    }

    public void onRetrieve(View v) {
        if (myAccessToken == null) {
            Lt.alert(this, "Please log-on to Pocket first.");
            return;
        }
        AndyUtil.bgTask(this, true, "PocketTest", "Retrieving items...", new AndyUtil.OpCallback() {
            @Override
            public boolean runInBg() {
                try {
                    myPages = new ArrayList<Page>();
                    int count = 10;
                    int offset = 0;
                    RetrieveResponse response;
                    do {
                        RetrieveRequest rrq = new RetrieveRequest.Builder()
                                // .setSince(lastRequest)
                                // .setTag("tts_tag")
                                .setContentType("article")
                                .setDetailType("simple") // or "complete"
                                .setCount(count)
                                .setOffset(offset)
                                .build();
                        response = pocketClient.retrieve(myAccessToken, rrq);
                        if (response == null)
                            break;
                        myPages.addAll(response.getItems());
                        offset += response.size();
                    } while (response.size() == count);
                } catch (PocketException pe) {
                    // ignore and return true - happens if there are no more articles (pages) to return from Pocket
                } catch (Exception e) {
                    Lt.e("Retrieve exception: " + e);
                    e.printStackTrace();
                    return false;
                }
                return true;
            }

            @Override
            public void onFinished(boolean result) {
                if (myPages != null) {
                    Lt.d("RetrieveResponse size: " + myPages.size());
                    for (Page p : myPages) {
                        Lt.d(p.getGivenUrl() + " : " + p.getResolvedTitle());
                    }
                }
            }
        });
    }

    public void onAuthorizationFinished(RequestToken unused) {
        AndyUtil.bgTask(this, false, null, null, new AndyUtil.OpCallback() {
            @Override
            public boolean runInBg() {
                try {
                    myAccessToken = pocketClient.authenticate(mRequestToken);
                } catch (Exception e) {
                    myAccessToken = null;
                    Lt.e("Exception: " + e);
                    e.printStackTrace();
                    return false;
                }
                return true;
            }

            @Override
            public void onFinished(boolean result) {

            }
        });
    }

    @Override
    public void onRequested(ConsumerKey consumerKey, RequestToken requestToken) {
        mRequestToken = requestToken;
    }

    @Override
    public Activity onRequestContinued() {
        String url = PocketServer.getRedirectUrl(mConsumerKey, mRequestToken);

        // Try first with headless browser...
        android.webkit.CookieSyncManager.createInstance(this);
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);
        String cookies = android.webkit.CookieManager.getInstance().getCookie(url);
        if (cookies != null && !cookies.equals("")) {
            try {
                HttpClient client = new HttpClient();
                URL requestUrl = new URL(url);
                HttpHeaders headers = new HttpHeaders();
                headers.put(HttpHeader.HOST, requestUrl.getHost());
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                headers.put("X-Accept", "application/x-www-form-urlencoded");
                headers.put("Cookie", cookies);
                HttpParameters parameters = new HttpParameters();
                HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, parameters);

                HttpResponse response = client.execute(request).followRedirects(false);
                if (response.getStatusCode() == HttpURLConnection.HTTP_MOVED_TEMP) { // 302, redirect
                    String loc = response.getHeaderField("Location");
                    if (loc != null && loc.startsWith(PocketUtils.getAppId(mConsumerKey))) {
                        // All is OK, we are logged in
                        onAuthorizationFinished(null);
                        return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(this, SimpleBrowser.class);
        intent.putExtra("url", url);
        startActivity(intent);

        return null;
    }

    public void pocketLogin(View v) {
        AndyUtil.bgTask(this, false, null, null, new AndyUtil.OpCallback() {
            @Override
            public boolean runInBg() {
                try {
                    pocketClient.authorize(PocketActivity.this);
                } catch (InvalidRequestException ire) {
                    // ignore, we return null in onRequestContinued() and do our own URL handling.
                } catch (Exception e) {
                    Lt.e("Pocket auth exception: " + e);
                    e.printStackTrace();
                    return false;
                }
                return true;
            }

            @Override
            public void onFinished(boolean result) { }
        });

    }
}