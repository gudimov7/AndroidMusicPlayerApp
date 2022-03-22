package com.example.musicplayerservice.services;

import static com.example.musicplayerservice.CommonLib.toast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.musicplayerservice.PlayerActivity;

public class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null) {
            PlayerActivity pa = new PlayerActivity();
            switch (intent.getAction()) {
                case "pause" : pa.pause(); break;
                case "stop" : pa.stop(); break;
                case "next" : pa.next(); break;
                case "prev" : pa.prev(); break;
                default: toast(context, "Whats that?" + intent.getAction());
            }
        }

    }
}