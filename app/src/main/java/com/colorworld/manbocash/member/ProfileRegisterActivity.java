package com.colorworld.manbocash.member;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.colorworld.manbocash.Const;
import com.colorworld.manbocash.MainActivity;
import com.colorworld.manbocash.R;
import com.colorworld.manbocash.indicator.LoadingIndicator;
import com.colorworld.manbocash.model.Duplication;
import com.colorworld.manbocash.requestPermission.RequestPermission;
import com.colorworld.manbocash.retrofit.NetRetrofit;
import com.colorworld.manbocash.room.entity.MyInfoEntity;
import com.colorworld.manbocash.terms.DialogTerms;
import com.colorworld.manbocash.tutorial.tutorials.TutorialSupportActivity;
import com.colorworld.manbocash.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRegisterActivity extends AppCompatActivity {

    private static int REQ_CODE_SELECT_IMAGE = 0;


    private Activity mActivity;
    private EditText nickName, referee;
    private String email_ID, password, myReferCode;
    private ImageView myPhoto;
    private Button confirmBtn;

    private FirebaseAuth mAuth;

    private LoadingIndicator loadingIndicator;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_profile);

        mActivity = this;
        mAuth = FirebaseAuth.getInstance();

        if (getIntent().getExtras() != null) {
            email_ID = getIntent().getExtras().get("email").toString();
            password = getIntent().getExtras().get("password").toString();
        }


        loadingIndicator = new LoadingIndicator(this);
        loadingIndicator.setCancelable(false);


        nickName = (EditText) findViewById(R.id.signUp_nickname);
        referee = (EditText) findViewById(R.id.signUp_referee);
        referee.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    //TODO 추천인 유효성 검사


                    //로그인 시도, 파이어스토어저장, 로그인 성공 실패

                    //나의 추천인코드 생성 및 유효성 검사
//                    createReferCode();


                    //메인화면
                }

                return false;
            }
        });


        myPhoto = (ImageView) findViewById(R.id.signUp_cameraBtn);
        myPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });


        confirmBtn = (Button) findViewById(R.id.body_confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingIndicator.show();

                createReferCode();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("ios", "My profile onActivityResult - requestCode : " + requestCode + ", resultCode : " + resultCode);


        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == RESULT_OK) {

                SharedPreferences sharedPreferences = getSharedPreferences("manboData", MODE_PRIVATE);
                sharedPreferences.edit().putString("profileImagePath", data.getDataString()).apply();

                Uri uri = data.getData();

                CropImage.activity(uri)
                        .setActivityTitle("내 프로필 이미지")
                        .setBackgroundColor(R.color.corlor_black)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setFixAspectRatio(true)
                        .start(this);

            }
        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri uri = result.getUri();
            Glide.with(this).load(uri).into(myPhoto);

        }
    }


    private void createReferCode() {
        myReferCode = "";
        myReferCode = Util.makeReferCode();

        Log.e("ios", "myReferCode : " + myReferCode);


        Call<Duplication> retrofit = NetRetrofit.getInstance().getService().duplicationReferCodeCheck(myReferCode);
        retrofit.enqueue(new Callback<Duplication>() {
            @Override
            public void onResponse(Call<Duplication> call, Response<Duplication> response) {

                if (response.isSuccessful()) {
                    Duplication duplication = response.body();
                    Log.e("ios", "나의 추천코드 중복 검사 : " + duplication.getResult());

//                            goToProfileActivity();
                    if (duplication.getResult().toString().equals("중복")) {

                        createReferCode();


                    }else { //중복아님


                        createUserAndLogin();

                    }



                } else {

                    loadingIndicator.cancel();
                    Util.showSingleBtnDialog(mActivity, "네트워크 에러", "다시 시도해주시기 바랍니다\n" + response.errorBody().toString());
                    Log.e("ios", "나의 추천코드 중복 에러 : " + response.errorBody());


                }

            }

            @Override
            public void onFailure(Call<Duplication> call, Throwable t) {

                //TODO : 에러 얼럿

            }
        });

    }


    private void createUserAndLogin() {
        mAuth.createUserWithEmailAndPassword(email_ID, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("ios", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            Toast.makeText(ProfileRegisterActivity.this, "회원가입이 완료 되었습니다",
                                    Toast.LENGTH_SHORT).show();

                            loginByNewUser();

                        } else {
                            Log.e("ios", "createUserWithEmail:failure", task.getException());
                            loadingIndicator.cancel();
                        }
                    }
                });
    }


    private void loginByNewUser() {

        mAuth.signInWithEmailAndPassword(email_ID, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        loadingIndicator.cancel();

                        if (task.isSuccessful()) {
                            Log.e("ios", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.e("ios", "signInWithCredential:success"
                                    + "\nuser : " + user.getDisplayName()
                                    + "\n" + user.getPhoneNumber()
                                    + "\n" + user.getEmail()
                                    + "\n" + user.getPhotoUrl()
                                    + "\n" + user.getProviderData()
                                    + "\n" + user.getMetadata().getCreationTimestamp()
                                    + "\n" + user.getMetadata().getLastSignInTimestamp()
                                    + "\n" + user.getUid());


//                            Util.registerUserDataForFirestore(getApplicationContext(), user, Const.RegisteredBy.MANBOCASH, myReferCode);

//                            ((DialogTerms)DialogTerms.staticDialogTermsContext).registerUserDataForFirestore(user, Const.RegisteredBy.MANBOCASH, myReferCode);


                            // 무조건 처음 가입 이니까 퍼미션 화면으로

                            Date lastSignDate = new Date(user.getMetadata().getLastSignInTimestamp());
                            Date creationDate = new Date(user.getMetadata().getCreationTimestamp());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String lastSignInTime = simpleDateFormat.format(lastSignDate);
                            String creationTime = simpleDateFormat.format(creationDate);

                            MyInfoEntity myInfo = new MyInfoEntity();

                            myInfo.creationTimestamp = creationTime;
                            myInfo.email = user.getEmail();
                            myInfo.lastSignInTimestamp = lastSignInTime;
                            myInfo.nickname = nickName.getText().toString();
                            //photourl
                            myInfo.refercode = myReferCode;
                            myInfo.referee = referee.getText().toString();
                            myInfo.registerby = Const.RegisteredBy.MANBOCASH;
                            myInfo.uid = user.getUid();


                            Intent intent = new Intent(getApplicationContext(), RequestPermission.class);
                            intent.putExtra("infoData", myInfo);
                            startActivity(intent);



                        } else {
                            Log.e("ios", "signInWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);



                        }

                    }
                });

    }

}
