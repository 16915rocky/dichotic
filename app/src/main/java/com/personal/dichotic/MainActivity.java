package com.personal.dichotic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.personal.dichotic.ui.ScondActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_start_button)
    TextView tvStartButton;
    @BindView(R.id.tv_warn_headset)
    TextView tvWarnHeadset;
    private boolean isConnected=false;
    private AudioManager audoManager;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
                if(intent.hasExtra("state")) {
                    if(intent.getIntExtra("state", 0) == 0) {
                        tvWarnHeadset.setVisibility(View.VISIBLE);
                        tvStartButton.setBackgroundResource(R.drawable.oval_gray_bg);
                        isConnected=false;
                    } else if(intent.getIntExtra("state", 0) == 1) {
                        tvWarnHeadset.setVisibility(View.GONE);
                        tvStartButton.setBackgroundResource(R.drawable.oval_blue_bg);
                        isConnected=true;
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ScondActivity.class);
                    startActivity(intent);
                }
            }
        });
        checkIsWiredHeadsetOn();
        registerReceive();
    }

    private void registerReceive() {
        IntentFilter intentFilter = new IntentFilter();
        //步骤2：实例化LocalBroadcastManager的实例
        //LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        //步骤3：设置接收广播的类型
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        //步骤4：调用LocalBroadcastManager单一实例的registerReceiver（）方法进行动态注册
        registerReceiver(mReceiver, intentFilter);


    }

    private void checkIsWiredHeadsetOn() {
        audoManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if(audoManager.isWiredHeadsetOn()){
            tvWarnHeadset.setVisibility(View.GONE);
        }
    }

    /*public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
                    if (intent.hasExtra("state")) {
                        if (intent.getIntExtra("state", 0) == 0) {

                        } else if (intent.getIntExtra("state", 0) == 1) {
                            Toast.makeText(context, "headset connected", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        };
        registerReceiver(mReceiver, intentFilter);
    }*/


}
