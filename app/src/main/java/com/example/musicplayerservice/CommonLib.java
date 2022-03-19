package com.example.musicplayerservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * MusicPlayerService
 * created by
 *
 * @author gudimov
 * on 11 march 2022
 */
public abstract class CommonLib {
    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void log(Context context, String msg) {
        Log.d(context.getClass().getSimpleName(), msg);
    }
}
