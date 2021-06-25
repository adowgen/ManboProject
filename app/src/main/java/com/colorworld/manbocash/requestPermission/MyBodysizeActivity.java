package com.colorworld.manbocash.requestPermission;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.colorworld.manbocash.Const;
import com.colorworld.manbocash.MainActivity;
import com.colorworld.manbocash.R;
import com.colorworld.manbocash.room.MyInfoDatabase;
import com.colorworld.manbocash.room.dao.MyInfoDao;
import com.colorworld.manbocash.room.entity.MyInfoEntity;
import com.colorworld.manbocash.tutorial.tutorials.TutorialSupportActivity;
import com.colorworld.manbocash.util.AppExecutors;
import com.colorworld.manbocash.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyBodysizeActivity extends AppCompatActivity {

    private EditText height, weight;
    private Button body_confirm_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_body_size);


        height = (EditText) findViewById(R.id.height_et);
//        height.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                height.setText(s + "   cm");
//            }
//        });

        weight = (EditText) findViewById(R.id.weight_et);
//        weight.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                weight.setText(s + "   kg");
//            }
//        });


        body_confirm_btn = (Button) findViewById(R.id.body_confirm_btn);
        body_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                MyInfoDatabase myInfoDatabase = MyInfoDatabase.getInstance(getApplicationContext());
                MyInfoDao myInfoDao = myInfoDatabase.getMyInfoDao();
                AppExecutors executors = new AppExecutors();

                executors.diskIO().execute(() -> {

                    MyInfoEntity infoData = new MyInfoEntity();
                    infoData = myInfoDao.selectInfoDataByUID(currentUser.getUid());


                    int heightInt = 0;
                    int weightInt = 0;

                    if (height.getText().toString().length() <= 0 || height.getText() == null) {
                        heightInt = 1;
                    } else {

                        heightInt = Integer.parseInt(height.getText().toString());

                        if (heightInt > 230 || heightInt < 100) {
                            heightInt = 1;
                            //너무 크거나 작습니다
//                            Toast.makeText(getApplicationContext(), "조금 더 정확한 값을 입력해 주시면\\n칼로리 및 거리등의 데이터도 더 정확해 집니다", Toast.LENGTH_LONG).show();

//                            executors.mainThread().execute(() -> {
//                                new MaterialDialog.Builder(getApplicationContext())
//                                        .title("알림")
//                                        .content("조금 더 정확한 값을 입력해 주시면\n칼로리 및 거리등의 데이터도 더 정확해 집니다 ")
//                                        .positiveText("확인")
//                                        .positiveColorRes(R.color.color_common)
//                                        .negativeColorRes(R.color.color_common)
//                                        .show();
//                            });

//                            return;
                        }

                    }

                    if (weight.getText().toString().length() <= 0) {
                        weightInt = 1;
                    } else {

                        weightInt = Integer.parseInt(weight.getText().toString());

                        if (weightInt > 330 || weightInt < 30) {
                            weightInt = 1;
                            //너무 크거나 작습니다
//                            executors.mainThread().execute(() -> {
//                                new MaterialDialog.Builder(getApplicationContext())
//                                        .title("알림")
//                                        .content("조금 더 정확한 값을 입력해 주시면\n칼로리 및 거리등의 데이터도 더 정확해 집니다 ")
//                                        .positiveText("확인")
//                                        .positiveColorRes(R.color.color_common)
//                                        .negativeColorRes(R.color.color_common)
//                                        .show();
//                            });

//                            Toast.makeText(getApplicationContext(), "조금 더 정확한 값을 입력해 주시면\\n칼로리 및 거리등의 데이터도 더 정확해 집니다", Toast.LENGTH_LONG).show();
//
//                            return;
                        }
                    }


                    infoData.height = String.valueOf(heightInt);
                    infoData.weight = String.valueOf(weightInt);

                    myInfoDao.updateMyInfo(infoData);


                    Util.registerUserDataForFirestore(MyBodysizeActivity.this, infoData);


                    if (!infoData.registerby.equals(Const.RegisteredBy.MANBOCASH)) { /*에메일로그인은 최초가입때 추천인 입력함*/
                        startActivity(new Intent(MyBodysizeActivity.this, RecommendActivity.class));
                        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                    } else {
                        startActivity(new Intent(MyBodysizeActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }

                });


                //TODO 로그아웃 후 다시 로그인시 or 앱을 삭제후 재설치시 추천인등록을 했었으면 화면전환을 메인으로
//                SharedPreferences sharedPreferences = getSharedPreferences("manboData", MODE_PRIVATE);
//                String loginType = sharedPreferences.getString("loginType", Const.RegisteredBy.MANBOCASH);



            }
        });


    }
}
