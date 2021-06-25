package com.colorworld.manbocash.mainFragments;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.colorworld.manbocash.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class storeFragment extends Fragment {
    public static storeFragment staticStoreContext;

    public TextView title;

    public WebView mStoreWebView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_store, container, false);

        staticStoreContext = this;

        mStoreWebView = (WebView) rootView.findViewById(R.id.store_webview);

        mStoreWebView.clearCache(true);

        mStoreWebView.getSettings().setJavaScriptEnabled(true);
        mStoreWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        mStoreWebView.getSettings().setGeolocationEnabled(true);
        mStoreWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mStoreWebView.getSettings().setSupportZoom(false);
        mStoreWebView.getSettings().setBuiltInZoomControls(false);
        mStoreWebView.getSettings().setUseWideViewPort(true);
        mStoreWebView.getSettings().setLoadWithOverviewMode(true);
        mStoreWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mStoreWebView.getSettings().setDomStorageEnabled(true);
        mStoreWebView.setWebChromeClient(new WebChromeClient());
        mStoreWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.e("ios", "onPageStarted : " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Log.e("ios", "onPageFinished : " + url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                Log.e("ios", "onReceivedError : " + error);
            }


        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStoreWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mStoreWebView.loadUrl("https://www.manbocash.com/" + currentUser.getUid());
//        mStoreWebView.loadUrl("https://www.naver.com");


        title = (TextView) rootView.findViewById(R.id.store_title);
        title.setText(currentUser.getUid());


        return rootView;
    }

    public boolean canGoBack() {
        if (mStoreWebView.canGoBack()) {
            return true;
        }else {
            return false;
        }
    }

    public void goBack() {
        mStoreWebView.goBack();
    }

}
