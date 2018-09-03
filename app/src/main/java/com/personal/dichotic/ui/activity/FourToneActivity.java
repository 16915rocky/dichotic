package com.personal.dichotic.ui.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.personal.dichotic.R;
import com.personal.dichotic.util.MediaPlayerThread;
import com.personal.dichotic.util.PlayThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class FourToneResultActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.rt_word_no1)
    RelativeLayout rtWordNo1;
    @BindView(R.id.rt_word_no2)
    RelativeLayout rtWordNo2;
    @BindView(R.id.rt_word_no3)
    RelativeLayout rtWordNo3;
    @BindView(R.id.rt_word_no4)
    RelativeLayout rtWordNo4;
    @BindView(R.id.rt_word_no5)
    RelativeLayout rtWordNo5;
    @BindView(R.id.rt_word_no6)
    RelativeLayout rtWordNo6;
    @BindView(R.id.tb_find)
    Toolbar tbFind;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.rt_word_no7)
    RelativeLayout rtWordNo7;
    @BindView(R.id.rt_word_no8)
    RelativeLayout rtWordNo8;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rt_count)
    RelativeLayout rtCount;

    private MediaPlayerThread mPlayThread;
    private SeekBar mBalance;
    private PlayThread mChannelLeftPlayer;
    private PlayThread mChannelRightPlayer;
    private HandlerThread mCheckmsgThread;
    private Handler mCheckMsgHandler;
    private MediaPlayer mMediaPlayer;
    private static final int MSG_UPDATE_INFO = 0x110;
    private boolean isUpdateInfo = true;
    private int surplusMediaSum = 0;
    private Map<Integer, String> maps;
    private int[] arr = {R.raw.ba2ba2, R.raw.ba2da2, R.raw.ba2ga4, R.raw.ba2ka1, R.raw.ba2ka3, R.raw.ba2pa4, R.raw.ba2ta1, R.raw.ba2ta3,
            R.raw.pa4ba2, R.raw.pa4da2, R.raw.pa4ga4, R.raw.pa4ka1, R.raw.pa4ka3, R.raw.pa4pa4, R.raw.pa4ta1, R.raw.pa4ta3,
            R.raw.ta1ba2, R.raw.ta1da2, R.raw.ta1ga4, R.raw.ta1ka1, R.raw.ta1ka3, R.raw.ta1pa4, R.raw.ta1ta1, R.raw.ta1ta3,
            R.raw.da2ba2, R.raw.da2da2, R.raw.da2ga4, R.raw.da2ka1, R.raw.da2ka3, R.raw.da2pa4, R.raw.da2ta1, R.raw.da2ta3,
            R.raw.ga4ba2, R.raw.ga4da2, R.raw.ga4ga4, R.raw.ga4ka1, R.raw.ga4ka3, R.raw.ga4pa4, R.raw.ga4ta1, R.raw.ga4ta3,
            R.raw.ka1ba2, R.raw.ka1da2, R.raw.ka1ga4, R.raw.ka1ka1, R.raw.ka1ka3, R.raw.ka1pa4, R.raw.ka1ta1, R.raw.ka1ta3,
            R.raw.ka3ba2, R.raw.ka3da2, R.raw.ka3ga4, R.raw.ka3ka1, R.raw.ka3ka3, R.raw.ka3pa4, R.raw.ka3ta1, R.raw.ka3ta3,
            R.raw.ta3ba2, R.raw.ta3da2, R.raw.ta3ga4, R.raw.ta3ka1, R.raw.ta3ka3, R.raw.ta3pa4, R.raw.ta3ta1, R.raw.ta3ta3};
    private String[] arrValue = {"ba2ba2", "ba2da2", "ba2ga4", "ba2ka1", "ba2ka3", "ba2pa4", "ba2ta1", "ba2ta3",
            "pa4ba2", "pa4da2", "pa4ga4", "pa4ka1", "pa4ka3", "pa4pa4", "pa4ta1", "pa4ta3",
            "ta1ba2", "ta1da2", "ta1ga4", "ta1ka1", "ta1ka3", "ta1pa4", "ta1ta1", "ta1ta3",
            "da2ba2", "da2da2", "da2ga4", "da2ka1", "da2ka3", "da2pa4", "da2ta1", "da2ta3",
            "ga4ba2", "ga4da2", "ga4ga4", "ga4ka1", "ga4ka3", "ga4pa4", "ga4ta1", "ga4ta3",
            "ka1ba2", "ka1da2", "ka1ga4", "ka1ka1", "ka1ka3", "ka1pa4", "ka1ta1", "ka1ta3",
            "ka3ba2", "ka3da2", "ka3ga4", "ka3ka1", "ka3ka3", "ka3pa4", "ka3ta1", "ka3ta3",
            "ta3ba2", "ta3da2", "ta3ga4", "ta3ka1", "ta3ka3", "ta3pa4", "ta3ta1", "ta3ta3",};

    private List<Map<Integer, String>> mList = new ArrayList<Map<Integer, String>>();
    private int leftScore = 0;
    private int rightScore = 0;
    private int sameScore = 0;
    //5秒内只能点击2个字
    private int clickNum = 0;
    //与UI线程管理的handler
    private Handler mHandler = new Handler();
    int selectLeftProgress = 0,selectRightProgress=0;
    private AudioManager audio;
    private int count = 0;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title=getIntent().getStringExtra("title");
        setContentView(R.layout.activity_four_tone);
        ButterKnife.bind(this);
        tvTitle.setText(title);
        tbFind.setNavigationIcon(R.mipmap.ic_arrow_left);
        setSupportActionBar(tbFind);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tbFind.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FourToneResultActivity.this, SelectionPatternActivity.class);
                finish();
            }
        });
        SharedPreferences spProgress = getSharedPreferences("sp_progress", Context.MODE_PRIVATE);
        selectLeftProgress = spProgress.getInt("selectLeftProgress", 0);
        selectRightProgress = spProgress.getInt("selectLeftProgress", 0);
        tbFind.setNavigationIcon(R.mipmap.ic_arrow_left);
        setSupportActionBar(tbFind);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        initEvent();
        startAudio();

    }

    private void initEvent() {
        rtWordNo1.setOnClickListener(this);
        rtWordNo2.setOnClickListener(this);
        rtWordNo3.setOnClickListener(this);
        rtWordNo4.setOnClickListener(this);
        rtWordNo5.setOnClickListener(this);
        rtWordNo6.setOnClickListener(this);
        rtWordNo7.setOnClickListener(this);
        rtWordNo8.setOnClickListener(this);

        tbFind.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FourToneResultActivity.this, SelectionPatternActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void startAudio() {
        randomSort();
        initBackThread();
        mCheckMsgHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 1000);
    }

    private void randomSort() {
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            int p = random.nextInt(i + 1);
            //音频内容随机
            int tmp = arr[i];
            arr[i] = arr[p];
            arr[p] = tmp;
            //对应映射字符串随机
            String tmp2 = arrValue[i];
            arrValue[i] = arrValue[p];
            arrValue[p] = tmp2;
        }
    }

    private void initBackThread() {
        mCheckmsgThread = new HandlerThread("check-message-coming");
        mCheckmsgThread.start();
        mCheckMsgHandler = new Handler(mCheckmsgThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                mHandler.post(new Runnable() {
                                  @Override
                                  public void run() {
                                      if (clickNum >= 1) {
                                          clickNum = 0;
                                      }
                                      resetBackground();
                                      count = count + 1;
                                      setNum(count);
                                      if (surplusMediaSum == arr.length) {
                                          Intent intent = new Intent();
                                          intent.putExtra("leftScore", leftScore);
                                          intent.putExtra("rightScore", rightScore);
                                          intent.putExtra("sameScore", sameScore);
                                          intent.setClass(FourToneResultActivity.this, FourToneLastActivity.class);
                                          startActivity(intent);
                                          finish();
                                      }
                                  }
                              }
                );

                if (surplusMediaSum != arr.length) {
                    changeMedia(arr[surplusMediaSum]);
                    surplusMediaSum++;
                } else {
                    isUpdateInfo = false;
                    //mCheckmsgThread.stop();
                    rtWordNo1.setFocusable(false);
                    rtWordNo2.setFocusable(false);
                    rtWordNo3.setFocusable(false);
                    rtWordNo4.setFocusable(false);
                    rtWordNo5.setFocusable(false);
                    rtWordNo6.setFocusable(false);
                    rtWordNo7.setFocusable(false);
                    rtWordNo8.setFocusable(false);

                }
                if (isUpdateInfo) {
                    mCheckMsgHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 5000);


                }
            }
        };
    }

    private void setNum(int count) {
        tvCount.setText(count + "/64");
    }

    private void changeMedia(int data) {

        releaseMediaPlayer();
        mMediaPlayer = MediaPlayer.create(FourToneResultActivity.this, data);
        setBalance(1000, selectLeftProgress,selectRightProgress);
        mMediaPlayer.start();
    }

    //注销当前音频，为准备播放下个音频做准备
    private void releaseMediaPlayer() {
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 设置左右声道平衡
     *
     * @param max     最大值
     *
     */
    public void setBalance(int max, int selectLeftProgress,int selectRightProgress) {
        float slp = (float) selectLeftProgress / (float) max;
        float srp= (float) selectRightProgress / (float) max;
        // Log.i(TAG, "setBalance: b = " + b);
        if (null != mMediaPlayer)
            mMediaPlayer.setVolume(slp, srp);
    }

    /**
     * 设置左右声道是否可用
     *
     * @param left  左声道
     * @param right 右声道
     */
    public void setChannel(boolean left, boolean right) {
        if (null != mMediaPlayer) {
            mMediaPlayer.setVolume(left ? 1 : 0, right ? 1 : 0);
            mMediaPlayer.start();
        }
    }

    public void pause() {
        if (null != mMediaPlayer)
            mMediaPlayer.pause();
    }

    public void play() {
        if (null != mMediaPlayer)
            mMediaPlayer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rt_word_no1:
                if (clickNum < 1) {
                    recordThePoints((String) rtWordNo1.getTag());
                    rtWordNo1.setBackgroundResource(R.color.drop_down_unselected);
                    clickNum++;
                }

                break;
            case R.id.rt_word_no2:
                if (clickNum < 1) {
                    rtWordNo2.setBackgroundResource(R.color.drop_down_unselected);
                    recordThePoints((String) rtWordNo2.getTag());
                    clickNum++;
                }

                break;
            case R.id.rt_word_no3:
                if (clickNum < 1) {
                    rtWordNo3.setBackgroundResource(R.color.drop_down_unselected);
                    recordThePoints((String) rtWordNo3.getTag());
                    clickNum++;
                }

                break;
            case R.id.rt_word_no4:
                if (clickNum < 1) {
                    rtWordNo4.setBackgroundResource(R.color.drop_down_unselected);
                    recordThePoints((String) rtWordNo4.getTag());
                    clickNum++;
                }

                break;
            case R.id.rt_word_no5:
                if (clickNum < 1) {
                    rtWordNo5.setBackgroundResource(R.color.drop_down_unselected);
                    recordThePoints((String) rtWordNo5.getTag());
                    clickNum++;
                }

                break;
            case R.id.rt_word_no6:
                if (clickNum < 1) {
                    rtWordNo6.setBackgroundResource(R.color.drop_down_unselected);
                    recordThePoints((String) rtWordNo6.getTag());
                    clickNum++;
                }
                break;
            case R.id.rt_word_no7:
                if (clickNum < 1) {
                    rtWordNo7.setBackgroundResource(R.color.drop_down_unselected);
                    recordThePoints((String) rtWordNo7.getTag());
                    clickNum++;
                }
                break;
            case R.id.rt_word_no8:
                if (clickNum < 1) {
                    rtWordNo8.setBackgroundResource(R.color.drop_down_unselected);
                    recordThePoints((String) rtWordNo8.getTag());
                    clickNum++;
                }
                break;

            default:
                break;
        }

    }

    private void recordThePoints(String selectWord) {
        String twoWord = arrValue[surplusMediaSum - 1];
        String leftWord = twoWord.substring(0, 3);
        String rightWord = twoWord.substring(3);
        if (leftWord.equals(rightWord)) {
            if (leftWord.equals(selectWord)) {
                sameScore++;
                Log.e("sameScore", sameScore + "");
            }
            return;
        }
        if (leftWord.equals(selectWord)) {
            leftScore++;


        }
        if (rightWord.equals(selectWord)) {
            rightScore++;
            Log.e("rightScore", rightScore + "");
        }

    }

    private void resetBackground() {
        rtWordNo1.setBackgroundResource(R.color.white);
        rtWordNo2.setBackgroundResource(R.color.white);
        rtWordNo3.setBackgroundResource(R.color.white);
        rtWordNo4.setBackgroundResource(R.color.white);
        rtWordNo5.setBackgroundResource(R.color.white);
        rtWordNo6.setBackgroundResource(R.color.white);
        rtWordNo7.setBackgroundResource(R.color.white);
        rtWordNo8.setBackgroundResource(R.color.white);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCheckmsgThread != null) {
            mCheckmsgThread.getLooper().quit();

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
