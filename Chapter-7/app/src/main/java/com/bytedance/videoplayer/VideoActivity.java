package com.bytedance.videoplayer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;
    private SeekBar seekBar;
    private Button buttonPlay, buttonScreen;
    private TextView textViewTime;
    private TextView textViewCurrentPosition;
    private TextView textViewStatus;
    private RelativeLayout videoLayout;
    private LinearLayout videoControl;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            if (videoView.isPlaying()) {
                int current = videoView.getCurrentPosition();
                seekBar.setProgress(current);
                textViewCurrentPosition.setText(time(videoView.getCurrentPosition()));
            }
            handler.postDelayed(runnable, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video);

        videoView = (VideoView) this.findViewById(R.id.videoView);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            videoView.setVideoURI(uri);
        } else {
            videoView.setVideoPath(getVideoPath(R.raw.qq));
        }
        //videoView.setVideoURI(Uri.parse("file:///sdcard/Download/test.mp4"));
        //videoView.setVideoPath(getVideoPath(R.raw.qq));
        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                textViewTime.setText(time(videoView.getDuration()));
                textViewStatus.setText("视频加载完毕");
                buttonPlay.setEnabled(true);
            }
        });

        // 在播放完毕被回调
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(VideoActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // 发生错误重新播放
                play();
                Toast.makeText(VideoActivity.this, "播放出错", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        videoLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        videoControl = (LinearLayout) findViewById(R.id.videoController);

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        textViewStatus.setText("玩命加载中");

        textViewTime = (TextView) findViewById(R.id.textViewTime);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        // 为进度条添加进度更改事件
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        textViewCurrentPosition = (TextView) findViewById(R.id.textViewCurrentPosition);

        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setEnabled(false);

        final Button buttonStop = (Button) findViewById(R.id.buttonStop);

        buttonScreen = (Button) findViewById(R.id.buttonScreen);

        buttonPlay.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonScreen.setOnClickListener(this);
        videoLayout.setOnClickListener(this);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        View decorView6 = getWindow().getDecorView();

        int screenOritentation = getResources().getConfiguration().orientation;

        if (screenOritentation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏时处理
            buttonScreen.setText("小窗");
            int uiOptions6 = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView6.setSystemUiVisibility(uiOptions6);
            setVideoScreenSize(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            //竖屏时处理
            buttonScreen.setText("全屏");
            decorView6.setSystemUiVisibility(0);
            setVideoScreenSize(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    private void setVideoScreenSize(int width, int height) {
        //获取视频控件的布局参数
        ViewGroup.LayoutParams videoViewLayoutParams = videoView.getLayoutParams();
        //设置视频范围
        videoViewLayoutParams.width = width;
        videoViewLayoutParams.height = height;
        videoView.setLayoutParams(videoViewLayoutParams);
        //设置视频和控制组件的layout
        ViewGroup.LayoutParams videoLayoutLayoutParams= videoLayout.getLayoutParams();
        videoLayoutLayoutParams.width = width;
        videoLayoutLayoutParams.height = height;
        videoLayout.setLayoutParams(videoLayoutLayoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay:
                play();
                break;
            case R.id.buttonStop:
                stop();
                break;
            case R.id.buttonScreen:
                screen();
                break;
            case R.id.relativeLayout:
                setControl();
            default:
                break;
        }
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        // 当进度条停止修改的时候触发
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 取得当前进度条的刻度
            int progress = seekBar.getProgress();
            videoView.seekTo(progress);
            textViewCurrentPosition.setText(time(videoView.getCurrentPosition()));
            /*if (videoView.isPlaying()) {
                // 设置当前播放的位置
                videoView.seekTo(progress);
            }*/
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

        }
    };

    protected void play() {

        if (buttonPlay.getText().equals("播放")) {
            buttonPlay.setText("暂停");
            textViewStatus.setText("请您欣赏");
            // 开始线程，更新进度条的刻度
            handler.postDelayed(runnable, 0);
            videoView.start();
            seekBar.setMax(videoView.getDuration());

        } else {
            buttonPlay.setText("播放");
            if (videoView.isPlaying()) {
                videoView.pause();
            }
        }

    }

    protected void stop() {
        videoView.stopPlayback();
        videoView.resume();
        buttonPlay.setText("播放");
    }

    protected void screen() {
        int screenOritentation = getResources().getConfiguration().orientation;
        View decorView6 = getWindow().getDecorView();

        if (buttonScreen.getText().equals("全屏")) {
            buttonScreen.setText("小窗");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置为横屏
            if (screenOritentation == Configuration.ORIENTATION_LANDSCAPE) {
                int uiOptions6 = View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView6.setSystemUiVisibility(uiOptions6);
                //videoControl.setVisibility(View.GONE);
                //textViewStatus.setVisibility(View.GONE);
                setVideoScreenSize(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        } else {
            buttonScreen.setText("全屏");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置为竖屏
            if (screenOritentation == Configuration.ORIENTATION_PORTRAIT) {
                decorView6.setSystemUiVisibility(0);
                //videoControl.setVisibility(View.VISIBLE);
                //textViewStatus.setVisibility(View.VISIBLE);
                setVideoScreenSize(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }
    }

    protected void setControl() {
        if(videoControl.getVisibility() == View.GONE) {
            textViewStatus.setVisibility(View.VISIBLE);
            videoControl.setVisibility(View.VISIBLE);
        } else if (videoControl.getVisibility() == View.VISIBLE) {
            textViewStatus.setVisibility(View.GONE);
            videoControl.setVisibility(View.GONE);
        }
    }

    protected String time(long millionSeconds) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millionSeconds);
        return simpleDateFormat.format(c.getTime());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }

}