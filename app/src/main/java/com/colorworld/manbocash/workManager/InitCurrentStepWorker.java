package com.colorworld.manbocash.workManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.annotation.Nonnull;

import static android.content.Context.MODE_PRIVATE;

public class InitCurrentStepWorker extends Worker {
    private Context mContext;
    private Activity mActivity;

    public InitCurrentStepWorker(
            @Nonnull Context context,
            @Nonnull WorkerParameters params) {
        super(context, params);

        mContext = context;
        mActivity = (Activity) context;

    }

    @NonNull
    @Override
    public Result doWork() {

        SharedPreferences sp = mContext.getSharedPreferences("stepCount", MODE_PRIVATE);

        int backgroundStepCount = sp.getInt("appOsCount_Background", 0);
        sp.edit().putInt("appOsCount", backgroundStepCount).apply();
        sp.edit().putInt("heartCoinCount", 0).apply();


        return Result.success();
    }

}
