package com.mblhcmute.messageexampleboundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Messenger mMessenger;
    private boolean isServiceConnected = false;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mMessenger = new Messenger(iBinder);
            isServiceConnected = true;
            //send message play music
            sendMessagePlayMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMessenger = null;
            isServiceConnected  = false;
        }
    };

    private void sendMessagePlayMusic() {
        Message message = Message.obtain(null,MusicService.MSG_PLAY_MUSIC, 0,0);
        try {
            mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStartService = findViewById(R.id.btnStartService);
        Button btnStopService = findViewById(R.id.btnStopService);

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickedStartService();
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickedStopService();
            }
        });
    }

    private void onClickedStartService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void onClickedStopService() {
        if(isServiceConnected) {
            unbindService(mServiceConnection);
            isServiceConnected = false;
        }
    }
}