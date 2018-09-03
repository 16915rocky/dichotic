package com.personal.dichotic.ui.activity;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.personal.dichotic.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rocky on 2018/5/1.
 */

public class PromptActivity extends AppCompatActivity {
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tb_find)
    Toolbar tbFind;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private AudioManager audio;
    private String DesContent;
    private boolean isFirstTone = true, isTestTone = false;
    private String title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        DesContent = getIntent().getStringExtra("DesContent");
        isFirstTone = getIntent().getBooleanExtra("isFirstTone", true);
        isTestTone = getIntent().getBooleanExtra("isTestTone", false);
        setContentView(R.layout.activity_fifth);
        ButterKnife.bind(this);
        tvTitle.setText(title);
        tbFind.setNavigationIcon(R.mipmap.ic_arrow_left);
        setSupportActionBar(tbFind);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvHint.setText(DesContent);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title",title);
                if (isTestTone) {
                    intent.setClass(PromptActivity.this, TestActivity.class);
                } else if (isFirstTone) {
                    intent.setClass(PromptActivity.this, FTActivity.class);
                } else {
                    intent.setClass(PromptActivity.this, FourToneActivity.class);
                }
                startActivity(intent);
            }
        });
        tbFind.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
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
