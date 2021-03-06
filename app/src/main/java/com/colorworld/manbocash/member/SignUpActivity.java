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


                //????????? ???????????? ??? ?????? Activity


                if (scanErrorForSignUp()) {
                    checkEmailAndNextActivity();
                }


            }
        });

    }

    private void checkEmailAndNextActivity() {
        //????????? ???????????? ??? ?????? Activity
        loadingIndicator.show();
        Call<Duplication> retrofit = NetRetrofit.getInstance().getService().duplicationEmailCheck(email.getText().toString());
        retrofit.enqueue(new Callback<Duplication>() {
            @Override
            public void onResponse(Call<Duplication> call, Response<Duplication> response) {
                loadingIndicator.cancel();
                if (response.isSuccessful()) {
                    Duplication duplication = response.body();
                    Log.e("ios", "????????? ??????????????? : " + response.body());
                    Log.e("ios", "????????? ?????? ?????? : " + duplication.getResult());

//                            goToProfileActivity();
                    if (duplication.getResult().equals("??????")) {

                        Util.showSingleBtnDialog(mActivity, "????????? ????????? ?????????", "?????? ???????????? ????????? ?????????");

                    }else {

                        goToProfileActivity();

                    }



                } else {

                    Util.showSingleBtnDialog(mActivity, "???????????? ??????", "?????? ?????????????????? ????????????\n" + response.errorBody().toString());
                    Log.e("ios", "????????? ?????? ?????? : " + response.errorBody());


                }

            }

            @Override
            public void onFailure(Call<Duplication> call, Throwable t) {

                //TODO : ?????? ??????

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


        if (email.getText().length() == 0 || password.getText().length() == 0 || rePassword.getText().length() == 0) {  //????????? ????????????

            new MaterialDialog.Builder(this)
                    .title("??????")
                    .content("???????????? ??????????????????")
                    .positiveText("??????")
                    .positiveColorRes(R.color.color_common)
                    .show();

        } else if (!Util.validateEmail(email.getText().toString())) { //????????? ?????? ??????

            new MaterialDialog.Builder(this)
                    .title("??????")
                    .content("????????? ????????? ????????????")
                    .positiveText("??????")
                    .positiveColorRes(R.color.color_common)
                    .show();

        } else if (!Util.validatePassword(password.getText().toString())) { //???????????? ?????? ??????

            new MaterialDialog.Builder(this)
                    .title("??????")
                    .content("??????/??????/???????????? ??? 2????????? ????????? 8~15?????? ?????? ??? ????????????")
                    .positiveText("??????")
                    .positiveColorRes(R.color.color_common)
                    .show();

        } else if (!password.getText().toString().equals(rePassword.getText().toString())) {

            new MaterialDialog.Builder(this)
                    .title("??????")
                    .content("???????????? ??????????????? ???????????? ????????????")
                    .positiveText("??????")
                    .positiveColorRes(R.color.color_common)
                    .show();

        } else {
            value = true;
        }


        return value;
    }
}
