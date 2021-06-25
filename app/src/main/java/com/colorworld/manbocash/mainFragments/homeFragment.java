package com.colorworld.manbocash.mainFragments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.TextDelegate;
import com.bumptech.glide.Glide;
import com.colorworld.manbocash.Const;
import com.colorworld.manbocash.MainActivity;
import com.colorworld.manbocash.R;
import com.colorworld.manbocash.changeLottieImage.LottieAdapter;
import com.colorworld.manbocash.changeLottieImage.LottieItem;
import com.colorworld.manbocash.changeLottieImage.LottieList;
import com.colorworld.manbocash.rank.RankActivity;
import com.colorworld.manbocash.room.MyInfoDatabase;
import com.colorworld.manbocash.room.dao.MyInfoDao;
import com.colorworld.manbocash.room.entity.MyInfoEntity;
import com.colorworld.manbocash.tutorial.tutorials.TutorialSupportActivity;
import com.colorworld.manbocash.util.AppExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.protobuf.StringValue;
import com.skyfishjy.library.RippleBackground;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.text.DecimalFormat;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class homeFragment extends Fragment implements SensorEventListener {

    public static homeFragment mStaticHFContext;

    public ImageView walkBg;

    public TextView mGreetingText;
    public TextView mDistance, mTime, mCal;
    public String myHeight, myWeight;
    public String todayDistance, todayTime, todayCal;


    private ImageView mRankBtn, mWalkMan, offerWallBtn;
    private ConstraintLayout.LayoutParams walkMan_params;
    private FrameLayout walkingGuide;
    private ConstraintLayout.LayoutParams walkingGuide_params;

    private LottieAnimationView walkingImage, playRewardADBtn;

    private SeekBar mWalkGage;
    private RippleBackground mRippleBackground;
    private boolean isClickEnabel = true;

    //step_counter
    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private TextView mCounterNum, mPlusOne_1, mPlusOne_2, mPlusOne_3, mPlusOne_4, mPlusOne_5;
    private int currentPlusOne = 1;

    private SharedPreferences mSp;
    private int mCurrentCount = 0, mOldCount;


    //counter
    private ImageView heart_imageView;
    private TextView heart_coinCount_text, mCash, mTapDescription;
    private ValueAnimator cashCountingAni, heartDownCountingAni;

    private LottieAnimationView mCongratulationAni;

    private DecimalFormat myFormatter;
    private DecimalFormat distanceFormatter;


    //로티 이미지 체인지
    private ConstraintLayout lottiePickerView;
    private List<LottieItem> lottieItems;
    private LottieList lottieList;
    private DiscreteScrollView lottiePicker;
    private InfiniteScrollAdapter<?> infiniteAdapter;
    private TextView cclText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_home, container, false);

        mStaticHFContext = this;
        mSp = this.getActivity().getSharedPreferences("stepCount", MODE_PRIVATE);


        myFormatter = new DecimalFormat("###,###");
        distanceFormatter = new DecimalFormat("###.#");


        SharedPreferences dataSp = this.getActivity().getSharedPreferences("manboData", MODE_PRIVATE);
        String greetingText = dataSp.getString("greeting", "안녕하세요. 반갑습니다!");


        offerWallBtn = (ImageView) rootView.findViewById(R.id.main_weather_btn);
        offerWallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) MainActivity.mStaticContext).openOfferWall();
            }
        });


        mGreetingText = (TextView) rootView.findViewById(R.id.greetingTV1);
        mGreetingText.setText(greetingText);


        mDistance = (TextView) rootView.findViewById(R.id.status_distance_tv);

        mTime = (TextView) rootView.findViewById(R.id.status_time_tv);

        mCal = (TextView) rootView.findViewById(R.id.status_calori_tv);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        MyInfoDatabase myInfoDatabase = MyInfoDatabase.getInstance(getContext());
        MyInfoDao myInfoDao = myInfoDatabase.getMyInfoDao();
        AppExecutors executors = new AppExecutors();


        executors.diskIO().execute(() -> {

            MyInfoEntity infoData = new MyInfoEntity();
            infoData = myInfoDao.selectInfoDataByUID(currentUser.getUid());


            if (infoData.height.equals("1") || infoData.weight.equals("1")) {
                myHeight = "170";
                myWeight = "65";
            } else {
                myHeight = infoData.height;
                myWeight = infoData.weight;
            }

        });


        walkBg = (ImageView) rootView.findViewById(R.id.home_walkbg);
        walkBgSizeCheck();

        mRankBtn = (ImageView) rootView.findViewById(R.id.main_lanking_btn);
        mRankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RankActivity.class);
                startActivity(intent);
            }
        });


        mWalkGage = (SeekBar) rootView.findViewById(R.id.home_gage);
        mWalkGage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mCounterNum = (TextView) rootView.findViewById(R.id.greetingTV2_2);

