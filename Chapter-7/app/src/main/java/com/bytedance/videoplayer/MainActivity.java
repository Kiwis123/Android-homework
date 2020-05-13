package com.bytedance.videoplayer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(new File("/sdcard/Download/test.mp4"));
        intent.setDataAndType(data, "video/mp4");
        startActivity(intent);*/
        startActivity(new Intent(MainActivity.this, VideoActivity.class));
    }
}
