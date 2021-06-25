package com.colorworld.manbocash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.colorworld.manbocash.tutorial.tutorials.TutorialSupportActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("isFirstRun", MODE_PRIVATE);
        Log.e("ios", "isFirst running : " + sp.getBoolean("running", false));
        if (!sp.getBoolean("running", false)) {
            Log.e("ios", "LaunchActivity - tutorial");
            TutorialSupportActivity.start(this);
        }else {
            Log.e("ios", "LaunchActivity - mainActivity");
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
        }


    }
}
