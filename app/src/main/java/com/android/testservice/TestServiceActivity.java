package com.android.testservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TestServiceActivity extends AppCompatActivity {
    private MyService.MyBinder myBinder;
    private boolean isBinding;
    private String TAG = "TestServiceActivity";
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: " + name);
            isBinding = true;
            myBinder = (MyService.MyBinder) service;
            Toast.makeText(TestServiceActivity.this, "服务已绑定" + myBinder, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //解绑Service这个方法并不会回调 该方法只在Service 被破坏了或者被杀死的时候调用. 例如, 系统资源不足, 要关闭一些Services, 刚好连接绑定的 Service 是被关闭者之一,  这个时候onServiceDisconnected() 就会被调用.
            Log.i(TAG, "onServiceDisconnected: " + name);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_service);

        final Intent intent = new Intent(this, MyService.class);

        findViewById(R.id.btn_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService(intent, conn, Context.BIND_AUTO_CREATE);
            }
        });
        findViewById(R.id.btn_unbind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBinding) {
                    unbindService(conn);
                    isBinding = false;
                    Toast.makeText(TestServiceActivity.this, "服务已解绑", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.btn_get_state).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBinder == null) {
                    Toast.makeText(TestServiceActivity.this, "Service 已断开", Toast.LENGTH_SHORT).show();
                } else {
                    if (myBinder.isUnbind()) {
                        Toast.makeText(TestServiceActivity.this, "Service 已经解绑了，最后的Count值为：" + myBinder.getCount(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TestServiceActivity.this, "Service的Count值为：" + myBinder.getCount(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
