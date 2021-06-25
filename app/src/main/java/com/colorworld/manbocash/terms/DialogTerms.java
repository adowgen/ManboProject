package com.colorworld.manbocash.terms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.colorworld.manbocash.Const;
import com.colorworld.manbocash.MainActivity;
import com.colorworld.manbocash.R;
import com.colorworld.manbocash.indicator.LoadingIndicator;
import com.colorworld.manbocash.member.SignUpActivity;
import com.colorworld.manbocash.model.Duplication;
import com.colorworld.manbocash.model.Member;
import com.colorworld.manbocash.requestPermission.RequestPermission;
import com.colorworld.manbocash.retrofit.NetRetrofit;
import com.colorworld.manbocash.room.entity.MyInfoEntity;
import com.colorworld.manbocash.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

public class DialogTerms extends AppCompatActivity {
    public static DialogTerms staticDialogTermsContext;

    int loginType;
    private String referCode;

    private FrameLayout privacyBtn, locationBtn, pushBtn;
    private ImageView privacyCheck, locationCheck, pushCheck, nextBtn, allBtn;
    private TextView nextBtnTitle, GoneTextView;

    private LoadingIndicator loadingIndicator;


    boolean isCheck1 = false, isCheck2 = false, isCheck3 = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_terms);
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        staticDialogTermsContext = this;


        loadingIndicator = new LoadingIndicator(this);
        loadingIndicator.setCancelable(false);


        loginType = getIntent().getExtras().getInt("loginType");


        checkDeviceSizeAndDiappearText();


        privacyCheck = (ImageView) findViewById(R.id.dialog_terms_check1);
        locationCheck = (ImageView) findViewById(R.id.dialog_terms_check3);
        pushCheck = (ImageView) findViewById(R.id.dialog_terms_check5);

        nextBtn = (ImageView) findViewById(R.id.dialog_terms_nextBtn);
        nextBtn.setEnabled(false);
        nextBtnTitle = (TextView) findViewById(R.id.dialog_terms_btn_title);


        privacyBtn = (FrameLayout) findViewById(R.id.dialog_terms_frame1);
        privacyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsDetailActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", "개인정보 취급방침 동의");
                intent.putExtra("url", Const.TermsURL.PRIVACY_URL);
                startActivityForResult(intent, Const.TermsType.PRIVACY);
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_stand);
            }
        });

        locationBtn = (FrameLayout) findViewById(R.id.dialog_terms_frame2);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsDetailActivity.class);
                intent.putExtra("title", "위치기반 서비스 약관동의");
                intent.putExtra("url", Const.TermsURL.LOCATION_URL);
                startActivityForResult(intent, Const.TermsType.LOCATION);
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_stand);
            }
        });


        pushBtn = (FrameLayout) findViewById(R.id.dialog_terms_frame3);
        pushBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsDetailActivity.class);
                intent.putExtra("title", "마케팅 활용 앱푸시 수신동의");
                intent.putExtra("url", Const.TermsURL.PUSH_URL);
                startActivityForResult(intent, Const.TermsType.PUSH);
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_stand);
            }
        });


        allBtn = (ImageView) findViewById(R.id.dialog_terms_allBtn);
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheck1 = true;
                isCheck2 = true;
                isCheck3 = true;
                privacyCheck.setImageDrawable(getResources().getDrawable(R.drawable.vec_checkbox_fill_circle_outline));
                locationCheck.setImageDrawable(getResources().getDrawable(R.drawable.vec_checkbox_fill_circle_outline));
                pushCheck.setImageDrawable(getResources().getDrawable(R.drawable.vec_checkbox_fill_circle_outline));
                nextBtn.setEnabled(true);
                nextBtnTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.home_cash_));
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //로그인 방식에 따라

                if (loginType == Const.LoginType.PASSWORD) {
                    //회원가입

                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                } else {

                    loadingIndicator.show();

                    switch (loginType) {
                        case Const.LoginType.FACEBOOK:
                            loginWithAuthenticReferCode(Const.RegisteredBy.FACEBOOK);
                            break;
                        case Const.LoginType.GOOGLE:
                            loginWithAuthenticReferCode(Const.RegisteredBy.GOOGLE);
                            break;
                        case Const.LoginType.KAKAO:
                            loginWithAuthenticReferCode(Const.RegisteredBy.KAKAO);
                            break;

                    }


//                    setResult(RESULT_OK);
//                    finish();

                }

            }
        });


    }


    public void loginWithAuthenticReferCode(String registerType) {
        referCode = "";
        referCode = Util.makeReferCode();

        Call<Duplication> retrofit = NetRetrofit.getInstance().getService().duplicationReferCodeCheck(referCode);
        String finalReferCode = referCode;
        retrofit.enqueue(new Callback<Duplication>() {
            @Override
            public void onResponse(Call<Duplication> call, retrofit2.Response<Duplication> response) {

                if (response.isSuccessful()) {
                    Duplication duplication = response.body();
                    Log.e("ios", "나의 추천코드 중복 검사 : " + duplication.getResult());

//                            goToProfileActivity();
                    if (duplication.getResult().toString().equals("중복")) {

                        loginWithAuthenticReferCode(registerType);


                    } else { //중복아님

                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = firebaseAuth.getCurrentUser();


                        Date lastSignDate = new Date(user.getMetadata().getLastSignInTimestamp());
                        Date creationDate = new Date(user.getMetadata().getCreationTimestamp());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String lastSignInTime = simpleDateFormat.format(lastSignDate);
                        String creationTime = simpleDateFormat.format(creationDate);


                        MyInfoEntity myInfo = new MyInfoEntity();
                        myInfo.email = user.getEmail();
                        myInfo.uid = user.getUid();
                        myInfo.refercode = referCode;
                        myInfo.lastSignInTimestamp = lastSignInTime;
                        myInfo.creationTimestamp = creationTime;
                        myInfo.nickname = user.getDisplayName();
                        myInfo.photourl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";
                        myInfo.registerby = registerType;


                        Intent intent = new Intent(getApplicationContext(), RequestPermission.class);
                        intent.putExtra("infoData", myInfo);
                        startActivity(intent);


//                        switch (registerType) {
//                            case Const.RegisteredBy.FACEBOOK:
//                                registerUserDataForFirestore(user, Const.RegisteredBy.FACEBOOK, finalReferCode);
//                                break;
//                            case Const.RegisteredBy.GOOGLE:
//                                registerUserDataForFirestore(user, Const.RegisteredBy.GOOGLE, finalReferCode);
//                                break;
//                            case Const.RegisteredBy.KAKAO:
//                                registerUserDataForFirestore(user, Const.RegisteredBy.KAKAO, finalReferCode);
//                                break;
//                            case Const.RegisteredBy.MANBOCASH:
//                                registerUserDataForFirestore(user, Const.RegisteredBy.MANBOCASH, finalReferCode);
//                                break;
//
//                        }

                    }


                } else {

                    Util.showSingleBtnDialog(getApplicationContext(), "네트워크 에러", "다시 시도해주시기 바랍니다\n" + response.errorBody().toString());
                    Log.e("ios", "나의 추천코드 중복 에러 : " + response.errorBody());

                }

            }

            @Override
            public void onFailure(Call<Duplication> call, Throwable t) {

                //TODO : 에러 얼럿

            }
        });
    }