//        mWalkMan = (ImageView) rootView.findViewById(R.id.home_walking_man);
//        Glide.with(getActivity()).load(R.drawable.walkingman).into(mWalkMan);
//        walkMan_params = (ConstraintLayout.LayoutParams) mWalkMan.getLayoutParams();


        walkingGuide = (FrameLayout) rootView.findViewById(R.id.walkingGuideImage);
        walkingGuide_params = (ConstraintLayout.LayoutParams) walkingGuide.getLayoutParams();

        walkingImage = (LottieAnimationView) rootView.findViewById(R.id.home_walking_dog_man);
        walkingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lottiePickerView.setVisibility(View.VISIBLE);
            }
        });


        walkMan_params = (ConstraintLayout.LayoutParams) walkingImage.getLayoutParams();


        initPlusOneAndAnimation(rootView);

        mCash = (TextView) rootView.findViewById(R.id.home_cash_tv);
        exchangeCash(0);

        mCongratulationAni = (LottieAnimationView) rootView.findViewById(R.id.congratulation);
        mCongratulationAni.setVisibility(View.INVISIBLE);


        //최초 설치시에만 보이도록 수정
        final Animation alphaZero = AnimationUtils.loadAnimation(getContext(), R.anim.alpah_zero);
        alphaZero.setRepeatMode(1);
        alphaZero.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mTapDescription.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTapDescription.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mTapDescription = (TextView) rootView.findViewById(R.id.home_decription_tv);
        mTapDescription.setVisibility(View.GONE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTapDescription.startAnimation(alphaZero);
            }
        }, 10000);


        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        if (stepCountSensor == null) {
            Log.e("ios", "No Step Detect Sensor");
            Toast.makeText(getActivity(), "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }


        playRewardADBtn = (LottieAnimationView) rootView.findViewById(R.id.playAdBtn);
        playRewardADBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playRewardADBtn.setRepeatCount(1);
                playRewardADBtn.playAnimation();

                new MaterialDialog.Builder(getActivity())
                        .title("리워드광고")
                        .content("광고를 보시면 리워드에 따라\n하트가 캐시로 자동전환됩니다")
                        .positiveText("확인")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ((MainActivity) MainActivity.mStaticContext).showRewardAD();
                            }
                        })
                        .positiveColorRes(R.color.color_common)
                        .show();

            }
        });





        lottiePickerView = (ConstraintLayout) rootView.findViewById(R.id.selectLottiePicker);
        lottiePickerView.setVisibility(View.GONE);



        lottieList = LottieList.get();
        lottieItems = lottieList.getData();
        lottiePicker = rootView.findViewById(R.id.lottie_picker);
        lottiePicker.setOrientation(DSVOrientation.HORIZONTAL);
        lottiePicker.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);

                //저작권 표시 : Chris Gannon @LottieFiles
                cclText.setText(lottieItems.get(positionInDataSet).getCcl() + "@LottieFiles");

            }
        });
        infiniteAdapter = InfiniteScrollAdapter.wrap(new LottieAdapter(getApplicationContext(), lottieItems));
        lottiePicker.setAdapter(infiniteAdapter);
        lottiePicker.setItemTransitionTimeMillis(150);
        lottiePicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());


        LottieItem defaultWalkImage = lottieItems.get(mSp.getInt("walkingImage", 2));
        setWalkingImage(defaultWalkImage);

        cclText = (TextView) rootView.findViewById(R.id.ccl_text);
        cclText.setText(lottieItems.get(0).getCcl() + "@LottieFiles");

