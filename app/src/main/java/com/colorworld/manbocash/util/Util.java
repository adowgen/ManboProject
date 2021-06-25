package com.colorworld.manbocash.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.colorworld.manbocash.BranchViewActivity;
import com.colorworld.manbocash.MainActivity;
import com.colorworld.manbocash.R;
import com.colorworld.manbocash.model.Duplication;
import com.colorworld.manbocash.model.Member;
import com.colorworld.manbocash.retrofit.NetRetrofit;
import com.colorworld.manbocash.room.entity.MyInfoEntity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Util {

    // 이메일정규식
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    //비밀번호정규식 - 숫자, 문자, 특수문자 중 2가지 포함(8~15자)
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM = Pattern.compile("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#$%^&*])(?=.*[0-9!@#$%^&*]).{8,15}$");

    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.matches();
    }


    public static void showSingleBtnDialog(Context context, String title, String body) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(body)
                .positiveText("확인")
                .positiveColorRes(R.color.color_common)
                .show();
    }


    public static String randomPassword (int length) {
        int index = 0;
        char[] charSet = new char[] {
                '0','1','2','3','4','5','6','7','8','9'
                ,'A','B','C','D','E','F','G','H','I','J','K','L','M'
                ,'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

        StringBuffer sb = new StringBuffer();
        for (int i=0; i<length; i++) {
            index =  (int) (charSet.length * Math.random());
            sb.append(charSet[index]);
        }

        return sb.toString();
    }

    public static String makeReferCode() {
        String code = randomPassword(6);
        return code;
    }


    public static void registerUserDataForFirestore(Context context, MyInfoEntity myInfo) {


//        SharedPreferences sharedPreferences = context.getSharedPreferences("manboData", Context.MODE_PRIVATE);
//        sharedPreferences.edit().putString("loginType", regiterBy).apply();


        //파이어스토어 저장
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("members").document(myInfo.uid).set(myInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

//                loadingIndicator.cancel();




                //메인으로 이동
//                Intent main = new Intent(context, BranchViewActivity.class);
//                context.startActivity(main);

//                finish();

//                Log.e("ios", "firestore 저장완료,  myReferCode : " + myReferCode);

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                loadingIndicator.cancel();
                Log.e("ios", "firestore 저장실패", e);
            }
        });

    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }else {
            return false;
        }

    }


}
