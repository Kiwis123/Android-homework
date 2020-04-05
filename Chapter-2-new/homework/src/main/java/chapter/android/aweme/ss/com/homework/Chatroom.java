package chapter.android.aweme.ss.com.homework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Chatroom extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        RelativeLayout layout = new RelativeLayout(this);
        Intent intent=getIntent();
        String index=intent.getStringExtra("position");
        System.out.println(index);
        TextView itemIndex=(TextView) findViewById(R.id.tv_content_info);
        TextView header=(TextView) findViewById(R.id.tv_with_name);
        itemIndex.setText("当前所在item为第" + index + "项！");
        header.setText("消息");
    }
}
