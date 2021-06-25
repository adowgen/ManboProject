package com.colorworld.manbocash.mainFragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.colorworld.manbocash.Const;
import com.colorworld.manbocash.MainActivity;
import com.colorworld.manbocash.R;
import com.colorworld.manbocash.room.MyInfoDatabase;
import com.colorworld.manbocash.room.dao.MyInfoDao;
import com.colorworld.manbocash.room.entity.MyInfoEntity;
import com.colorworld.manbocash.service.StepService;
import com.colorworld.manbocash.tutorial.tutorials.TutorialSupportActivity;
import com.colorworld.manbocash.util.AppExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kakao.sdk.link.LinkClient;
import com.kakao.sdk.link.model.LinkResult;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class settingFragment  extends Fragment {
    public static settingFragment mStaticSFContext;
    private SharedPreferences stepCountSP;

    private Activity mActivity;
    public TextView mVersion, mLogout, mNickName, mReferCode;

    public ImageView mShareBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mStaticSFContext = this;

        final View rootView = inflater.inflate(R.layout.fragment_main_setting, container, false);


        mNickName = (TextView) rootView.findViewById(R.id.userName_value);
        mReferCode = (TextView) rootView.findViewById(R.id.recomm_value);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        MyInfoDatabase myInfoDatabase = MyInfoDatabase.getInstance(getApplicationContext());
        MyInfoDao myInfoDao = myInfoDatabase.getMyInfoDao();
        AppExecutors executors = new AppExecutors();

        executors.diskIO().execute(() -> {

            MyInfoEntity infoData = new MyInfoEntity();
            infoData = myInfoDao.selectInfoDataByUID(currentUser.getUid());


            try {
                mNickName.setText(infoData.nickname);
                mReferCode.setText(infoData.refercode);
            }catch (NullPointerException e) {
                mNickName.setText("myNick");
                mReferCode.setText("refercode");
            }

        });


        stepCountSP = this.getActivity().getSharedPreferences("stepCount", MODE_PRIVATE);



        SharedPreferences dataSp = this.getActivity().getSharedPreferences("manboData", MODE_PRIVATE);
        String versionText = dataSp.getString("version", "1.0");

        mVersion = (TextView) rootView.findViewById(R.id.appVersion_value);
        mVersion.setText(versionText);


        mShareBtn = (ImageView) rootView.findViewById(R.id.shareBtn);
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinkClient.getInstance().customTemplate(getApplicationContext(), 40353, (linkResult, throwable) -> {

                    Log.e("ios", "kakao link throwable : " + throwable);


                    if (linkResult != null) {

                        startActivity(linkResult.getIntent());

                    }else {
                        Log.e("ios", "linkResult is null");




                    }


                    return null;
                });

            }
        });


        mLogout = (TextView) rootView.findViewById(R.id.setting_logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO : 필요한 초기화 설정

                new MaterialDialog.Builder(rootView.getContext())
                        .title("로그아웃")
                        .content("로그아웃을 하시면 걸음수 및 코인등\n해당 정보가 리셋됩니다\n확인을 누르면 로그아웃 됩니다")
                        .positiveText("확인")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                //TODO 서비스 워크매니저 중지

                                ((MainActivity)MainActivity.mStaticContext).stopStepServiceAndWorkManager();


                                stepCountSP.edit().putInt("appOsCount", 0).apply();
                                stepCountSP.edit().putInt("appOsCount_Background", 0).apply();
                                stepCountSP.edit().putInt("convertedCount", 0).apply();
                                stepCountSP.edit().putInt("heartCoinCount", 0).apply();
                                stepCountSP.edit().putLong("myCash", 0).apply();

                                FirebaseAuth.getInstance().signOut();
                                TutorialSupportActivity.start(rootView.getContext());
                            }
                        })
                        .negativeText("취소")
                        .positiveColorRes(R.color.color_common)
                        .negativeColorRes(R.color.color_common)
                        .show();



            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();



    }

    //    public void fetchVersionText(String str) {
//        mVersion.setText(str);
//    }
}
