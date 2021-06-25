package com.colorworld.manbocash;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.util.StringUtil;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.colorworld.manbocash.mainFragments.chartFragment;
import com.colorworld.manbocash.mainFragments.homeFragment;
import com.colorworld.manbocash.mainFragments.settingFragment;
import com.colorworld.manbocash.mainFragments.storeFragment;
import com.colorworld.manbocash.model.Member;
import com.colorworld.manbocash.receiver.AlarmReceiver;
import com.colorworld.manbocash.requestPermission.RequestPermission;
import com.colorworld.manbocash.room.MyInfoDatabase;
import com.colorworld.manbocash.room.StepsDatabase;
import com.colorworld.manbocash.room.dao.MyInfoDao;
import com.colorworld.manbocash.room.dao.StepsDao;
import com.colorworld.manbocash.room.entity.MyInfoEntity;
import com.colorworld.manbocash.room.entity.MySteps;
import com.colorworld.manbocash.service.StepService;
import com.colorworld.manbocash.util.AppExecutors;
import com.colorworld.manbocash.util.Util;
import com.colorworld.manbocash.workManager.InitCurrentStepWorker;
import com.colorworld.manbocash.workManager.SaveStepCountWorker;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.protobuf.StringValue;
import com.igaworks.adpopcorn.IgawAdpopcorn;
import com.igaworks.adpopcorn.IgawAdpopcornExtension;
import com.igaworks.adpopcorn.cores.model.APClientRewardItem;
import com.igaworks.adpopcorn.interfaces.IAPClientRewardCallbackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private static final String android_version = "android_version";
    private static final String main_greeting = "main_greeting";

    public static MainActivity mStaticContext;
    public Context mContext;

    //main fragment view
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private homeFragment mHomeFragment = new homeFragment();
    private chartFragment mChartFragment = new chartFragment();
    private storeFragment mStoreFragment = new storeFragment();
    private settingFragment mSettingFragment = new settingFragment();

    private BottomNavigationView bottomNavigationView;

    /*구글광고*/
    private FrameLayout mAd_bg;
    private ImageView mAd_bg_x_btn;

    private AdView mBottomAdView;
    private AdView mRectangleAdView;
    //    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;
    private RewardedAdLoadCallback adLoadCallback;

    private boolean isAdSuccess = false;

    //test
    private boolean isView = false;

    //백그라운드 서비스
    private Intent mStepService;

    //알람등록
    private AlarmManager resetAlarmManager, repeatAlarmManager;
    private PendingIntent resetPintent, repeatPintent;

    private String changeNick;

    private long RewardQuantityCash;

    private void startWorkManager() {


        //지정된 시간(자정)으로 사용하려면 알람등록
//        WorkRequest getCurrentWork = new PeriodicWorkRequest.Builder(InitCurrentStepWorker.class,
//                1, TimeUnit.DAYS,
//                1, TimeUnit.HOURS).build();

//        WorkManager.getInstance(mContext)
//                .enqueue(getCurrentWork);


        WorkRequest saveRoom = new PeriodicWorkRequest.Builder(SaveStepCountWorker.class,
                20, TimeUnit.MINUTES,
                5, TimeUnit.MINUTES).build();
        WorkManager.getInstance(mContext)
                .enqueue(saveRoom);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("ios", "MainAcitivity");
        mContext = getApplicationContext();
        mStaticContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        getTokeWithFCM();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

                Log.e("ios", "onInitializationComplete : " + initializationStatus);

            }
        });
//        MobileAds.initialize(this, "ca-app-pub-6561710413911304~8412658551");

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
        initAdvertising();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        transaction.replace(R.id.frameLayout, mHomeFragment).commitAllowingStateLoss();
        transaction.add(R.id.frameLayout, mHomeFragment);
        transaction.add(R.id.frameLayout, mChartFragment);
        transaction.add(R.id.frameLayout, mStoreFragment);
        transaction.add(R.id.frameLayout, mSettingFragment);

        transaction.hide(mChartFragment);
        transaction.hide(mStoreFragment);
        transaction.hide(mSettingFragment).commitAllowingStateLoss();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.action_botMenu_home:
                                Log.e("ios", "action_botMenu_home");
