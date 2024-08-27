package com.example.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {
    TextView diplay_randomnumber;
    Button Start_Service,Stop_Service,BindService,unbindService,GetRandomNumber;
    private BoundService boundService;
    private boolean isSeviceBound;
    private ServiceConnection serviceConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        diplay_randomnumber=findViewById(R.id.random_numberdisp);
        Start_Service=findViewById(R.id.startservice);
        Stop_Service=findViewById(R.id.stopservice);
        BindService=findViewById(R.id.bind);
        unbindService=findViewById(R.id.unbind);
        GetRandomNumber=findViewById(R.id.getrandomnumber);
        serviceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
//                BoundService.MyServiceBinder binder=(BoundService.MyServiceBinder)service;
//                boundService=binder.getSerivice();
                isSeviceBound=true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isSeviceBound=false;

            }
        };

        BindService.setOnClickListener(v->{
            Intent intent=new Intent(this,BoundService.class);
            bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
        });

        unbindService.setOnClickListener(v->{
            if(isSeviceBound){
                unbindService(serviceConnection);
                isSeviceBound=false;

            }
        });

        GetRandomNumber.setOnClickListener(v->{
            if(isSeviceBound){
               int x= boundService.GetRandomNumber();
               diplay_randomnumber.setText(String.valueOf(x));
            }else{
                diplay_randomnumber.setText("Service not bound");

            }
        });

        Start_Service.setOnClickListener(v->{
            Intent intent=new Intent(this,BoundService.class);
            startService(intent);


        });

        Stop_Service.setOnClickListener(v->{
            Intent intent=new Intent(this,BoundService.class);
            stopService(intent);
        });



    }
}