package com.android.testservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 2019/7/11 22:46
 */
public class MyService extends Service {
    private int count;
    private boolean quit;
    private String TAG = "MyService";

    @Override
    public void onCreate() {
        super.onCreate();
        quit = false;
        System.out.println("Service isCreated");
        Log.i(TAG, "Service onCreate: ");
        new Thread() {
            @Override
            public void run() {
                while (!quit) {
                    SystemClock.sleep(1000);
                    count++;
                }
            }
        }.start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        quit = true;
        Log.i(TAG, "Service onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service onDestroy: ");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public int getCount() {
            return count;
        }
        public boolean isUnbind() {
            return quit;
        }
    }
}