//                                transaction.replace(R.id.frameLayout, mHomeFragment).commitAllowingStateLoss();
                                isStoreView = false;
                                mPressFirstBackKey = false;

                                transaction.show(mHomeFragment);
                                transaction.hide(mChartFragment);
                                transaction.hide(mStoreFragment);
                                transaction.hide(mSettingFragment).commitAllowingStateLoss();


                                break;
                            case R.id.action_botMenu_chart:
                                Log.e("ios", "action_botMenu_chart");
//                                transaction.replace(R.id.frameLayout, mChartFragment).commitAllowingStateLoss();
                                isStoreView = false;
                                mPressFirstBackKey = false;

                                transaction.hide(mHomeFragment);
                                transaction.show(mChartFragment);
                                transaction.hide(mStoreFragment);
                                transaction.hide(mSettingFragment).commitAllowingStateLoss();

                                break;
                            case R.id.action_botMenu_stroe:

                                isStoreView = true;
                                mPressFirstBackKey = false;
                                Log.e("ios", "action_botMenu_stroe");
//                                transaction.replace(R.id.frameLayout, mStoreFragment).commitAllowingStateLoss();
                                transaction.hide(mHomeFragment);
                                transaction.hide(mChartFragment);
                                transaction.show(mStoreFragment);
                                transaction.hide(mSettingFragment).commitAllowingStateLoss();
                                break;
                            case R.id.action_botMenu_set:

                                isStoreView = false;
                                mPressFirstBackKey = false;
                                Log.e("ios", "action_botMenu_set");
//                                transaction.replace(R.id.frameLayout, mSettingFragment).commitAllowingStateLoss();
                                transaction.hide(mHomeFragment);
                                transaction.hide(mChartFragment);
                                transaction.hide(mStoreFragment);
                                transaction.show(mSettingFragment).commitAllowingStateLoss();
                                break;
                        }
                        return true;
                    }
                });

        initFirebaseRemoteConfig();
//        getWeatherStatus();
//        *테스트용으로 프로덕트일때 풀어야됨
        //intro_login
