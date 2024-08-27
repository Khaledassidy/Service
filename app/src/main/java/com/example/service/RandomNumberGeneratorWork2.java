package com.example.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Random;

public class RandomNumberGeneratorWork2 extends Worker {
    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN=0;
    private final int MAX=1000;
    Context context;
    WorkerParameters workerParameters;

    public RandomNumberGeneratorWork2(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
        this.workerParameters=workerParams;
        mIsRandomGeneratorOn=true;
    }

    public void StartRandomNumberGenerator(){
        int i=0;
        while (i<20&& !isStopped()){
            try {

                Thread.sleep(1000);

                mRandomNumber=new Random().nextInt(MAX)+MIN;
                i++;
                Log.d("workmanger", "worker 3 Thread id"+Thread.currentThread().getId()+"Random Number : "+mRandomNumber);


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @NonNull
    @Override
    public Result doWork() {
        StartRandomNumberGenerator();
        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.d("workmanger","worker 3 canceled");
    }
}
