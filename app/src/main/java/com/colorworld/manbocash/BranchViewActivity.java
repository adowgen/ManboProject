package com.colorworld.manbocash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.colorworld.manbocash.requestPermission.RequestPermission;
import com.colorworld.manbocash.room.entity.MyInfoEntity;

public class BranchViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MyInfoEntity myInfo = (MyInfoEntity) getIntent().getExtras().get("infoData");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(this, RequestPermission.class);
                intent.putExtra("infoData", myInfo);
                startActivity(intent);

            }else {

                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                main.putExtra("infoData", myInfo);
                startActivity(main);

            }
        }

    }


}
