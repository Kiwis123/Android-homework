package com.example.douyinapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewPager2ViewHolder> {

    private static final String TAG = "ViewPager2Adapter";

    private ArrayList<VideoData> videoDataList; // 视频数据数组
    private int viewHolderCount; // 记录形成的item数
    private ViewPager2 viewPager2;


    ViewPager2Adapter(Context context, ArrayList<VideoData> videoData, ViewPager2 viewPager2) {
        this.videoDataList = videoData;
        this.viewHolderCount = 0;
        this.viewPager2 = viewPager2;
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public ViewPager2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewPager2ViewHolder viewHolder = new ViewPager2ViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: " + viewHolderCount);
        viewHolderCount++;

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewPager2ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: #" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return videoDataList.size();
    }

    class ViewPager2ViewHolder extends RecyclerView.ViewHolder{

        private TextView infoName;
        private TextView infoDescription;
        private TextView infoLikes;
        private VideoView videoView;
        private ImageView imageView;
        private GestureDetector myGestureDetector;
        private Love ll_love;


        ViewPager2ViewHolder(@NonNull View itemView) {
            super(itemView);
            infoName = itemView.findViewById(R.id.infoName);
            infoDescription = itemView.findViewById(R.id.infoDescription);
            infoLikes = itemView.findViewById(R.id.infoLikes);
            videoView = itemView.findViewById(R.id.videoView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        @SuppressLint("ClickableViewAccessibility")
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        void bind(final int position) {
            final VideoData videoData = videoDataList.get(position);
            infoName.setText(videoData.getNickname());
            infoDescription.setText(videoData.getDescription());
            infoLikes.setText("点赞：" + Integer.toString(videoData.getLikecount()));
            Log.d(TAG, videoData.getNickname());
            Glide.with(itemView.getContext()).load(videoData.getAvatar()).into(imageView);
            videoView.setVideoPath(Uri.parse(videoData.getFeedurl()).toString());

            // 监听对VideoView组件的点击行为
            ll_love = (Love) itemView.findViewById(R.id.lovelayout);
            myGestureDetector = new GestureDetector(itemView.getContext(), new myOnGestureListener());
            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    myGestureDetector.onTouchEvent(event);
                    return true;
                }
            });

            // 监听点击封面、开始播放视频
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setVisibility(View.GONE);
                    videoView.start();
                }
            });

            // 监听当前页，若移动到了其他页则重新设置本页封面为可见
            final int lastPosition = position;
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    if (lastPosition != position) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });

            // 监听视频播放，若结束循环播放
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.setLooping(true);
                    mp.start();
                }
            });
        }

        //监听VideoView组件上的手势动作
        class myOnGestureListener extends GestureDetector.SimpleOnGestureListener {
            //双击
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                ll_love.addLoveView(e.getRawX(), e.getRawY());
                return super.onDoubleTap(e);
            }

            //单击且短时间内没有再次点击
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                } else {
                    imageView.setVisibility(View.GONE);
                    videoView.start();
                }
                return false;
            }
        }
    }
}
