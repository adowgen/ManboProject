package com.colorworld.manbocash.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;


import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Log.e("ios", "========================ManboBroadcastReceiver==========");

        //자정알람
        SharedPreferences sp = context.getSharedPreferences("stepCount", MODE_PRIVATE);

        int backgroundStepCount = sp.getInt("appOsCount_Background", 0);
        sp.edit().putInt("appOsCount", backgroundStepCount).apply();
        sp.edit().putInt("heartCoinCount", 0).apply();
        sp.edit().putInt("convertedCount", 0).apply();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            AlarmManager resetAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent resetIntent = intent;
            PendingIntent resetPintent = PendingIntent.getBroadcast(context, 24, resetIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            // 자정 시간
            Calendar resetCal = Calendar.getInstance();
            resetCal.setTimeInMillis(System.currentTimeMillis());
            resetCal.set(Calendar.HOUR_OF_DAY, 23);
            resetCal.set(Calendar.MINUTE, 55);

            resetAlarmManager.setExact(AlarmManager.RTC_WAKEUP, resetCal.getTimeInMillis(), resetPintent);
        }

    }


}

