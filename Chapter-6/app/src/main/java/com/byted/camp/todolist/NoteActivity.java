package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.byted.camp.todolist.db.TodoContract.TodoEntry;
import com.byted.camp.todolist.db.TodoDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    private static final String TAG = "NoteActivity";

    private EditText editText;
    private Button addBtn;
    private RadioGroup radioGroup;
    private RadioButton radioHigh;
    private RadioButton radioNormal;
    private RadioButton radioLow;
    private int level;


    private TodoDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        dbHelper = new TodoDbHelper(this);

        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0); // 调用软键盘
        }

        addBtn = findViewById(R.id.btn_add);
        radioGroup = findViewById(R.id.event_level);
        radioHigh = findViewById(R.id.level_high);
        radioNormal = findViewById(R.id.level_normal);
        radioLow = findViewById(R.id.level_low);

        /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (radioHigh.isChecked()) {
                    level = 0;
                } else if (radioNormal.isChecked()) {
                    level = 1;
                } else if (radioLow.isChecked()) {
                    level = 2;
                }
            }
        });*/

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioHigh.isChecked()) {
                    level = 0;
                } else if (radioNormal.isChecked()) {
                    level = 1;
                } else if (radioLow.isChecked()) {
                    level = 2;
                }
                Log.i(TAG, "event level:" + level);
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim(), level);
                if (succeed) {
                    Toast.makeText(NoteActivity.this,
                            "Note added", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(NoteActivity.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean saveNote2Database(String content, int level) {
        // TODO 插入一条新数据，返回是否插入成功

        Date date =new Date(System.currentTimeMillis());
        SimpleDateFormat sf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoEntry.COLUMN_NAME_CONTENT, content);
        values.put(TodoEntry.COLUMN_NAME_TIME, sf.format(date));
        values.put(TodoEntry.COLUMN_NAME_ISDONE, 0);
        values.put(TodoEntry.COLUMN_NAME_LEVEL, level);

        long newRowId = db.insert(TodoEntry.TABLE_NAME, null, values);
        Log.i(TAG, "perform add data, result:" + newRowId + ", content:" + content + ", level:" + level);
        if(newRowId != -1){
            return true;
        }
        return false;
    }
}