//warning
//        mSp.edit().putInt("heartCoinCount", 1000).apply();

        return rootView;
    }


    public void setLottieImage(int position) {
        int realPosition = infiniteAdapter.getRealPosition(lottiePicker.getCurrentItem());
        mSp.edit().putInt("walkingImage", realPosition).apply();

        LottieItem current = lottieItems.get(realPosition);
        setWalkingImage(current);
        lottiePickerView.setVisibility(View.GONE);
    }



    public void setWalkingImage(LottieItem lottieItem) {
        walkingImage.setAnimation(lottieItem.getImage());
        walkingImage.playAnimation();


        if (lottieItem.isFlip()) {
            walkingImage.setScaleX(-1);
        }else {
            walkingImage.setScaleX(1);
        }


        switch (lottieItem.getId()) {
            case Const.lottieImageType.DOGWITHMAN :
                setLottieFrame(130,130);
                walkMan_params.verticalBias = 0.65f;
                walkingImage.setLayoutParams(walkMan_params);
                break;
            case Const.lottieImageType.WALKINGDOG :
                setLottieFrame(60,60);
                walkMan_params.verticalBias = 0.5f;
                walkingImage.setLayoutParams(walkMan_params);
                break;
            case Const.lottieImageType.COUPLE :
                setLottieFrame(140,140);
                walkMan_params.verticalBias = 0.5f;
                walkingImage.setLayoutParams(walkMan_params);
                break;
            case Const.lottieImageType.DOGWITHWOMAN :
                setLottieFrame(100,100);
                walkMan_params.verticalBias = 0.5f;
                walkingImage.setLayoutParams(walkMan_params);
                break;
            case Const.lottieImageType.DEADPOOL :
                setLottieFrame(120,120);
                walkMan_params.verticalBias = 0.5f;
                walkingImage.setLayoutParams(walkMan_params);
                break;
            case Const.lottieImageType.REDDRESS :
                setLottieFrame(85,85);
                walkMan_params.verticalBias = 0.5f;
                walkingImage.setLayoutParams(walkMan_params);
                break;
            case Const.lottieImageType.ROBOT :
                setLottieFrame(95,95);
                walkMan_params.verticalBias = 0.5f;
                walkingImage.setLayoutParams(walkMan_params);
                break;
            default:

                break;
        }
    }


    public void setLottieFrame(int width, int height) {
        final int widthDP = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        final int heightDP = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());

        walkingImage.requestLayout();
        walkingImage.getLayoutParams().width = widthDP;
        walkingImage.getLayoutParams().height = heightDP;

    }


    public void animationBtnWithCompleteLoadRewardAD() {
        playRewardADBtn.setRepeatCount(2);
        playRewardADBtn.playAnimation();

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        Log.e("ios", "처음에 탄다 : " + event.values[0]);

        mOldCount = (int) event.values[0];

        Log.e("step", "mOldCount : " + mOldCount);

        if (mSp.getInt("appOsCount", 0) == 0) {
            mSp.edit().putInt("appOsCount", mOldCount).apply();
        }

        int oldCount = mSp.getInt("appOsCount", 0);

        Log.e("ios", "======================homeFragment appOsCount : " + oldCount + ", mOldCount : " + mOldCount);


        //step counter 센서는 핸드폰을 껐다가 키면 0가 된다. 폰에 저장된 appOsCount 보다 센서 카운트가 작으면 껐다가 다시 킨걸로 간주하고 재설정 해줌
        if (mOldCount > oldCount) {
            mCurrentCount = mOldCount - oldCount;
        } else {
            mSp.edit().putInt("appOsCount", mOldCount).apply();
            int newOldCount = mSp.getInt("appOsCount", 0);
            mCurrentCount = mOldCount - newOldCount;
        }

        if (mCurrentCount > 10000) {
            mCurrentCount = 10000;
        }


        String formattedStringPrice = myFormatter.format(mCurrentCount);


        float myDistance = ((Float.parseFloat(myHeight) - 100.f) * mCurrentCount) / 100000.f;

        Log.e("ios", "Integer.parseInt(myHeight)  - 100) *  mCurrentCount / 100000 : " + myDistance);

        todayDistance = distanceFormatter.format(myDistance);  /*70cm = 0.0007km*/
        mDistance.setText(todayDistance + " km");


        long myCal = Math.round(Integer.parseInt(myWeight) * (float) (mCurrentCount / 10000.f) * 5.5);


//        Log.e("ios", "myCal : " + myCal + "\nmyWeight : " + myWeight + "\nmCurrentCount : " + mCurrentCount + "\nInteger.parseInt(myWeight) : " + Integer.parseInt(myWeight) + "\n(mCurrentCount/10000) : " + (float)(mCurrentCount/10000.f) + "\n");

        mCal.setText(String.valueOf(myCal) + " 칼로리");


        mCounterNum.setText(formattedStringPrice);

        float walkCountBias = (float) mCurrentCount;

        if (walkCountBias <= 290) {
            mWalkGage.setProgress(290);
            walkingGuide_params.horizontalBias = 0.f / 10000.f;
            walkingImage.setLayoutParams(walkMan_params);


//            walkMan_params.horizontalBias = 0.f / 10000.f;
////            mWalkMan.setLayoutParams(walkMan_params);
//            walkingImage.setLayoutParams(walkMan_params);
        } else {
            mWalkGage.setProgress(mCurrentCount);
            walkingGuide_params.horizontalBias = walkCountBias / 10000.f;
            walkingImage.setLayoutParams(walkMan_params);


//            walkMan_params.horizontalBias = walkCountBias / 10000.f;
////            mWalkMan.setLayoutParams(walkMan_params);
//            walkingImage.setLayoutParams(walkMan_params);
        }



        /*
         * 전환가능한 걸음수만 계산 및 하트 카운트 업데이트
         *
         * convertedCount : 전환한 걸음수
         *
         * convertabelCount = mCurrentCount - convertedCount : 전환 가능한 걸음수
         *
         * convertingCoin = convertableCount / 5 : 코인으로 전환
         *
         * convertedCount = convertedCount + (convertableCount - convertableCount%5) : 전환된 걸음수 업데이트
         *
         * heartCoinCount = heartCoinCount + convertingCoin : 하트 카운트 업데이트
         *
         * mCurrentCount < convertedCount || mCurrent == 0 : 핸드폰을 리부팅해서 현재 카운트가 0 이니까 convertedCount = 0 으로 업데이트
         *
         *
         * */


        int convertedCount = mSp.getInt("convertedCount", 0);

        if (mCurrentCount < convertedCount || mCurrentCount == 0)
            mSp.edit().putInt("convertedCount", 0).apply();


        int convertableCount = mCurrentCount - convertedCount;
        int convertingCoin = (int) Math.floor(convertableCount / 5); // n.0 으로 나와서 캐스팅

        Log.e("step", "\n mCurrentCount : " + mCurrentCount + "\n convertedCount : " + convertedCount + "\n convertableCount : " + convertableCount + "\n convertingCoin : " + convertingCoin);

        convertedCount += (convertableCount - convertableCount % 5);
        mSp.edit().putInt("convertedCount", convertedCount).apply();

        int heartCoin = mSp.getInt("heartCoinCount", 0);

        Log.e("step", "\n 1 - heartCoin : " + heartCoin);

        heartCoin += convertingCoin;

        Log.e("step", "\n 2 - heartCoin : " + heartCoin);

        mSp.edit().putInt("heartCoinCount", heartCoin).apply();

        changeStatus_heart(heartCoin);


//        Log.e("ios", "2900/10000 = " + 2900.f / 10000.f + "    , walkCountBias/10000.f = " + walkCountBias / 10000.f + "    , walkCountBias = " + walkCountBias);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("ios", "=========================================================================================== home onResume");

        long oldCash = mSp.getLong("myCash", 0);
        long rewardCash = mSp.getLong("rewardCash", 0);

        boolean isReward = rewardCash - oldCash > 0 ? true : false;

        if (isReward) {
            mCongratulationAni.setVisibility(View.VISIBLE);
            mSp.edit().putLong("rewardCash", 0).apply();
            exchangeCash(rewardCash - oldCash);
        }

        int heartDownCount = mSp.getInt("googleReward", 0);

        if (heartDownCount > 0) {
            exchangeHeartToCoin(heartDownCount);
            mSp.edit().putInt("googleReward", 0).apply();
        }


//        sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onPause() {
        super.onPause();
//        sensorManager.unregisterListener(this);

    }


    public void readMyBodyData() {
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("manboData", MODE_PRIVATE);
//        boolean isFirstInstall = sharedPreferences.getBoolean("firstInstall", true);
//
//        if (!isFirstInstall) {


//        }
    }


    public void initPlusOneAndAnimation(View view) {

        heart_coinCount_text = (TextView) view.findViewById(R.id.home_heart_tv);

        final Animation upAnimation_1 = AnimationUtils.loadAnimation(getContext(), R.anim.ani);
        upAnimation_1.setRepeatMode(1);
        upAnimation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mPlusOne_1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPlusOne_1.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final Animation upAnimation_2 = AnimationUtils.loadAnimation(getContext(), R.anim.ani);
        upAnimation_2.setRepeatMode(1);
        upAnimation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mPlusOne_2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPlusOne_2.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final Animation upAnimation_3 = AnimationUtils.loadAnimation(getContext(), R.anim.ani);
        upAnimation_3.setRepeatMode(1);
        upAnimation_3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mPlusOne_3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPlusOne_3.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final Animation upAnimation_4 = AnimationUtils.loadAnimation(getContext(), R.anim.ani);
        upAnimation_4.setRepeatMode(1);
        upAnimation_4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mPlusOne_4.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPlusOne_4.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final Animation upAnimation_5 = AnimationUtils.loadAnimation(getContext(), R.anim.ani);
        upAnimation_5.setRepeatMode(1);
        upAnimation_5.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mPlusOne_5.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPlusOne_5.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mPlusOne_1 = (TextView) view.findViewById(R.id.home_plusone_1);
        mPlusOne_1.setVisibility(View.INVISIBLE);

        mPlusOne_2 = (TextView) view.findViewById(R.id.home_plusone_2);
        mPlusOne_2.setVisibility(View.INVISIBLE);

        mPlusOne_3 = (TextView) view.findViewById(R.id.home_plusone_3);
        mPlusOne_3.setVisibility(View.INVISIBLE);

        mPlusOne_4 = (TextView) view.findViewById(R.id.home_plusone_4);
        mPlusOne_4.setVisibility(View.INVISIBLE);

        mPlusOne_5 = (TextView) view.findViewById(R.id.home_plusone_5);
        mPlusOne_5.setVisibility(View.INVISIBLE);


        mRippleBackground = (RippleBackground) view.findViewById(R.id.heart_ripple_content);
        heart_imageView = (ImageView) view.findViewById(R.id.centerImage);
        heart_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClickEnabel) {
                    //카운터에서 코인으로

                    int heartCoin = mSp.getInt("heartCoinCount", 0);
                    if (heartCoin > 0) {
                        downCountHeartPoint(1);

                        if (currentPlusOne == 1) {
                            mPlusOne_1.startAnimation(upAnimation_1);
                            currentPlusOne = 2;
                        } else if (currentPlusOne == 2) {
                            mPlusOne_2.startAnimation(upAnimation_2);
                            currentPlusOne = 3;
                        } else if (currentPlusOne == 3) {
                            mPlusOne_3.startAnimation(upAnimation_3);
                            currentPlusOne = 4;
                        } else if (currentPlusOne == 4) {
                            mPlusOne_4.startAnimation(upAnimation_4);
                            currentPlusOne = 5;
                        } else if (currentPlusOne == 5) {
                            mPlusOne_5.startAnimation(upAnimation_5);
                            currentPlusOne = 1;
                            ((MainActivity) MainActivity.mStaticContext).openadverstising();
                            enableBtns(false);
                        }
                    }
                } else {
                    Log.e("ios", "광고");
                    //TODO 광고가 뜨면 코인으로 전환 : 광고 fail시엔 전환 안됨


                }
            }
        });

    }

    private void downCountHeartPoint(int toCoin) {
        int heartCoin = mSp.getInt("heartCoinCount", 0);
        heartCoin -= toCoin;
        mSp.edit().putInt("heartCoinCount", heartCoin).apply();
        changeStatus_heart(heartCoin);


        //캐시 올리는 메서드 작성
        //광고가 뜨면 코인으로 전환 : 광고 fail시엔 전환 안됨
    }

    public void exchangeCash(long coin) {
        long nowCash = mSp.getLong("myCash", 0);

        long duration = 0;
        if (coin < 100) {
            duration = 600;
        } else if (coin < 600) {
            duration = 600 * coin / 100;
        } else {
            duration = 5000;
        }

        Log.e("ios", "nowCash : " + nowCash + " ,  nowCash + coin : " + (nowCash + coin));

        if (cashCountingAni != null) cashCountingAni = null;

        cashCountingAni = ValueAnimator.ofInt((int) nowCash, (int) nowCash + (int) coin);
        cashCountingAni.setDuration(duration);
        cashCountingAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCash.setText(animation.getAnimatedValue().toString());
            }
        });

        cashCountingAni.start();

        nowCash += coin;
