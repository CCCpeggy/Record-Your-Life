package com.ct.daan.recordingyourlife.Settings;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.DbTable.ExamDbTable;
import com.ct.daan.recordingyourlife.DbTable.ReminderDbTable;
import com.ct.daan.recordingyourlife.Exam.ExamPageActivity;
import com.ct.daan.recordingyourlife.Note.NotePageActivity;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.Table.AddTableSettingsActivity;

import java.util.Locale;

/**
 * Created by info on 2017/12/25.
 */

public class Settings extends AppCompatActivity{
    Spinner theme_sp;
    EditText Time_et;
    CalendarFunction calendarFunction;
    ReminderDbTable ReminderDb;
    ExamDbTable ExamDb;
    int theme_this;
    private final static String SQLiteDB_Path="student_project.db";
    private SQLiteDatabase db=null;

    Calendar m_Calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_content);

        initView();
    }
    void initView(){
        theme_sp=(Spinner)findViewById(R.id.theme_sp);
        theme_sp.setSelection(theme_this);
        theme_sp.setOnItemSelectedListener(Theme_sp_Listener);
        Time_et=(EditText)findViewById(R.id.time_et);
        Time_et.setOnClickListener(TimePick_Listener);
        calendarFunction=new CalendarFunction();

        OpOrCrDb();
        ReminderDb=new ReminderDbTable(SQLiteDB_Path,db);
        ReminderDb.OpenOrCreateTb();

        ExamDb=new ExamDbTable(SQLiteDB_Path,db);
        ExamDb.OpenOrCreateTb();

        SharedPreferences prefs = getSharedPreferences("RECORDINGYOURLIFE", 0);
        String Time = prefs.getString("ExamRemindTime" ,"");
        Time_et.setText(Time);
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

    private EditText.OnClickListener TimePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!Time_et.getText().toString().isEmpty()) m_Calendar=calendarFunction.TimeTextToCalendarType(Time_et.getText().toString());
            int hour = m_Calendar.get(Calendar.HOUR_OF_DAY);
            int minute = m_Calendar.get(Calendar.MINUTE);
            // Create a new instance of TimePickerDialog and return it
            TimePickerDialog timePicker= new TimePickerDialog(Settings.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String Time= String.format("%02d:%02d", hourOfDay, minute);
                    Log.v("選擇時間", String.format("考試填入提醒時間=%s",Time));
                    saveSettingData("ExamRemindTime",Time);
                    Time_et.setText(Time);
                }
            }, hour, minute, true);
            timePicker.setTitle("考試填入提醒時間");
            timePicker.show();

        }


    };


    private Spinner.OnItemSelectedListener Theme_sp_Listener= new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String theme_str=adapterView.getSelectedItem().toString();
            int theme_index=theme_sp.getSelectedItemPosition();
            if(theme_index==theme_this)return;
            saveTheme(theme_index,theme_str);
        }



        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    void saveTheme(final int theme_index,String theme_str) {
        new android.app.AlertDialog.Builder(Settings.this)
                .setMessage(String.format("確認要換成%s主題嗎",theme_str))
                .setTitle(R.string.theme_tilte)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor pref = getSharedPreferences("RECORDINGYOURLIFE", 0).edit();
                        pref.putInt("THEME_INDEX" , theme_index);
                        pref.apply();
                        Restart();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    void saveSettingData(String Key,String Value){/*
        saveData(Key, Value);
        Cursor cursor=ExamDb.getCursor("'考試日期'>="+calendarFunction.getTodayDate());
        int[] ExamIDs=new int[cursor.getCount()];
        String[] Names=new String[cursor.getCount()];
        String[] Subjects=new String[cursor.getCount()];
        for (int i=0;i<cursor.getCount();i++){
            ExamIDs[i]=cursor.getInt(0);
            Names[i]=cursor.getString(4);
            Subjects[i]=cursor.getString(0);
        }
        String Reminder_time=Reminder_cursor.getString(2);
        int Reminder_type=Reminder_cursor.getInt(3);
        Log.v("setReminder",String.format("context:%s,Exam_id:%d,title:%s,content:%s,reminder_id:%d,date:%s,time:%s"
                ,context,Exam_id,name,subject,reminder_id,Reminder_date,Reminder_time));
        CalendarFunction calFunction=new CalendarFunction();
        Calendar calendar=calFunction.DateTimeTextToCalendarType(Reminder_date,Reminder_time);
        String BROADCAST_ACTION = "net.macdidi.broadcast01.action.MYBROADCAST02";
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("REMINDERID", reminder_id);
        intent.putExtra("EXAMID", Exam_id);
        intent.putExtra("NAME",name);
        intent.putExtra("SUBJECT", subject);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);*/
    }

    void saveData(String Key,String Value){
        SharedPreferences.Editor pref = getSharedPreferences("RECORDINGYOURLIFE", 0).edit();
        pref.putString(Key , Value);
        pref.apply();
    }

    void Restart(){
        Intent i=getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    void setTheme(){
        SharedPreferences prefs = getSharedPreferences("RECORDINGYOURLIFE", 0);
        theme_this = prefs.getInt("THEME_INDEX" ,0);
        int theme;
        switch (theme_this){
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