//        Intent isLogin = new Intent(mContext, Tutorial_LoginActivity.class);
//        startActivity(isLogin);

        if (null == StepService.serviceIntent) {

            mStepService = new Intent(this, StepService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(mStepService);
            } else {
                startService(mStepService);
            }

//            Toast.makeText(this, "서비스 시작", Toast.LENGTH_SHORT).show();

        } else {

            mStepService = StepService.serviceIntent;

//            Toast.makeText(this, "서비스 중", Toast.LENGTH_SHORT).show();

        }

        //날짜 계산 후 날짜가 지났으면 스탭 데이터 서버에 올리기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd", mContext.getResources().getConfiguration().locale);
        String todayGetTime = sdf.format(date);

        SharedPreferences compareDateSP = getSharedPreferences("stepCount", MODE_PRIVATE);
        String compareDate = compareDateSP.getString("compareDate", "0000");


        if (compareDate.equals("0000")) {
            compareDateSP.edit().putString("compareDate", todayGetTime).apply();
            boolean test = compareDateSP.edit().putString("createFirestoreDocu", "create").commit();

        } else {

            if (!compareDate.equals(todayGetTime)) {
                // TODO : 파이어스토어 저장, 디비 업데이트 (is_Success)
                StepsDatabase stepsDatabase = StepsDatabase.getInstance(mContext);
                StepsDao stepsDao = stepsDatabase.getMyStepsDao();
                AppExecutors executors = new AppExecutors();

                executors.diskIO().execute(() -> {
                    MySteps[] myStepsArrayList = stepsDao.selectBySuccess(false);
                    Map<String, Object> step = new HashMap<>();

                    List<String> sender2 = new ArrayList<>();

                    if (myStepsArrayList.length > 0) {

                        String[] sender = new String[myStepsArrayList.length];

                        int i = 0;
                        for (MySteps mySteps : myStepsArrayList) {
                            sender[i] = mySteps.date + "|" + mySteps.step;
                            sender2.add(mySteps.date + "|" + mySteps.step);
                            i++;
                            Log.e("ios", "mySteps : " + mySteps.isSuccess + "\n" + mySteps.step);

                            if (i == myStepsArrayList.length) {
                                step.put("last_update", mySteps.date);
                            }
                        }

                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();


                        if (compareDateSP.getString("createFirestoreDocu", "create").equals("create")) {
                            step.put("steps_history", sender2);
                            compareDateSP.edit().putString("createFirestoreDocu", "alreadyCreated").apply();
                            updateStepDataWithFirestore(currentUser, myStepsArrayList, step, true);
                        } else {
                            step.put("steps_history", sender);
                            updateStepDataWithFirestore(currentUser, myStepsArrayList, step, false);
                        }


                    }
                });


            }
        }

        registerResetAlarm();
        startWorkManager();

    }

    public void stopStepServiceAndWorkManager() {
        if (StepService.serviceIntent != null) stopService(StepService.serviceIntent);
        WorkManager.getInstance(mContext).cancelAllWork();
    }

    public void openOfferWall() {
        IgawAdpopcorn.openFeedOfferWall(MainActivity.this);
    }

    public void getTokeWithFCM() {


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        Log.e("ios", "FCM token : " + token);

                        // Log and toast
//                        String msg = getString("FCM registration Token:  %s" , token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void registerResetAlarm() {


        resetAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent resetIntent = new Intent(this, AlarmReceiver.class);
        resetPintent = PendingIntent.getBroadcast(this, 24, resetIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // 자정 시간
        Calendar resetCal = Calendar.getInstance();
        resetCal.setTimeInMillis(System.currentTimeMillis());
        resetCal.set(Calendar.HOUR_OF_DAY, 23);
        resetCal.set(Calendar.MINUTE, 55);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            resetAlarmManager.setExact(AlarmManager.RTC_WAKEUP, resetCal.getTimeInMillis(), resetPintent);
        } else {
            resetAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, resetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, resetPintent);
        }


        //1시간마다 스탭카운트 디비 저장 => 워크매니저로 대체
//        repeatAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//        Intent repeatIntent = new Intent(this, AlarmReceiver.class);
//        resetIntent.putExtra("requestCode", 112);
//        repeatPintent = PendingIntent.getBroadcast(this, 1, repeatIntent, PendingIntent.FLAG_CANCEL_CURRENT );
//
//        repeatAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + 60 * 1000, AlarmManager.INTERVAL_FIFTEEN_MINUTES, repeatPintent);

    }

    public void updateStepDataWithFirestore(FirebaseUser user, final MySteps[] mySteps, final Map stepMap, boolean create) {


        Log.e("ios", "stepMap.get(\"steps_history\") : " + stepMap.get("steps_history"));

        //파이어스토어 저장
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference stepHistoryDocu = db.collection("members").document(user.getUid()).collection("steps").document("general");


        Log.e("ios", "stepHistoryDocu.getId() : " + stepHistoryDocu.getId() + " , " + stepHistoryDocu.getFirestore() + " , " + stepHistoryDocu.getPath() + " , " + stepHistoryDocu.getParent());


        if (create) {

            Log.e("ios", "최초 스탭수 디비 저장");

            //create
            db.collection("members").document(user.getUid()).collection("steps").document("general").set(stepMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    StepsDatabase stepsDatabase = StepsDatabase.getInstance(mContext);
                    StepsDao stepsDao = stepsDatabase.getMyStepsDao();
                    AppExecutors executors = new AppExecutors();

                    for (MySteps mySteps1 : mySteps) {
                        mySteps1.isSuccess = true;
                        executors.diskIO().execute(() -> {
                            stepsDao.updateMyStep(mySteps1);
                        });
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("ios", "firestore 저장실패", e);
                }
            });
        } else {
            Log.e("ios", "최초 스탭수 디비 업데이트");


            //update
            stepHistoryDocu.update("last_update", stepMap.get("last_update"));

            String[] stepArray = (String[]) stepMap.get("steps_history");
            stepHistoryDocu.update("steps_history", FieldValue.arrayUnion(stepArray));

        }
    }

    public void initAdvertising() {
        mBottomAdView = findViewById(R.id.bottomAdView);
        mRectangleAdView = findViewById(R.id.rectangleAdView);


//        List<String> testDeviceIds = Arrays.asList("E47A32E2EEEBC3429772B45B15A2889F");
//        RequestConfiguration configuration =
//                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
//        MobileAds.setRequestConfiguration(configuration);


        AdRequest adRequest = new AdRequest.Builder().build();
        mBottomAdView.loadAd(adRequest);
        mRectangleAdView.loadAd(adRequest);
        mBottomAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.


            }


            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                Log.e("ios", "배너 광고 : onAdFailedToLoad : " + loadAdError.toString() + "\n info : " + loadAdError.getResponseInfo());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBottomAdView.loadAd(adRequest);
                    }
                }, 15000);


            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.e("ios", "광고 : onAdOpened");


            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        //앱 처음 오픈시 onAdLoaded(),

        mRectangleAdView.setAdListener(new AdListener() {

        });

        mRectangleAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
