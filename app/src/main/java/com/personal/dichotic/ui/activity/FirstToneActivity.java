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

public class FirstToneActivity extends AppCompatActivity implements View.OnClickListener {
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
    @BindView(R.id.tv_title)
    TextView tvTitle;
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
    private int[] arr = {R.raw.ba1ba1, R.raw.ba1da1, R.raw.ba1ga1, R.raw.ba1ka1, R.raw.ba1pa1, R.raw.ba1ta1,
            R.raw.da1ba1, R.raw.da1da1, R.raw.da1ga1, R.raw.da1ka1, R.raw.da1pa1, R.raw.da1ta1,
            R.raw.ga1ba1, R.raw.ga1da1, R.raw.ga1ga1, R.raw.ga1ka1, R.raw.ga1pa1, R.raw.ga1ta1,
            R.raw.ka1ba1, R.raw.ka1da1, R.raw.ka1ga1, R.raw.ka1ka1, R.raw.ka1pa1, R.raw.ka1ta1,
            R.raw.pa1ba1, R.raw.pa1da1, R.raw.pa1ga1, R.raw.pa1ka1, R.raw.pa1pa1, R.raw.pa1ta1,
            R.raw.ta1ba1, R.raw.ta1da1, R.raw.ta1ga1, R.raw.ta1ka1, R.raw.ta1pa1, R.raw.ta1ta1};
    private String[] arrValue = {"ba1ba1", "ba1da1", "ba1ga1", "ba1ka1", "ba1pa1", "ba1ta1",
            "da1ba1", "da1da1", "da1ga1", "da1ka1", "da1pa1", "da1ta1",
            "ga1ba1", "ga1da1", "ga1ga1", "ga1ka1", "ga1pa1", "ga1ta1",
            "ka1ba1", "ka1da1", "ka1ga1", "ka1ka1", "ka1pa1", "ka1ta1",
            "pa1ba1", "pa1da1", "pa1ga1", "pa1ka1", "pa1pa1", "pa1ta1",
            "ta1ba1", "ta1da1", "ta1ga1", "ta1ka1", "ta1pa1", "ta1ta1"};

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
        title = getIntent().getStringExtra("title");
        setContentView(R.layout.activity_t);
        ButterKnife.bind(this);
        tvTitle.setText(title);
        tbFind.setNavigationIcon(R.mipmap.ic_arrow_left);
        setSupportActionBar(tbFind);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tbFind.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FirstToneActivity.this, SelectionPatternActivity.class);
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
        tbFind.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FirstToneActivity.this, SelectionPatternActivity.class);
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
                                          intent.setClass(FirstToneActivity.this, LastActivity.class);
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


                }
                if (isUpdateInfo) {
                    mCheckMsgHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 5000);


                }
            }
        };
    }

    private void setNum(int count) {
        tvCount.setText(count + "/36");
    }

    private void changeMedia(int data) {

        releaseMediaPlayer();
        mMediaPlayer = MediaPlayer.create(FirstToneActivity.this, data);
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
