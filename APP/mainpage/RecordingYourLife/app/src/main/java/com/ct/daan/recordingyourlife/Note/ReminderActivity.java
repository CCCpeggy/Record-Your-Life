package com.ct.daan.recordingyourlife.Note;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.ReminderDbTable;

import java.util.Locale;

public class ReminderActivity extends AppCompatActivity {
    Intent intent;
    int id;
    Cursor cursor;
    ReminderDbTable ReminderDb;
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    EditText date,time;
    Switch isReplace;
    Spinner ReplaceType;

    CalendarFunction calFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_page);

        intent=getIntent();
        Bundle extra=intent.getExtras();
        id=extra.getInt("REMINDERID");

        initView();
    }
    private void initView(){
        calFunction=new CalendarFunction();
        date=(EditText)findViewById(R.id.date_et);
        time=(EditText)findViewById(R.id.time_et);
        isReplace=(Switch)findViewById(R.id.isreplace_sw);
        ReplaceType=(Spinner) findViewById(R.id.replacetype_sp);
        date.setOnClickListener(DatePick_Listener);
        time.setOnClickListener(TimePick_Listener);

        OpOrCrDb();
        ReminderDb=new ReminderDbTable(SQLiteDB_Path,db);
        ReminderDb.OpenOrCreateTb();
        Cursor cursor=ReminderDb.getCursor(id);
        cursor.moveToFirst();
        date.setText(cursor.getString(1));
        time.setText(cursor.getString(2));
        isReplace.setChecked(cursor.getInt(3)==1);
        ReplaceType.setSelection(cursor.getInt(4));
    }
    //打開或新增資料庫
    private void OpOrCrDb(){
        try{
            db=openOrCreateDatabase(SQLiteDB_Path,MODE_PRIVATE,null);
            Log.v("資料庫","資料庫載入成功");
        }catch (Exception ex){
            Log.e("#001","資料庫載入錯誤");
        }
    }

    void Complete() {
        ReminderDb.updateReminderData(id,date.getText().toString(),time.getText().toString(),isReplace.isChecked()?1:0 ,ReplaceType.getSelectedItemPosition());
        intent.putExtra("REMINDER_ID",id);
        setResult(RESULT_OK,intent);
        finish();
    }



    Calendar m_Calendar = Calendar.getInstance();
    private EditText.OnClickListener DatePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText et=(EditText)v;
            String date=et.getText().toString();
            if(!date.isEmpty())
                m_Calendar=calFunction.DateTextToCalendarType(date);
            DatePickerDialog dataPick=new DatePickerDialog(ReminderActivity.this,datepicker,
                    m_Calendar.get(Calendar.YEAR),
                    m_Calendar.get(Calendar.MONTH),
                    m_Calendar.get(Calendar.DAY_OF_MONTH));
            dataPick.show();
        }
    };

    private EditText.OnClickListener TimePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText et=(EditText)v;
            String time=et.getText().toString();
            if(!time.isEmpty())
                m_Calendar=calFunction.TimeTextToCalendarType(time);
            android.app.TimePickerDialog dataPick=new TimePickerDialog(ReminderActivity.this,TimePickerDialog,
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
}