//                Log.e("ios", "==============================================mRectangleAdView : onAdLoaded()");

                isAdSuccess = true;
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                //TODO 얼럿으로 알려줘야할듯.
                isAdSuccess = false;
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
//                Log.e("ios", "==============================================mRectangleAdView : onAdOpened()");
                isAdSuccess = true;
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
//                Log.e("ios", "==============================================mRectangleAdView : onAdLeftApplication()");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
//                Log.e("ios", "==============================================mRectangleAdView : onAdClosed()");
            }
        });


        mAd_bg = (FrameLayout) findViewById(R.id.ad_bg);
        mAd_bg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
                //하위 뷰의 버튼이 안눌리게 하기 위함
            }
        });
        mAd_bg_x_btn = (ImageView) findViewById(R.id.ad_bg_x);
        mAd_bg_x_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAd_bg.setVisibility(View.INVISIBLE);
                bottomNavigationView.setAlpha(1.0f);
                enableBottomNavi(true);
                ((homeFragment) homeFragment.mStaticHFContext).enableBtns(true);

                Log.e("ios", "isAdSuccess : " + (isAdSuccess ? "true" : "false"));

                if (isAdSuccess) ((homeFragment) homeFragment.mStaticHFContext).exchangeCash(1);

            }
        });

        mAd_bg.setVisibility(View.INVISIBLE);


        mRewardedAd = createAndLoadRewardedAd();

    }

    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");

        //ca-app-pub-6561710413911304/8738316989
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                Log.e("ios", "rewardedAd.getRewardItem().getAmount() : " + rewardedAd.getRewardItem().getAmount());


                ((homeFragment)homeFragment.mStaticHFContext).animationBtnWithCompleteLoadRewardAD();


            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
                Log.e("ios", "onRewardedAdFailedToLoad() : " + adError.getResponseInfo() + "\n" + adError.toString());
                //그외 다시 로드 재시도
                if (Util.isNetworkConnected(mContext)) {
                    mRewardedAd = createAndLoadRewardedAd();
                } else {
                    //네트워크가 연결이 안되서 실패이면 재시도 안함
                }
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    public void showRewardAD() {
        if (mRewardedAd.isLoaded()) {
            Activity activityContext = MainActivity.this;
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    mRewardedAd = createAndLoadRewardedAd();
                }


                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    Log.e("ios", "RewardItem : " + reward.getType() + "\n" + reward.getAmount());

                    SharedPreferences sp = getSharedPreferences("stepCount", MODE_PRIVATE);
                    sp.edit().putInt("googleReward", reward.getAmount()).apply();

                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                    Log.e("ios", "showRewardAD : onRewardedAdFailedToShow");
                }
            };
            mRewardedAd.show(activityContext, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");

            new MaterialDialog.Builder(this)
                    .title("현재 게재할 광고가 없습니다")
                    .content("잠시 후 다시 시도해 주시기 바랍니다.")
                    .positiveText("확인")
                    .positiveColorRes(R.color.color_common)
                    .show();


        }
    }

    public void getWeatherStatus() {
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("ios", "PERMISSION_GRANTED");
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        String provider = location.getProvider();
        final double longitude = location.getLongitude();
        final double latitude = location.getLatitude();
        double altitude = location.getAltitude();

        Log.e("ios", "위치정보 : " + provider + "\n" +
                "위도 : " + longitude + "\n" +
                "경도 : " + latitude + "\n" +
                "고도  : " + altitude);


        new Thread(new Runnable() {
            @Override
            public void run() {
                // Thread 안에서 바로 UI작업을 한다.
                String myLocationWeatherURL = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=fcaeb43325261acc32ca270c4c7f9196";

                try {
                    Log.e("ios", "json : " + readJsonFromUrl(myLocationWeatherURL));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public void requestPermissionActivity(Member myInfoData) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(mContext, RequestPermission.class);
                intent.putExtra("infoData", myInfoData);
                startActivity(intent);

                return;
            }
        }

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public void openadverstising() {

        mAd_bg.setVisibility(View.VISIBLE);
        bottomNavigationView.setAlpha(0.1f);
        enableBottomNavi(false);

    }

    public void enableBottomNavi(boolean enable) {
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            bottomNavigationView.getMenu().getItem(i).setEnabled(enable);
        }
    }

    public void initFirebaseRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();

                            Log.e("ios", "Config params updated: " + updated);

                            Log.e("ios", "Fetch and activate succeeded : " + task.getException());


                        } else {
                            Log.e("ios", "Fetch failed");

                        }
                        displayWelcomeMessage();
                    }
                });
    }

    public void displayWelcomeMessage() {

        String mainGreeting = mFirebaseRemoteConfig.getString("main_greeting");
        String andVersion = mFirebaseRemoteConfig.getString("android_version");

        SharedPreferences dataSp = this.getSharedPreferences("manboData", MODE_PRIVATE);
        dataSp.edit().putString("greeting", mainGreeting).apply();
        dataSp.edit().putString("version", andVersion).apply();

        //test uid
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //생각 좀 해야함

        MyInfoDatabase myInfoDatabase = MyInfoDatabase.getInstance(getApplicationContext());
        MyInfoDao myInfoDao = myInfoDatabase.getMyInfoDao();
        AppExecutors executors = new AppExecutors();


        executors.diskIO().execute(() -> {


            MyInfoEntity infoData = new MyInfoEntity();
            infoData = myInfoDao.selectInfoDataByUID(currentUser.getUid());

            try {
                changeNick = mainGreeting.replace("%NICK%", infoData.nickname);
            } catch (NullPointerException e) {
                changeNick = mainGreeting.replace("%NICK%", "사용자");
            }

            executors.mainThread().execute(() -> {
                ((homeFragment) homeFragment.mStaticHFContext).fetchGreetingText(changeNick);
            });


            Log.e("ios", "changeNick : " + changeNick);

        });


//        ((settingFragment) settingFragment.mStaticSFContext).fetchVersionText(andVersion);


        initOfferWall(currentUser);


        //저장안됨... 퍼미션없음
//        MyInfoEntity infoData = new MyInfoEntity();
//        infoData.email = currentUser.getEmail();
//        infoData.nickname = currentUser.getDisplayName();
//        infoData.photourl = currentUser.getPhotoUrl().toString();
//        infoData.refercode = currentUser.getReferCode();
//        infoData.referee = currentUser.getReferee();
//        infoData.registerby = currentUser.getRegisteredBy();
//        infoData.uid = currentUser.getUid();
//
//
//        MyInfoDatabase myInfoDatabase = MyInfoDatabase.getInstance(mContext);
//        MyInfoDao myInfoDao = myInfoDatabase.getMyInfoDao();
//        AppExecutors executors = new AppExecutors();
//
//        executors.diskIO().execute(() -> {
//            myInfoDao.insertMyInfo(infoData);
//        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    public void initOfferWall(FirebaseUser currentUser) {
//        MyInfoDatabase myInfoDatabase = MyInfoDatabase.getInstance(mContext);
//        MyInfoDao myInfoDao = myInfoDatabase.getMyInfoDao();
//        AppExecutors infoExecutors = new AppExecutors();
//
//        infoExecutors.diskIO().execute(() -> {
//            MyInfoEntity data = myInfoDao.selectByRegisterBy(Const.RegisteredBy.MANBOCASH);
//
//        });


        IgawAdpopcorn.setUserId(mContext, currentUser.getUid());

        IgawAdpopcornExtension.setClientRewardCallbackListener(mContext, new IAPClientRewardCallbackListener() {
            @Override
            public void onGetRewardInfo(boolean b, String s, APClientRewardItem[] apClientRewardItems) {

                //결과
                if (b == true) {
                    Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();

                    /*
                     * 1. rewardItem[i].getCampaignKey() : 유저가 완료한 캠페인의 캠페인 키
                     * 2. rewardItem[i].getCampaignTitle() : 유저가 완료한 캠페인의 이름
                     * 3. rewardItem[i].getRTID() : 완료 트랜잭션 아이디
                     * 4. rewardItem[i].getRewardQauntity() : 유저에게 지급해야 할 가상화폐의 양
                     * 5. rewardItem[i].didGiveRewardItem() : 리워드 지급 처리 완료 정보를 IGAW서버에 전달하기 위한 api
                     * */


                    for (APClientRewardItem rewardItem : apClientRewardItems) {

                        Log.e("ios", "rewardItem.getCampaignKey : " + rewardItem.getCampaignKey()
                                + "\nrewardItem.getCampaignTitle : " + rewardItem.getCampaignTitle()
                                + "\nrewardItem.getRTID : " + rewardItem.getRTID()
                                + "\nrewardItem.getRewardQuantity : " + rewardItem.getRewardQuantity()
                                + "\nrewardItem.getItemKey : " + rewardItem.getItemKey()
                                + "\nrewardItem.getItemName : " + rewardItem.getItemName());

                        RewardQuantityCash = rewardItem.getRewardQuantity();

                        rewardItem.didGiveRewardItem();

                    }
                }


            }

            @Override
            public void onDidGiveRewardItemResult(boolean b, String s, int i, String s1) {

                /*
                 * onDidGiveRewardItemResult(boolean isSuccess, String resultMsg, int resultCode, String completedRewardKey)
                 * */

                if (b == true) {

                    //리워드 전에 서버에서 중복 검사후 중복아니면 리워드 제공 및 서버 업데이트

                    //리워드 캐시 + 내 캐시 가 원래 캐시보다 많으면 리워드로 판단

                    SharedPreferences sp = getSharedPreferences("stepCount", MODE_PRIVATE);
                    long currentCash = sp.getLong("myCash", 0);

                    long rewardCash = sp.getLong("rewardCash", 0);

                    if (rewardCash == 0) {
                        rewardCash = currentCash + RewardQuantityCash;
                    } else {
                        rewardCash += RewardQuantityCash;
                    }

                    sp.edit().putLong("rewardCash", rewardCash).apply();


                    Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
                    String completedRewardKey = s1;
                    Log.e("ios", "completedRewardKey : " + completedRewardKey + "resultCode : " + i);

                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (null != mStepService) {
//            stopService(mStepService);
//            mStepService = null;
//        }

    }

    private boolean mPressFirstBackKey = false;
    private boolean isStoreView = false;

    @Override
    public void onBackPressed() {

        if (isStoreView) {

            if (((storeFragment)storeFragment.staticStoreContext).canGoBack()) {
                ((storeFragment)storeFragment.staticStoreContext).goBack();
                mPressFirstBackKey = false;
            }else {
                if (mPressFirstBackKey == false) {
                    Toast.makeText(this, "한번 더 누르면 종료합니다.", Toast.LENGTH_LONG).show();
                    mPressFirstBackKey = true;
                } else {
                    ActivityCompat.finishAffinity(this);

                }
            }

        }else {
            if (mPressFirstBackKey == false) {
                Toast.makeText(this, "한번 더 누르면 종료합니다.", Toast.LENGTH_LONG).show();
                mPressFirstBackKey = true;
            } else {
                ActivityCompat.finishAffinity(this);

            }
        }
    }
}
