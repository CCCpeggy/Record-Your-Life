package com.ct.daan.recordingyourlife.Note;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.Class.OthersFunction;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.NoteDbTable;
import com.ct.daan.recordingyourlife.DbTable.Note_Reminder_DbTable;
import com.ct.daan.recordingyourlife.DbTable.ReminderDbTable;

import java.util.Locale;


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
    NoteDbTable NoteDb;
    CalendarFunction calFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
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
        date.setOnClickListener(DatePick_Listener);
        time.setOnClickListener(TimePick_Listener);

        OpOrCrDb();
        NoteReminderDb = new Note_Reminder_DbTable(SQLiteDB_Path, db);
        NoteReminderDb.OpenOrCreateTb();
        ReminderDb = new ReminderDbTable(SQLiteDB_Path, db);
        ReminderDb.OpenOrCreateTb();
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
        othersFunction.setReminder(context,cursor,reminderId,id,Note_cursor.getString(1),Note_cursor.getString(2));
    }

    void Complete() {
        ReminderDb.insertReminderData(date.getText().toString(),time.getText().toString(),isReplace.isChecked()?1 : 0, ReplaceType.getSelectedItemPosition());
        Cursor cursor = ReminderDb.getCursor();
        cursor.moveToLast();
        NoteReminderDb.insertNoteReminderData(cursor.getInt(0), id);
        setReminder(AddReminderActivity.this,cursor, cursor.getInt(0));
        setResult(RESULT_OK, intent);
        finish();
    }


    Calendar m_Calendar = Calendar.getInstance();
    private EditText.OnClickListener DatePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dataPick=new DatePickerDialog(AddReminderActivity.this,datepicker,
                    m_Calendar.get(Calendar.YEAR),
                    m_Calendar.get(Calendar.MONTH),
                    m_Calendar.get(Calendar.DAY_OF_MONTH));
            dataPick.show();
        }
    };

    private EditText.OnClickListener TimePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            android.app.TimePickerDialog dataPick=new TimePickerDialog(AddReminderActivity.this,TimePickerDialog,
                    m_Calendar.get(Calendar.HOUR_OF_DAY),
                    m_Calendar.get(Calendar.MINUTE),true);
            dataPick.show();
        }
    };

    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            m_Calendar.set(Calendar.YEAR, year);
            m_Calendar.set(Calendar.MONTH, monthOfYear);
            m_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
            date.setText(sdf.format(m_Calendar.getTime()));
        }
    };


    TimePickerDialog.OnTimeSetListener TimePickerDialog=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            m_Calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            m_Calendar.set(Calendar.MINUTE, minute);
            String myFormat = "HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
            time.setText(sdf.format(m_Calendar.getTime()));
        }
    };


    //增加動作按鈕到工具列
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_actions, menu);
        return true;
    }

    //動作按鈕回應
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                Complete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void setTheme(){
        SharedPreferences prefs = getSharedPreferences("RECORDINGYOURLIFE", 0);
        int theme_index = prefs.getInt("THEME_INDEX" ,0);
        int theme=0;
        switch (theme_index){
            case 1:
                theme=R.style.AppTheme1;
                break;
            case 2:
                theme=R.style.AppTheme2;
                break;
            case 3:
                theme= R.style.AppTheme3;
                break;
            case 4:
                theme=R.style.AppTheme4;
                break;
            case 5:
                theme=R.style.AppTheme5;
                break;
            case 6:
                theme=R.style.AppTheme6;
                break;
            case 7:
                theme=R.style.AppTheme7;
                break;
            case 8:
                theme=R.style.AppTheme8;
                break;
            case 9:
                theme=R.style.AppTheme9;
                break;
            case 10:
                theme=R.style.AppTheme10;
                break;
            case 0:
            default:
                theme=R.style.AppTheme0;
                break;
        }
        setTheme(theme);
    }
}
