package com.colorworld.manbocash.requestPermission;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.colorworld.manbocash.Const;
import com.colorworld.manbocash.MainActivity;
import com.colorworld.manbocash.R;
import com.colorworld.manbocash.indicator.LoadingIndicator;
import com.colorworld.manbocash.room.MyInfoDatabase;
import com.colorworld.manbocash.room.dao.MyInfoDao;
import com.colorworld.manbocash.room.entity.MyInfoEntity;
import com.colorworld.manbocash.util.AppExecutors;
import com.colorworld.manbocash.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RecommendActivity extends AppCompatActivity {

    private EditText referee;
    private Button recommend_confirm_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activyty_recommend);

        referee = (EditText) findViewById(R.id.referee_et);

        referee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("ios", "setOnEditorActionListener : " + count);
                if (count > 0) {
                    recommend_confirm_btn.setText("확인");
                }else {
                    recommend_confirm_btn.setText("건너뛰기");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        recommend_confirm_btn = (Button) findViewById(R.id.recommend_confirm_btn);
        recommend_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO 추천인 유효성 검사 필요함
                if (referee.getText().length() > 0) {

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    MyInfoDatabase myInfoDatabase = MyInfoDatabase.getInstance(getApplicationContext());
                    MyInfoDao myInfoDao = myInfoDatabase.getMyInfoDao();
                    AppExecutors executors = new AppExecutors();

                    executors.diskIO().execute(() -> {

                        MyInfoEntity infoData = new MyInfoEntity();
                        infoData = myInfoDao.selectInfoDataByUID(currentUser.getUid());

                        String refereeStr = referee.getText().toString();
                        if (refereeStr.length() < 1) {
                            refereeStr = "admin";
                        }

                        infoData.referee = refereeStr;

                        myInfoDao.updateMyInfo(infoData);


                        Util.registerUserDataForFirestore(getApplicationContext(), infoData);

                    });

                }





//                SharedPreferences sharedPreferences = getSharedPreferences("manboData", MODE_PRIVATE);
//                sharedPreferences.edit().putBoolean("firstInstall", false).apply();

                startActivity(new Intent(RecommendActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });


    }
}
