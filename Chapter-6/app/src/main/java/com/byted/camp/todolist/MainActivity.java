package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract.TodoEntry;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.operation.activity.DatabaseActivity;
import com.byted.camp.todolist.operation.activity.DebugActivity;
import com.byted.camp.todolist.operation.activity.SettingActivity;
import com.byted.camp.todolist.ui.NoteListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;

    private TodoDbHelper dbHelper;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new TodoDbHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) throws ParseException {
                MainActivity.this.deleteNote(note);
                notesAdapter.refresh(loadNotesFromDatabase());
            }

            @Override
            public void updateNote(Note note) throws ParseException {
                MainActivity.this.updateNode(note);
                notesAdapter.refresh(loadNotesFromDatabase());
            }
        });
        recyclerView.setAdapter(notesAdapter);


        try {
            notesAdapter.refresh(loadNotesFromDatabase()); // 加入Database里的新数据
            Log.i(TAG, "app start:first loat data!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            case R.id.action_database:
                startActivity(new Intent(this, DatabaseActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            try {
                notesAdapter.refresh(loadNotesFromDatabase());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<Note> loadNotesFromDatabase() throws ParseException {
        // TODO 从数据库中查询数据，并转换成 JavaBeans
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                TodoEntry.COLUMN_NAME_CONTENT,
                TodoEntry.COLUMN_NAME_TIME,
                TodoEntry.COLUMN_NAME_ISDONE,
                TodoEntry.COLUMN_NAME_LEVEL
        };

        String sortOrder = TodoEntry.COLUMN_NAME_LEVEL + " DESC";

        Cursor cursor = db.query(
                TodoEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<Note> allnotes = new ArrayList<>();

        Date date;
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(TodoEntry._ID));
            String content = cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_CONTENT));
            String level = cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_LEVEL));
            String time = cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_TIME));
            date = SIMPLE_DATE_FORMAT.parse(time);
            String isdone = cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_ISDONE));
            State state = State.from(Integer.parseInt(isdone == null ? "0" : isdone));
            Note temp = new Note(itemId);
            temp.setContent(content);
            temp.setDate(date);
            temp.setState(state);
            temp.setLevel(Integer.parseInt(level));
            Log.i(TAG, "itemId:" + itemId + ", content:" + content + ", date:" + time + ", isdone:" + isdone + ", level:" + level);
            allnotes.add(temp);
        }
        cursor.close();
        Collections.reverse(allnotes);

        return allnotes;
    }

    private void deleteNote(Note note) {
        // TODO 删除数据
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = TodoEntry._ID + "=?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(note.id)};
        // Issue SQL statement.
        int deletedRows = db.delete(TodoEntry.TABLE_NAME, selection, selectionArgs);
        Log.i(TAG, "perform delete data, result:" + deletedRows);
    }

    private void updateNode(Note note) {
        // TODO 更新数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // New value for one column
        State state = note.getState();
        ContentValues values = new ContentValues();
        values.put(TodoEntry.COLUMN_NAME_ISDONE, state.intValue);

        // Which row to update, based on the title
        String selection = TodoEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(note.id)};

        int count = db.update(
                TodoEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        Log.i(TAG, "perform update data, result:" + count);
    }

}
