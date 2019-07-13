package com.android.testservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.testservice.aidl.ICaculate;

public class TestAIDLActivity extends AppCompatActivity {

    public String TAG = "TestAIDLActivity";
    private EditText editA;
    private EditText editB;
    private EditText editResult;

    private ICaculate iCaculate;
    private boolean isBinding;

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: ");
            //使用BindInterface.Stub.asInterface()方法将传入的IBinder对象传换成了BindInterface对象
            iCaculate = ICaculate.Stub.asInterface(service);
            isBinding = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //解绑Service这个方法并不会回调 该方法只在Service 被破坏了或者被杀死的时候调用. 例如, 系统资源不足, 要关闭一些Services, 刚好连接绑定的 Service 是被关闭者之一,  这个时候onServiceDisconnected() 就会被调用.
            Log.i(TAG, "onServiceDisconnected: ");
            isBinding = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_aidl);

        editA = findViewById(R.id.edit_a);
        editB = findViewById(R.id.edit_b);
        editResult = findViewById(R.id.edit_result);

        bindService(new Intent(this, AIDLService.class), conn, Context.BIND_AUTO_CREATE);

        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBinding && iCaculate != null) {
                    String numbaerA = editA.getText().toString().trim();
                    if (TextUtils.isEmpty(numbaerA)) {
                        Toast.makeText(TestAIDLActivity.this, "请输入数字A", Toast.LENGTH_SHORT).show();
                        editA.requestFocus();
                        return;
                    }
                    String numbaerB = editB.getText().toString().trim();
                    if (TextUtils.isEmpty(numbaerB)) {
                        Toast.makeText(TestAIDLActivity.this, "请输入数字B", Toast.LENGTH_SHORT).show();
                        editB.requestFocus();
                        return;
                    }
                    try {
                        int result = iCaculate.add(Integer.valueOf(numbaerA), Integer.valueOf(numbaerB));
                        editResult.setText(String.valueOf(result));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBinding && conn != null) {
            unbindService(conn);
        }

    }
}