//        mCash.setText(String.valueOf(nowCash));
        mSp.edit().putLong("myCash", nowCash).apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCongratulationAni.setVisibility(View.GONE);
            }
        }, duration);

    }

    public void exchangeHeartToCoin(int reward) {
        if (cashCountingAni != null) cashCountingAni = null;


        int heartToCoin = (int) (reward / 5);

        long coinDuration = 0;
        if (heartToCoin < 100) {
            coinDuration = 600;
        } else if (heartToCoin < 600) {
            coinDuration = 600 * heartToCoin / 100;
        } else {
            coinDuration = 5000;
        }

        long nowCash = mSp.getLong("myCash", 0);
        cashCountingAni = ValueAnimator.ofInt((int) nowCash, (int) nowCash + heartToCoin);
        cashCountingAni.setDuration(coinDuration);
        cashCountingAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCash.setText(animation.getAnimatedValue().toString());
            }
        });

        cashCountingAni.start();

        mSp.edit().putLong("myCash", nowCash + heartToCoin).apply();


        long heartDuration = 0;
        if (reward < 100) {
            heartDuration = 600;
        } else if (reward < 600) {
            heartDuration = 600 * heartToCoin / 100;
        } else {
            heartDuration = 5000;
        }


        int nowHeartPoint = mSp.getInt("heartCoinCount", 0);

        int downHeartPoint = reward > nowHeartPoint ? nowHeartPoint : reward;

        heartDownCountingAni = ValueAnimator.ofInt(nowHeartPoint, nowHeartPoint - downHeartPoint);
        heartDownCountingAni.setDuration(heartDuration);
        heartDownCountingAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                heart_coinCount_text.setText(animation.getAnimatedValue().toString());
            }
        });

        heartDownCountingAni.start();

        mSp.edit().putInt("heartCoinCount", nowHeartPoint - downHeartPoint).apply();

    }


    public void changeStatus_heart(int heartCount) {

        heart_coinCount_text.setText(String.valueOf(heartCount));

//        if (heartCount < 5) {
//            heart_imageView.setImageResource(R.drawable.fragment_home_heart_gray);
//            mRippleBackground.stopRippleAnimation();
//            mTapDescription.setVisibility(View.INVISIBLE);
//
//        } else {
        heart_imageView.setImageResource(R.drawable.fragment_home_heart_red);
        mRippleBackground.startRippleAnimation();
//        }


    }

    public void fetchGreetingText(String str) {
        mGreetingText.setText(str);
    }

    public void enableBtns(boolean enable) {
        mRankBtn.setEnabled(enable);
        isClickEnabel = enable;
    }

    public void walkBgSizeCheck() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();

        float width = (float) dm.widthPixels;
        float height = (float) dm.heightPixels;

        float ratio = height / width;


        Log.e("ios", "w : " + width + " , h : " + height + " , ratio : " + ratio + " , dpi : " + dm.densityDpi);

// && dm.densityDpi > 480
        if (ratio > 1.80) { // s8, s9 +
            Log.e("ios", "ratio 1.8+");
            walkBg.setImageResource(R.drawable.fragment_home_walkbg);
        } else { //s6 edge -
            Log.e("ios", "ratio 1.8-");
            walkBg.setImageResource(R.drawable.fragment_home_walkbg_2560);
        }

    }


}
