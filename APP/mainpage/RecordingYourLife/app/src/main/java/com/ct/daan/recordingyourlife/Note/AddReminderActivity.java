package com.ct.daan.recordingyourlife.Note;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.Class.OthersFunction;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.NoteDbTable;
import com.ct.daan.recordingyourlife.DbTable.Note_Reminder_DbTable;
import com.ct.daan.recordingyourlife.DbTable.ReminderDbTable;


public class AddReminderActivity extends AppCompatActivity {
    Intent intent;
    int id;
    ReminderDbTable ReminderDb;
    Note_Reminder_DbTable NoteReminderDb;
    private SQLiteDatabase db = null;
    private String SQLiteDB_Path = "student_project.db";
    EditText date,time;;
    Switch isReplace;
    Spinner ReplaceType;
    Button Complete_btn;
    NoteDbTable NoteDb;
    CalendarFunction calFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_page);
        calFunction=new CalendarFunction();
        intent = getIntent();
        Bundle extra = intent.getExtras();
        id = extra.getInt("NOTEID");
        Log.v("NOTEID", id + "");

        initView();
    }

    private void initView() {
        date = (EditText) findViewById(R.id.date_et);
        time=(EditText)findViewById(R.id.time_et);
        isReplace = (Switch) findViewById(R.id.isreplace_sw);
        ReplaceType = (Spinner) findViewById(R.id.replacetype_sp);
        Complete_btn = (Button) findViewById(R.id.btn_Complete2);

        OpOrCrDb();
        NoteReminderDb = new Note_Reminder_DbTable(SQLiteDB_Path, db);
        NoteReminderDb.OpenOrCreateTb();
        ReminderDb = new ReminderDbTable(SQLiteDB_Path, db);
        ReminderDb.OpenOrCreateTb();
        Complete_btn.setOnClickListener(Complete_btn_Listener);
        NoteDb = new NoteDbTable(SQLiteDB_Path, db);
        NoteDb.OpenOrCreateTb();
    }

    //打開或新增資料庫
    private void OpOrCrDb() {
        try {
            db = openOrCreateDatabase(SQLiteDB_Path, MODE_PRIVATE, null);
            Log.v("資料庫", "資料庫載入成功");
        } catch (Exception ex) {
            Log.e("#001", "資料庫載入錯誤");
        }
    }

    private void setReminder(Context context,Cursor cursor, int reminderId) {
        OthersFunction othersFunction=new OthersFunction();
        Cursor Note_cursor = NoteDb.getCursor(id);
        Note_cursor.moveToFirst();
        othersFunction.setReminder(context,cursor,reminderId,Note_cursor.getString(1),Note_cursor.getString(2));
    }

    Button.OnClickListener Complete_btn_Listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            ReminderDb.insertReminderData(date.getText().toString(),time.getText().toString(),isReplace.isChecked()?1 : 0, ReplaceType.getSelectedItemPosition());
            Cursor cursor = ReminderDb.getCursor();
            cursor.moveToLast();
            NoteReminderDb.insertNoteReminderData(cursor.getInt(0), id);
            setReminder(AddReminderActivity.this,cursor, cursor.getInt(0));
            setResult(RESULT_OK, intent);
            finish();
        }
    };
}
