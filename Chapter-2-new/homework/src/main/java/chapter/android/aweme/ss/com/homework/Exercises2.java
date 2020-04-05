package chapter.android.aweme.ss.com.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 作业2：一个抖音笔试题：统计页面所有view的个数
 * Tips：ViewGroup有两个API
 * {@link android.view.ViewGroup #getChildAt(int) #getChildCount()}
 * 用一个TextView展示出来
 */
public class Exercises2 extends AppCompatActivity {

    private TextView mLifecycleDisplay;
    private static final String TAG = "wangyi";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relativelayout);
        mLifecycleDisplay = findViewById(R.id.output);
        mLifecycleDisplay.setText("共有" + getAllChildViewCount() + "个view\n");
    }

    public int getAllChildViewCount() {
        //todo 补全你的代码
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relative);
        return layout.getChildCount();
    }
}
