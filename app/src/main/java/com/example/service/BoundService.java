package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Random;

public class BoundService extends Service {
    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN=0;
    private final int MAX=1000;
    private static final int GET_COUNT=0;

    private class RandomNumberRequstHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==GET_COUNT){
                Message messageSendRandomNumber=Message.obtain(null,GET_COUNT);
                messageSendRandomNumber.arg1=GetRandomNumber();
                try {
                    msg.replyTo.send(messageSendRandomNumber);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            super.handleMessage(msg);
        }
    }

//    class MyServiceBinder extends Binder{
//        public BoundService getSerivice(){
//            return BoundService.this;
//        }
//    }

    Messenger randomNumberMessanger=new Messenger(new RandomNumberRequstHandler());


//    private IBinder mBinder=new MyServiceBinder();

    public BoundService() {
    }

    @Override
    public void onCreate() {
        Log.d("StartRandomNumber","oncreate"+mRandomNumber);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("StartRandomNumber","on start method generated random number");

        mIsRandomGeneratorOn=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                StartRandomNumberGenerator();
            }
        }).start();
        return START_STICKY;
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
        if(intent.getPackage().equals("com.example.serversideapp")){
            return randomNumberMessanger.getBinder();

        }else{
            Toast.makeText(this, "wrong package", Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("StartRandomNumber","unbind");
        return super.onUnbind(intent);

    }

    private void StartRandomNumberGenerator(){
        while (mIsRandomGeneratorOn){
            try {
                Thread.sleep(1000);
                if(mIsRandomGeneratorOn){
                    mRandomNumber=new Random().nextInt(MAX)+MIN;
                    Log.d("StartRandomNumber ", "Thread id"+Thread.currentThread().getId()+"Random Number : "+mRandomNumber);

                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void StopRandomNumberGenerated(){
        mIsRandomGeneratorOn=false;
    }


    public int GetRandomNumber(){
        return mRandomNumber;
    }
}