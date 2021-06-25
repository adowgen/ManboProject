package com.colorworld.manbocash;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.kakao.sdk.common.KakaoSdk;

public class ManboApplication extends Application {
    private static ManboApplication self;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("ios", "ManboApplication");
        self = this;
        FirebaseApp.initializeApp(this);//파이어베이스 초기화
        KakaoSdk.init(this, "33e11a8c137f0f8e8bca14987bbb81b7");//카카오 초기화

    }
}
