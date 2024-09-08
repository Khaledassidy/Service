package com.example.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Random;


public class RandomNumberGeneratorWork extends Worker {
    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN=0;
    private final int MAX=1000;
    Context context;
    private static int NOTIFICATION_ID=10;
    NotificationManager notificationManager;


    public RandomNumberGeneratorWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.context=context;

    }

    public void StartRandomNumberGenerator(){
        Data data=getInputData();
        int count_limit=data.getInt("max_limit",0);
        int i=0;
        while (i<count_limit&& !isStopped()) {
            try {

                Thread.sleep(1000);

                mRandomNumber = new Random().nextInt(MAX) + MIN;
                i++;
                Log.d("workmanger", "worker 2 Thread id" + Thread.currentThread().getId() + "Random Number : " + mRandomNumber);


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @NonNull
    @Override
    public Result doWork() {
        setForegroundAsync(createForeGroundInfo("Random Number Generator Running"));
        StartRandomNumberGenerator();
        Data data=new Data.Builder()
                .putString("msg","task done succecfully")
                .build();

        return Result.success(data);
    }

    @Override
    public void onStopped() {
        super.onStopped();

        Log.d("workmanger","worker 1 canceled");
    }

    private ForegroundInfo createForeGroundInfo(String message){
        Notification notification=getNotification(MainActivity.class,message,1,false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return new ForegroundInfo(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
        } else {
            return new ForegroundInfo(NOTIFICATION_ID, notification);
        }
    }

    public Notification getNotification(Class targetNotificationActivity,String title,int priority,boolean autocancel){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel("123","chanelname", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("dsdsds");
            notificationManager.createNotificationChannel(notificationChannel);

        }
        Intent intent=new Intent(context,targetNotificationActivity);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),"123")
                .setSmallIcon(R.drawable.baseline_library_music_24)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        return builder.build();
    }
}
