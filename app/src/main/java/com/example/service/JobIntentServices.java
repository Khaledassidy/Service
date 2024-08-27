package com.example.service;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.util.Random;

public class JobIntentServices extends JobIntentService {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN=0;
    private final int MAX=1000;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d("StartRandomNumber","on start method generated random number");
        mIsRandomGeneratorOn=true;
        StartRandomNumberGenerator(intent.getStringExtra("starter"));

    }



    public static void enqueuwork(Context context,Intent intent){
        enqueueWork(context,JobIntentServices.class,101,intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StopRandomNumberGenerated();
        Log.d("StartRandomNumber","Destroy");
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("StartRandomNumber","onbind");
            return super.onBind(intent);



    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("StartRandomNumber","unbind");
        return super.onUnbind(intent);

    }

    private void StartRandomNumberGenerator(String identifier){
        for (int i=0;i<5;i++){
            try {

                Thread.sleep(1000);

                    mRandomNumber=new Random().nextInt(MAX)+MIN;
                    Log.d("StartRandomNumber ", "Thread id"+Thread.currentThread().getId()+"Random Number : "+mRandomNumber+identifier);


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Log.d("StartRandomNumber ", "servies stoped"+identifier);

        stopSelf();

    }

    @Override
    public boolean onStopCurrentWork() {
        return super.onStopCurrentWork();
    }

    private void StopRandomNumberGenerated(){
        mIsRandomGeneratorOn=false;
    }


    public int GetRandomNumber(){
        return mRandomNumber;
    }
}
