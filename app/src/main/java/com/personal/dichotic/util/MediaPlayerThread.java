package com.personal.dichotic.util;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.util.Log;

import com.personal.dichotic.R;

/**
 * Created by kqw on 2016/8/26.
 * 播放音乐的线程
 */
public class MediaPlayerThread extends Thread {



    private static final String TAG = "PlayThread";
    private Activity mActivity;
    private byte[] data;
    private String mFileName;
    private MediaPlayer mMediaPlayer;

    public MediaPlayerThread(Activity activity, String fileName) {
        mActivity = activity;
        mFileName = fileName;
        mMediaPlayer =MediaPlayer.create(mActivity,R.raw.baba);
    }

    @Override
    public void run() {
        super.run();
            if (null != mMediaPlayer) {
                mMediaPlayer.start();
            }
    }

    /**
     * 设置左右声道平衡
     *
     * @param max     最大值
     * @param balance 当前值
     */
    public void setBalance(int max, int balance) {
        float b = (float) balance / (float) max;
        Log.i(TAG, "setBalance: b = " + b);
        if (null != mMediaPlayer)
            mMediaPlayer.setVolume(1 - b, b);
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

    public void stopp() {
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
      if (null != mMediaPlayer) {
          mMediaPlayer.stop();
          mMediaPlayer.release();
          mMediaPlayer = null;
        }
    }
}
