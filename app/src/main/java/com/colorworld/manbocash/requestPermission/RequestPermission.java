package com.colorworld.manbocash.requestPermission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.colorworld.manbocash.MainActivity;
import com.colorworld.manbocash.R;
import com.colorworld.manbocash.model.Member;
import com.colorworld.manbocash.room.MyInfoDatabase;
import com.colorworld.manbocash.room.dao.MyInfoDao;
import com.colorworld.manbocash.room.entity.MyInfoEntity;
import com.colorworld.manbocash.util.AppExecutors;

public class RequestPermission extends AppCompatActivity {
    static int REQUEST_PERMISSION_CODE = 1111;

    public Button confirm_Btn;

    private MyInfoEntity member;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);


        member = (MyInfoEntity) getIntent().getExtras().get("infoData");

        confirm_Btn = (Button) findViewById(R.id.permission_confirm_btn);
        confirm_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(RequestPermission.this, new String[]{
//                Manifest.permission.CAMERA, //사진
//                Manifest.permission.CALL_PHONE,  //통화
//                Manifest.permission.READ_PHONE_STATE,
//				Manifest.permission.WRITE_CALL_LOG,
//				Manifest.permission.PROCESS_OUTGOING_CALLS,
                        Manifest.permission.ACCESS_FINE_LOCATION, //위치
                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.GET_ACCOUNTS, //주소록
//                Manifest.permission.WRITE_CONTACTS,
//                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE, //사진 미디어
                Manifest.permission.WRITE_EXTERNAL_STORAGE
//                Manifest.permission.RECORD_AUDIO, //오디오 녹음
//				Manifest.permission.SEND_SMS, //문자
//				Manifest.permission.RECEIVE_SMS,
//				Manifest.permission.READ_SMS,
//				Manifest.permission.RECEIVE_MMS
                }, REQUEST_PERMISSION_CODE);

            }
        });


        //확인 안하면 어떻게 할

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//				Log.e("permission","permission : " + permissions[0] +
//						"\npermission : " + permissions[1] +
//						"\npermission : " + permissions[2] +
//						"\npermission : " + permissions[3] +
//						"\npermission : " + permissions[4] +
//						"\npermission : " + permissions[5] +
//						"\npermission : " + permissions[6] +
//						"\npermission : " + permissions[7] +
//						"\npermission : " + permissions[8] +
//						"\npermission : " + permissions[9] +
//						"\npermission : " + permissions[10] +
//						"\npermission : " + permissions[11] +
//						"\npermission : " + permissions[12] +
//						"\npermission : " + permissions[13] +
//						"\npermission : " + permissions[14] +
//						"\npermission : " + permissions[15] +
//						"\npermission : " + permissions[16]
//
//						+ "\ngrantResults : " + grantResults[0]
//						+ "\ngrantResults : " + grantResults[1]
//						+ "\ngrantResults : " + grantResults[2]
//						+ "\ngrantResults : " + grantResults[3]
//						+ "\ngrantResults : " + grantResults[4]
//						+ "\ngrantResults : " + grantResults[5]
//						+ "\ngrantResults : " + grantResults[6]
//						+ "\ngrantResults : " + grantResults[7]
//						+ "\ngrantResults : " + grantResults[8]
//						+ "\ngrantResults : " + grantResults[9]
//						+ "\ngrantResults : " + grantResults[10]
//						+ "\ngrantResults : " + grantResults[11]
//						+ "\ngrantResults : " + grantResults[12]
//						+ "\ngrantResults : " + grantResults[13]
//						+ "\ngrantResults : " + grantResults[14]
//						+ "\ngrantResults : " + grantResults[15]
//						+ "\ngrantResults : " + grantResults[16]
//
//				);


        Log.e("ios", "permission : grantResults[0] - " + grantResults[0]);

                if (grantResults[0] != -1) {
                    //TODO 거절

                }


        MyInfoDatabase myInfoDatabase = MyInfoDatabase.getInstance(this);
        MyInfoDao myInfoDao = myInfoDatabase.getMyInfoDao();
        AppExecutors executors = new AppExecutors();

        executors.diskIO().execute(() -> {
            myInfoDao.insertMyInfo(member);
        });


        Log.e("ios", "member.weight ===================================== : " + member.weight );


        if (member.weight != null) {

            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            main.putExtra("infoData", member);
            startActivity(main);

//            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        }else {
            //키 몸무게
            startActivity(new Intent(RequestPermission.this, MyBodysizeActivity.class));
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        }



    }

}
