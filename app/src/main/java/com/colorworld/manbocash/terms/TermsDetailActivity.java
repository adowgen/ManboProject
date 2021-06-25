package com.colorworld.manbocash.terms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.colorworld.manbocash.Const;
import com.colorworld.manbocash.R;

public class TermsDetailActivity extends AppCompatActivity {

    private WebView mWebView;
    private Button acceptBtn, cancelBtn;
    private TextView termsTitle;
    private ProgressBar loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terms_detail);

        loading = (ProgressBar) findViewById(R.id.terms_detail_loading);

        termsTitle = (TextView) findViewById(R.id.terms_detail_title);


        mWebView = (WebView) findViewById(R.id.terms_detail_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
//        mWebView.getSettings().setSupportZoom(true);
//        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setTextZoom(70);

        mWebView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                loading.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.e("ios", "webview error : " + error.getDescription().toString());
                }

            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                loading.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.e("ios", "webview error : " + errorResponse);
                }

            }
        });


        if (getIntent().getExtras() != null) {
            termsTitle.setText(getIntent().getExtras().get("title").toString());
            mWebView.loadUrl(getIntent().getExtras().get("url").toString());
        }



        acceptBtn = (Button) findViewById(R.id.terms_detail_acceptBtn);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_OK);
                finish();

            }
        });

        cancelBtn = (Button) findViewById(R.id.terms_detail_cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_CANCELED);
                finish();
            }
        });



    }




    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.animation_stand, R.anim.animation_leave_right);


    }
}
