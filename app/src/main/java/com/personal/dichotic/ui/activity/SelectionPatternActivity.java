package com.personal.dichotic.ui.activity;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.personal.dichotic.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rocky on 2018/4/16.
 */

public class FourthActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.tv_item1)
    TextView tvItem1;
    @BindView(R.id.tv_item2)
    TextView tvItem2;
    @BindView(R.id.tv_item3)
    TextView tvItem3;
    @BindView(R.id.tb_find)
    Toolbar tbFind;
    @BindView(R.id.tv_des)
    TextView tvDes;
    private AudioManager audio;
    private boolean isFirstTone=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstTone = getIntent().getBooleanExtra("isFirstTone",true);
        setContentView(R.layout.activity_fourth);
        ButterKnife.bind(this);
        tbFind.setNavigationIcon(R.mipmap.ic_arrow_left);
        setSupportActionBar(tbFind);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initView();
        initEvent();
        audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
    }

    private void initView() {
        if(isFirstTone){
            tvDes.setText("普通话CV单音节词(第一声调)");
            tvItem1.setText("自由模式\n(约3分钟)");
            tvItem2.setText("注意模式(左)\n(约3分钟)");
            tvItem3.setText("注意模式(右)\n(约3分钟)");
        }else{
            tvDes.setText("普通话CV单音节词(四种声调)");
            tvItem1.setText("自由模式\n(约10分钟)");
            tvItem2.setText("注意模式(左)\n(约10分钟)");
            tvItem3.setText("注意模式(右)\n(约10分钟)");
        }

    }

    private void initEvent() {
        tvItem1.setOnClickListener(this);
        tvItem2.setOnClickListener(this);
        tvItem3.setOnClickListener(this);
        tbFind.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_item1:
                intent.putExtra("title","自由模式");
                intent.putExtra("isFirstTone",isFirstTone);
                intent.putExtra("DesContent","在本项测试中，您将会在左右耳机中同时听到不同的声音，请选择您觉得相比较而言更清楚的声音。");
                showdialog(intent);
                break;
            case R.id.tv_item2:
                intent.putExtra("title","注意模式(左)");
                intent.putExtra("isFirstTone",isFirstTone);
                intent.putExtra("DesContent","在本项测试中，您将会在左右耳机中同时听到不同的声音，请选择出您在左边耳机中听到的声音，忽略右边耳机。");
                showdialog(intent);
                break;
            case R.id.tv_item3:
                intent.putExtra("title","注意模式(右)");
                intent.putExtra("isFirstTone",isFirstTone);
                intent.putExtra("DesContent","在本项测试中，您将会在左右耳机中同时听到不同的声音，请选择出您在右边耳机中听到的声音，忽略左边耳机。");
                showdialog(intent);
                break;
            default:
                break;
        }

    }

    private void showdialog(Intent intent) {

        AlertDialog.Builder builder = new AlertDialog.Builder(FourthActivity.this);
        builder.setMessage("请将耳机戴入相对应的耳别,并将音量调到最舒适强度");
        builder.setCancelable(true);
        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                intent.setClass(FourthActivity.this, FifthActivity.class);
                startActivity(intent);
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
