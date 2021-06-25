package com.colorworld.manbocash.workManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.colorworld.manbocash.room.StepsDatabase;
import com.colorworld.manbocash.room.dao.StepsDao;
import com.colorworld.manbocash.room.entity.MySteps;
import com.colorworld.manbocash.util.AppExecutors;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

import static android.content.Context.MODE_PRIVATE;

public class SaveStepCountWorker extends Worker {

    private StepsDatabase myStepDB;
    private StepsDao myStepdDao;

    private Context mContext;

    public SaveStepCountWorker(
            @Nonnull Context context,
            @Nonnull WorkerParameters params) {
        super(context, params);

        mContext = context;


    }

    @NonNull
    @Override
    public Result doWork() {

        //디비에 저장
        //저장 완료 후 Result.success();

        saveStepData();



        return null;
    }


    private void saveStepData() {

        Log.e("ios", "========================SaveStepCountWorker========== 1");

        //TODO 시간별로 룸 data 저장
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH", mContext.getResources().getConfiguration().locale);
        String compareTime_A = sdf.format(date);

        SharedPreferences sp = mContext.getSharedPreferences("stepCount", MODE_PRIVATE);
        int forRealStepCount = sp.getInt("saveLocalDBCount", sp.getInt("appOsCount_Background", 0));
        int totalOsCount = sp.getInt("appOsCount_Background", 0);

        //reset phone 일때
        if (forRealStepCount > totalOsCount) {
            forRealStepCount = totalOsCount;
        }
        int realCount = totalOsCount - forRealStepCount;

        sp.edit().putInt("saveLocalDBCount", totalOsCount).apply();

        MySteps myStep = new MySteps();
        myStep.step = realCount;
        myStep.date = compareTime_A;
        myStep.isSuccess = false;

        StepsDatabase stepsDatabase = StepsDatabase.getInstance(mContext);
        StepsDao stepsDao = stepsDatabase.getMyStepsDao();
        AppExecutors executors = new AppExecutors();

        executors.diskIO().execute(() -> {
            stepsDao.insertCurrentStep(myStep);
        });

    }


}
