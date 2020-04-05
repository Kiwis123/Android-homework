package chapter.android.aweme.ss.com.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.annotation.NonNull;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import chapter.android.aweme.ss.com.homework.model.Message;
import chapter.android.aweme.ss.com.homework.model.PullParser;

/**
 * 大作业:实现一个抖音消息页面,
 * 1、所需的data数据放在assets下面的data.xml这里，使用PullParser这个工具类进行xml解析即可
 * <p>如何读取assets目录下的资源，可以参考如下代码</p>
 * <pre class="prettyprint">
 *
 *     @Override
 *     protected void onCreate(@Nullable Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         setContentView(R.layout.activity_xml);
 *         //load data from assets/data.xml
 *         try {
 *             InputStream assetInput = getAssets().open("data.xml");
 *             List<Message> messages = PullParser.pull2xml(assetInput);
 *             for (Message message : messages) {
 *
 *             }
 *         } catch (Exception exception) {
 *             exception.printStackTrace();
 *         }
 *     }
 * </pre>
 * 2、所需UI资源已放在res/drawable-xxhdpi下面
 *
 * 3、作业中的会用到圆形的ImageView,可以参考 widget/CircleImageView.java
 */
public class Exercises3 extends AppCompatActivity implements GreenAdapter.ListItemClickListener{

    private RecyclerView mNumbersListView;
    private GreenAdapter mAdapter;
    private static int NUM_LIST_ITEMS = 30;
    private static final String TAG = "zyw";

    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //load data from assets/data.xml
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tips);

            //读取数据
            InputStream assetInput = getAssets().open("data.xml");
            List<chapter.android.aweme.ss.com.homework.model.Message> messages = PullParser.pull2xml(assetInput);
            for (Message message : messages) {
                Log.v(TAG, message.toString());
            }


            NUM_LIST_ITEMS=messages.size();

            mNumbersListView=findViewById(R.id.rv_list);//拿到RecyclerView的实例
            //下面三行设置纵向布局
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//用官方定义的线性垂直布局
            mNumbersListView.setLayoutManager(layoutManager);//设置RecyclerView管理器

            mNumbersListView.setHasFixedSize(true);

            mAdapter = new GreenAdapter(messages, NUM_LIST_ITEMS, this);//初始化适配器

            mNumbersListView.setAdapter(mAdapter);

            mNumbersListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                //可以在此方法里获取recycylerview有多少条item以及现在显示到了第几条等等一些信息。
                // 最后一个完全可见项的位置
                private int lastCompletelyVisibleItemPosition;

                @Override
                //滚动状态变化时回调
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (visibleItemCount > 0 && lastCompletelyVisibleItemPosition >= totalItemCount - 1) {
                            Toast.makeText(Exercises3.this, "已滑动到底部!,触发loadMore", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                //滚动进行时回调
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        lastCompletelyVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                    }
                    Log.d(TAG, "onScrolled: lastVisiblePosition=" + lastCompletelyVisibleItemPosition);
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }



    @Override
    public void onListItemClick(int clickedItemIndex) {
        Log.d(TAG, "onListItemClick: ");
        Intent intent = new Intent();
        intent.setClass(Exercises3.this, Chatroom.class);
        String index=String.valueOf(clickedItemIndex);
        intent.putExtra("position", index);
        startActivity(intent);
        /*if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mToast.show();*/
    }
}
