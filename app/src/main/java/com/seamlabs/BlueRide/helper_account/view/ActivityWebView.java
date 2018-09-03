package com.seamlabs.BlueRide.helper_account.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.seamlabs.BlueRide.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.BASE_URL;
import static com.seamlabs.BlueRide.utils.Constants.FORGET_PASSWORD_URL;
import static com.seamlabs.BlueRide.utils.Constants.URL_TO_LOAD_IN_WEBVIEW;

public class ActivityWebView extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;

    String urlToLoad = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
//        try {
//            urlToLoad = getIntent().getStringExtra(URL_TO_LOAD_IN_WEBVIEW);
//        } catch (Exception e) {
//            Log.i("WebView", "Exception " + e.getMessage().toString());
//        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(FORGET_PASSWORD_URL);
//        if (urlToLoad != null)
    }
}
