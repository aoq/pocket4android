package com.hyperionics.PocketTestBrowser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.aokyu.dev.pocket.*;
import com.aokyu.dev.pocket.content.Page;
import com.aokyu.dev.pocket.error.PocketException;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by greg on 7/31/2014.
 * To run this, insert your actual Pocket Consumer Key below,
 * at the line which says:
 *     ConsumerKey consumerKey = new ConsumerKey("Your consumer key");
 * and modify AndroidManifest.xml at the line which says:
 *     <data android:scheme="pocketappXXXXX" />
 * replacing XXXXX with the 5 digit number at the start of your consumer key.
 */
public class PocketActivity extends AuthCallbackActivity {
    static String TAG = "PocketTest";
    ConsumerKey consumerKey = new ConsumerKey("Your consumer key");
    PocketClient pocketClient = new PocketClient(consumerKey);
    AccessToken myAccessToken = null;
    ArrayList<Page> myPages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocket_activity);
        pocketLogin(null);
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

    public void onAuthorizationFinished(final RequestToken requestToken) {
        AndyUtil.bgTask(this, false, null, null, new AndyUtil.OpCallback() {
            @Override
            public boolean runInBg() {
                try {
                    myAccessToken = pocketClient.authenticate(requestToken);
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

    public void pocketLogin(View v) {
        AndyUtil.bgTask(this, false, null, null, new AndyUtil.OpCallback() {
            @Override
            public boolean runInBg() {
                try {
                    pocketClient.authorize(PocketActivity.this);
                } catch (Exception e) {
                    Log.e(TAG, "Pocket auth exception: " + e);
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