package com.colorworld.manbocash.member;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.colorworld.manbocash.R;
import com.colorworld.manbocash.indicator.LoadingIndicator;
import com.colorworld.manbocash.model.Duplication;
import com.colorworld.manbocash.retrofit.NetRetrofit;
import com.colorworld.manbocash.util.Util;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {

    private Activity mActivity;
    private EditText email, password, rePassword;
    private Button confirmBtn;

    private LoadingIndicator loadingIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mActivity = this;

        loadingIndicator = new LoadingIndicator(this);
        loadingIndicator.setCancelable(false);

        email = (EditText) findViewById(R.id.signUp_email);
        password = (EditText) findViewById(R.id.signUp_pw_1);
        rePassword = (EditText) findViewById(R.id.signUp_pw_2);
        rePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("ios", "KEYCODE_ENTER : " + actionId + ",  " + EditorInfo.IME_ACTION_DONE);

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (scanErrorForSignUp()) {
                        checkEmailAndNextActivity();
                    }
                }

                return false;
            }
        });

        confirmBtn = (Button) findViewById(R.id.signUp_confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //아이디 중복검사 후 다음 Activity


                if (scanErrorForSignUp()) {
                    checkEmailAndNextActivity();
                }


            }
        });

    }

    private void checkEmailAndNextActivity() {
        //아이디 중복검사 후 다음 Activity
        loadingIndicator.show();
        Call<Duplication> retrofit = NetRetrofit.getInstance().getService().duplicationEmailCheck(email.getText().toString());
        retrofit.enqueue(new Callback<Duplication>() {
            @Override
            public void onResponse(Call<Duplication> call, Response<Duplication> response) {
                loadingIndicator.cancel();
                if (response.isSuccessful()) {
                    Duplication duplication = response.body();
                    Log.e("ios", "이메일 리턴데이터 : " + response.body());
                    Log.e("ios", "이메일 중복 검사 : " + duplication.getResult());

//                            goToProfileActivity();
                    if (duplication.getResult().equals("중복")) {

                        Util.showSingleBtnDialog(mActivity, "중복된 이메일 입니다", "다른 이메일을 사용해 주세요");

                    }else {

                        goToProfileActivity();

                    }



                } else {

                    Util.showSingleBtnDialog(mActivity, "네트워크 에러", "다시 시도해주시기 바랍니다\n" + response.errorBody().toString());
                    Log.e("ios", "이메일 중복 에러 : " + response.errorBody());


                }

            }

            @Override
            public void onFailure(Call<Duplication> call, Throwable t) {

                //TODO : 에러 얼럿

            }
        });


    }


    private void goToProfileActivity() {

        Intent intent = new Intent(getApplicationContext(), ProfileRegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("email", email.getText().toString());
        intent.putExtra("password", password.getText().toString());
        startActivity(intent);
        overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter);

    }

    private boolean scanErrorForSignUp() {
        boolean value = false;


        if (email.getText().length() == 0 || password.getText().length() == 0 || rePassword.getText().length() == 0) {  //입력을 안했을때

            new MaterialDialog.Builder(this)
                    .title("알림")
                    .content("빈칸없이 입력해주세요")
                    .positiveText("확인")
                    .positiveColorRes(R.color.color_common)
                    .show();

        } else if (!Util.validateEmail(email.getText().toString())) { //이메일 형식 검사

            new MaterialDialog.Builder(this)
                    .title("알림")
                    .content("이메일 형식이 아닙니다")
                    .positiveText("확인")
                    .positiveColorRes(R.color.color_common)
                    .show();

        } else if (!Util.validatePassword(password.getText().toString())) { //패스워드 형식 검사

            new MaterialDialog.Builder(this)
                    .title("알림")
                    .content("영문/숫자/특수문자 중 2개이상 조합한 8~15자로 만들 수 있습니다")
                    .positiveText("확인")
                    .positiveColorRes(R.color.color_common)
                    .show();

        } else if (!password.getText().toString().equals(rePassword.getText().toString())) {

            new MaterialDialog.Builder(this)
                    .title("알림")
                    .content("입력하신 비밀번호가 일치하지 않습니다")
                    .positiveText("확인")
                    .positiveColorRes(R.color.color_common)
                    .show();

        } else {
            value = true;
        }


        return value;
    }
}
