package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private RadioGroup rg;
    private RadioButton rb_Male, rb_Female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg=(RadioGroup)findViewById(R.id.radioGroupId);
        rb_Female=(RadioButton)findViewById(R.id.rb_Female);
        rb_Male=(RadioButton)findViewById(R.id.rb_Male);
    }

    int size=30;
    public void bigger(View v){//按钮对应的onclick响应
        TextView txv;
        txv=(TextView) findViewById(R.id.txv);//根据ID找到对应的text对象
        txv.setTextSize(++size);//修改对象的字符大小-size
    }

    public void display(View v){//另外一个按钮对应的onclick响应
         EditText name=(EditText) findViewById(R.id.name);//根据ID找到对象
         TextView text2=(TextView) findViewById(R.id.txv);
         text2.setText(name.getText().toString());//设置字符
    }

    class MyRadioButtonListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 选中状态改变时被触发
            switch (checkedId) {
                case R.id.rb_Female:
                    // 当用户选择女性时
                    Log.i("sex", "当前用户选择"+rb_Female.getText().toString());
                    break;
                case R.id.rb_Male:
                    // 当用户选择男性时
                    Log.i("sex", "当前用户选择"+rb_Male.getText().toString());
                    break;
            }
        }
    }

}
