package com.example.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.media.MediaPlayer;
import android.util.Log;

public class jobservies extends JobService {
    MediaPlayer mediaPlayer;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("jobservies","onstartjob");
        mediaPlayer=MediaPlayer.create(this,R.raw.music);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                mediaPlayer.release();
                jobFinished(params,false);

            }
        });
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("jobservies","onstopjob");
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
        return true;
    }
}
