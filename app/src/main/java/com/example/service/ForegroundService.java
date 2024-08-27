package com.example.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.security.Provider;

public class ForegroundService extends Service {
    private static final String CHANEL_ID = "chanel_id";
    MediaPlayer mediaPlayer;
    public ForegroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=MediaPlayer.create(this,R.raw.music);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopSelf();
            }
        });
        Log.d("Service Life Cycle","OnCreate Service");
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1,displaynotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
        }else{
            startForeground(1,displaynotification());

        }
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }


        Log.d("Service Life Cycle","OnStart Service");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Log.d("Service Life Cycle","OnDestroy Service");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Notification displaynotification() {
        Intent intent = new Intent(this, ForegroundService.class);
        Intent intent1 = new Intent(this, ForegroundService.class);

        PendingIntent pi = null;
        PendingIntent pi1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int flags = PendingIntent.FLAG_IMMUTABLE;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                flags |= PendingIntent.FLAG_UPDATE_CURRENT;
            }
            pi = PendingIntent.getService(getApplicationContext(), 0, intent,flags);
            pi1 = PendingIntent.getService(getApplicationContext(), 0, intent1,flags);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, "CHANNEL display name", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("my channel description");
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), CHANEL_ID);
        builder.setContentTitle("Marwan khoury")
                .setSmallIcon(R.drawable.baseline_library_music_24)
                .setContentText("Ew3edene")
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(0, "resume", pi)
                .addAction(0, "stop", pi1);

        return builder.build();


    }
}