//    public void registerUserDataForFirestore(FirebaseUser user, final String regiterBy, @Nullable final String myReferCode) {
//
//
//        SharedPreferences sharedPreferences = getSharedPreferences("manboData", MODE_PRIVATE);
//        sharedPreferences.edit().putString("loginType", regiterBy).apply();
//
//
////                                long now = System.currentTimeMillis();
//        Date lastSignDate = new Date(user.getMetadata().getLastSignInTimestamp());
//        Date creationDate = new Date(user.getMetadata().getCreationTimestamp());
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String lastSignInTime = simpleDateFormat.format(lastSignDate);
//        String creationTime = simpleDateFormat.format(creationDate);
//
//        final Member member = new Member();
//
//        String makeEmail = "";
//        if (user.getEmail() == null) {
//            makeEmail = user.getUid() + "@kakao.com";
//        } else {
//            makeEmail = user.getEmail();
//        }
//
//        member.setEmail(makeEmail);
//        if (user.getDisplayName() != null) member.setNickName(user.getDisplayName());
//        if (user.getPhotoUrl() != null) member.setPhotoUrl(user.getPhotoUrl().toString());
//        member.setLastSignInTimestamp(lastSignInTime);
//        member.setCreationTimestamp(creationTime);
////                                member.setReferCode(); 만들고 유효성까지 확인 후 저장
//        member.setReferCode(myReferCode);
//        member.setRegisteredBy(regiterBy);
//        member.setUid(user.getUid());
//
//
//        //파이어스토어 저장
//        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("members").document(member.getUid()).set(member).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//                loadingIndicator.cancel();
//
//                //메인으로 이동
//                Intent main = new Intent(getApplicationContext(), MainActivity.class);
//                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                main.putExtra("infoData", member);
//                startActivity(main);
//
//                finish();
//
//                Log.e("ios", "firestore 저장완료,  myReferCode : " + myReferCode);
//
//            }
//
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                loadingIndicator.cancel();
//                Log.e("ios", "firestore 저장실패", e);
//            }
//        });
//
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("ios", "requestCode : " + requestCode + ", resultCode : " + resultCode);

        if (requestCode == Const.TermsType.PRIVACY) {
            if (resultCode == RESULT_OK) {
                isCheck1 = true;
                privacyCheck.setImageDrawable(getResources().getDrawable(R.drawable.vec_checkbox_fill_circle_outline));
            } else {
                isCheck1 = false;
                privacyCheck.setImageDrawable(getResources().getDrawable(R.drawable.vec_checkbox_blank_circle_outline));
            }
        }

        if (requestCode == Const.TermsType.LOCATION) {
            if (resultCode == RESULT_OK) {
                isCheck2 = true;
                locationCheck.setImageDrawable(getResources().getDrawable(R.drawable.vec_checkbox_fill_circle_outline));
            } else {
                isCheck2 = false;
                locationCheck.setImageDrawable(getResources().getDrawable(R.drawable.vec_checkbox_blank_circle_outline));
            }
        }

        if (requestCode == Const.TermsType.PUSH) {
            if (resultCode == RESULT_OK) {
                isCheck3 = true;
                pushCheck.setImageDrawable(getResources().getDrawable(R.drawable.vec_checkbox_fill_circle_outline));
            } else {
                isCheck3 = false;
                pushCheck.setImageDrawable(getResources().getDrawable(R.drawable.vec_checkbox_blank_circle_outline));
            }
        }


        if (isCheck1 && isCheck2) {
            nextBtn.setEnabled(true);
            nextBtnTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.home_cash_));
        } else {
            nextBtn.setEnabled(false);
            nextBtnTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_gray));
        }
    }

    public void checkDeviceSizeAndDiappearText() {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        float width = (float) dm.widthPixels;
        float height = (float) dm.heightPixels;

        float ratio = height / width;

        Log.e("ios", "w : " + width + " , h : " + height + " , ratio : " + ratio + " , dpi : " + dm.densityDpi);

        GoneTextView = (TextView) findViewById(R.id.dialog_terms_title2);
        if (ratio > 1.80) { // s8, s9 +

        } else { //s6 edge -
            GoneTextView.setVisibility(View.GONE);
        }

    }


    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onBackPressed() {

        setResult(RESULT_CANCELED);
        finish();

    }
}
