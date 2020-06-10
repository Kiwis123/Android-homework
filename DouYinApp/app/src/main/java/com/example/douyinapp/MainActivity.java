package com.example.douyinapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private ViewPager2Adapter viewPager2Adapter;
    private static final String TAG = "MainActivity";
    ArrayList<VideoData> videoDataList = new ArrayList<>();
    private CountDownLatch countDownLatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countDownLatch = new CountDownLatch(1);

        htmlDataThread.start(); // 开启子线程获取视频相关数据

        try {
            countDownLatch.await(); // 等待子线程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setViewPager2(); // 设置ViewPager2相关参数
    }

    private void setViewPager2() {
        viewPager2 = findViewById(R.id.viewpager2);// 拿到ViewPager2的实例
        viewPager2Adapter = new ViewPager2Adapter(this, videoDataList, viewPager2); // 传入数据
        viewPager2.setAdapter(viewPager2Adapter); // 设置Adapter
        viewPager2.setUserInputEnabled(true); // 允许用户滑动
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL); // 滑动方向设置为垂直

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { // 监听滑动状态的改变并输出日志
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.e(TAG, "onPageSelected: "+position );
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                Log.e(TAG, "onPageScrollStateChanged: "+state );
            }
        });
    }

    Thread htmlDataThread = new Thread(new Runnable() {
        @Override
        public void run() {
            getData();
            countDownLatch.countDown();
        }
    });

    private void getData() {
        String path = "https://beiyou.bytedance.com/api/invoke/video/invoke/video";

        try {
            String data = DataRequestActivity.getHtml(path); // 请求数据
            // 解析json内容
            Gson gson = new Gson();
            videoDataList = gson.fromJson(data, new TypeToken<ArrayList<VideoData>>() {}.getType()); // 格式解析并保存
            Log.e(TAG, "data: " + data);
            /*System.out.println(videoDataList.get(0).get_id());
            System.out.println(videoDataList.get(0).getAvatar());
            System.out.println(videoDataList.get(0).getDescription());
            System.out.println(videoDataList.get(0).getFeedurl());
            System.out.println(videoDataList.get(0).getLikecount());
            System.out.println(videoDataList.get(0).getNickname());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
