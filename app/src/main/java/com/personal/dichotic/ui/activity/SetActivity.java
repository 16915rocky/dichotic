package com.personal.dichotic.ui.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.personal.dichotic.R;
import com.personal.dichotic.ui.widgets.BasePopupWindow;
import com.personal.dichotic.util.MediaPlayerThread;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rocky on 2018/4/16.
 */

public class ScondActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.lt_sex_select)
    LinearLayout ltSexSelect;
    @BindView(R.id.tb_find)
    Toolbar tbFind;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.lt_age_select)
    LinearLayout ltAgeSelect;
    @BindView(R.id.tv_hand)
    TextView tvHand;
    @BindView(R.id.lt_hand_select)
    LinearLayout ltHandSelect;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.lt_left_ear)
    LinearLayout ltLeftEar;
    @BindView(R.id.lt_right_ear)
    LinearLayout ltRightEar;
    /*   @BindView(R.id.sb_balance)
       SeekBar sbBalance;*/
    private BasePopupWindow mPopupWidow;
    private ImageView imgHorn;
    private List<String> sexDatas, ageDatas, handDatas;
    private String sexStr,ageStr,headStr;
    private int tempNum;
    // private List<String> tempList;
    private int selectLeftProgress = 800;
    private int selectRightProgress = 800;
    private int position = -1;
    private MediaPlayer mMediaPlayer;
    private AudioManager audio;
    private MediaPlayerThread rightThread,leftThread;
    private List<String> tempList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        initData();
        ltSexSelect.setOnClickListener(this);
        ltAgeSelect.setOnClickListener(this);
        ltHandSelect.setOnClickListener(this);
        ltLeftEar.setOnClickListener(this);
        ltRightEar.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        tbFind.setNavigationIcon(R.mipmap.ic_arrow_left);
        setSupportActionBar(tbFind);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tbFind.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        sexDatas = new ArrayList<>();
        sexDatas.add("男");
        sexDatas.add("女");
        ageDatas = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            ageDatas.add(i + "");
        }
        handDatas = new ArrayList<>();
        handDatas.add("左右手");
        handDatas.add("左手");
        handDatas.add("右手");

    }


    private void showPopupWindow(List<String> list, int num) {
        tempList = list;
        tempNum = num;
        View view=null;
        SeekBar sblBalance=null;
        SeekBar sbrBalance=null;
        LoopView lvPicker=null;
         mPopupWidow = new BasePopupWindow(this);
        if(num==4){
             view = LayoutInflater.from(this).inflate(R.layout.view_select_item2, null);
            sblBalance=(SeekBar)view.findViewById(R.id.sb_balance);
            imgHorn=(ImageView)view.findViewById(R.id.img_horn);
            sblBalance.setProgress(selectLeftProgress+400);
            leftThread=new MediaPlayerThread(ScondActivity.this,R.raw.l1000,/*(float)(selectLeftProgress/100)*/1,0);

            sblBalance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progress=progress-400;
                   /* if(progress<0){
                        progress=0;
                    }*/
                    selectLeftProgress=progress;
                    leftThread.setBalance(1000,progress,true);
                    imgHorn.setActivated(true);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    leftThread.start();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    imgHorn.setActivated(false);
                    leftThread.pause();
                }
            });
        }else if(num==5){
            view = LayoutInflater.from(this).inflate(R.layout.view_select_item2, null);
            sbrBalance=(SeekBar)view.findViewById(R.id.sb_balance);
            imgHorn=(ImageView)view.findViewById(R.id.img_horn);
            sbrBalance.setProgress(selectRightProgress+400);
            rightThread=new MediaPlayerThread(ScondActivity.this,R.raw.r1000,/*(float)(selectLeftProgress/100)*/1,0);
            sbrBalance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progress=progress-400;
                   /* if(progress<0){
                        progress=0;
                    }*/
                    selectRightProgress=progress;
                    rightThread.setBalance(1000,progress,false);
                    imgHorn.setActivated(true);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    rightThread.start();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    imgHorn.setActivated(false);
                    rightThread.pause();
                }
            });
        }else {
            view = LayoutInflater.from(this).inflate(R.layout.view_select_item, null);
            lvPicker = (LoopView) view.findViewById(R.id.lv_picker);

            lvPicker.setTextSize(13);
            lvPicker.setItems(tempList);
            lvPicker.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    position = index;
                    if(tempNum==0){
                        sexStr = tempList.get(index);
                    }else if(tempNum==1){
                        ageStr = tempList.get(index);
                    }else if(tempNum==2){
                        headStr = tempList.get(index);
                    }

                }
            });
        }
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        mPopupWidow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWidow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWidow.setContentView(view);
        mPopupWidow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWidow.setOutsideTouchable(false);
        mPopupWidow.setFocusable(true);
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_second, null);
        mPopupWidow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWidow.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempNum == 0) {
                    if (position == -1) {
                        tvSex.setText("女");
                    } else {
                        tvSex.setText(sexStr);
                    }
                } else if (tempNum == 1) {
                    if (position == -1) {
                        tvAge.setText("51");
                    } else {
                        tvAge.setText(ageStr);
                    }
                } else if (tempNum == 2) {
                    if (position == -1) {
                        tvHand.setText("左右手");
                    } else {
                        tvHand.setText(headStr);
                    }

                }
                mPopupWidow.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lt_sex_select:
                showPopupWindow(sexDatas, 0);
                break;
            case R.id.lt_age_select:
                showPopupWindow(ageDatas, 1);
                break;
            case R.id.lt_hand_select:
                showPopupWindow(handDatas, 2);
                break;
            case R.id.tv_next:
                if ("".equals(etName.getText().toString()) || "".equals(tvAge.getText().toString()) || "".equals(tvSex.getText().toString()) || "".equals(tvHand.getText().toString())) {
                    Toast.makeText(ScondActivity.this, "请填写完信息!", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences spProgress = getSharedPreferences("sp_progress", Context.MODE_PRIVATE);

                    spProgress.edit().putInt("selectLeftProgress",selectLeftProgress ).commit();
                    spProgress.edit().putInt("selectRightProgress",selectRightProgress ).commit();
                    //
                    spProgress.edit().putBoolean("reset",false).commit();
                    Intent intent = new Intent();
                    intent.setClass(ScondActivity.this, ThirdActivity.class);
                    startActivity(intent);
                }
            case R.id.lt_left_ear:
                showPopupWindow(sexDatas, 4);
                break;
            case R.id.lt_right_ear:
                showPopupWindow(sexDatas, 5);
                break;
            default:
                break;
        }
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
