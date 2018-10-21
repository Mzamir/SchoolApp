package com.seamlabs.BlueRide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPolicy extends AppCompatActivity {

    @BindView(R.id.privacy_web_view)
    WebView webView;

    @BindView(R.id.web_view_progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl("https://www.blueride.co/terms-and-privacy-policy");
    }
}
