package com.colorworld.manbocash.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.colorworld.manbocash.MainActivity;
import com.colorworld.manbocash.R;
import com.colorworld.manbocash.room.StepsDatabase;
import com.colorworld.manbocash.room.dao.StepsDao;
import com.colorworld.manbocash.room.entity.MySteps;
import com.colorworld.manbocash.util.AppExecutors;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StepService extends Service implements SensorEventListener {
    public static Intent serviceIntent = null;
    private static int NOTIF_ID = 2;

    private Context mContext;
    private Activity mActivity;
    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private NotificationManager notificationManager;

    private StepsDatabase stepsDatabase;
    private StepsDao stepsDao;
    private AppExecutors executors;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("ios", "StepService : onCreate");

        mContext = getApplicationContext();
//        mActivity = (Activity) mContext;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("ios", "StepService : onStartCommand");


        serviceIntent = intent;

        //스탭센서 초기화
        sensorManager = (SensorManager) getApplication().getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //룸 DB 초기화
        stepsDatabase = StepsDatabase.getInstance(mContext);
        stepsDao = stepsDatabase.getMyStepsDao();
        executors = new AppExecutors();


        //자정 리셋로직 구현


        startForeground(NOTIF_ID, getStepNotification("만보캐시가 시작되었습니다."));


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        //메인으로 들어왔을때 서비스 중지 할 필요없음 계속 서비스
//        sensorManager.unregisterListener(this);


        //폰 부팅될때 알람등록


//        final Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.SECOND, 3);
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent,0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);



    }

    private Notification getStepNotification(String content) {
        Notification notification = null;


        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            String NOTIFICATION_CHANNEL_ID = "com.colorworld.manbocash";

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(content)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setContentIntent(pendingIntent)
                    .build();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String channelName = "StepCounter Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            chan.setShowBadge(false);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

        }

        return notification;



//        intent1 = new Intent(this,update.class);
//        pintent1 = PendingIntent.getActivity(this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        intent2 = new Intent(this, main.class);
//        pintent2 = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        contentView = new RemoteViews(getPackageName(), R.layout.notify);
//        contentView.setTextViewText(R.id.notifyLocation, location);
//        contentView.setTextViewText(R.id.notifyWeather, "날씨 : " + curWfKor + "," + curTemp);
//        contentView.setTextViewText(R.id.notifyFinddust, "미세먼지 : " + finddust);
//        contentView.setTextViewText(R.id.notifyTime, updateTime);
//        contentView.setOnClickPendingIntent(R.id.notifyReflash, pintent1);
//
//        mBuilder=new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.genie_icon)
//                .setContent(contentView)
//                .setContentIntent(pintent2);
//
//        mManager.notify(notifyID, mBuilder.build());



    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        int currentCount = (int) event.values[0];

        int displayCount = 0;

        SharedPreferences sp = getSharedPreferences("stepCount", MODE_PRIVATE);
        sp.edit().putInt("appOsCount_Background",  currentCount).apply();

        int oldCount = sp.getInt("appOsCount", 0);

        Log.e("ios", "======================StepService appOsCount : " + oldCount + " ,  os currentCount : " + currentCount);


        displayCount = currentCount - oldCount;

        if (displayCount > 10000) {
            displayCount = 10000;
        }

        startForeground(NOTIF_ID, getStepNotification("현재 걸음수 : " + displayCount));


        Log.e("ios", "StepService : currentCount : " + currentCount);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
