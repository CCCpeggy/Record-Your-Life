package com.ct.daan.recordingyourlife.Class;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.ct.daan.recordingyourlife.Table.AddTableSettingsActivity;

import java.util.Locale;

public class OthersFunction {
    CalendarFunction calendarFunction;
    public OthersFunction(){
        calendarFunction=new CalendarFunction();
    }

    public void setReminder(Context context,Cursor Reminder_cursor, int id,String title,String content) {

        Log.v("setReminder",String.format("context:%s,reminder_id:%s,title:%s,content:%s,date:%s,time:%s,type:%d"
                ,context,id,title,content,Reminder_cursor.getString(1),Reminder_cursor.getString(2),Reminder_cursor.getInt(3)));
        CalendarFunction calFunction=new CalendarFunction();
        Calendar calendar=calFunction.DateTimeTextToCalendarType(Reminder_cursor.getString(1),Reminder_cursor.getString(2));
        String BROADCAST_ACTION = "net.macdidi.broadcast01.action.MYBROADCAST01";
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("REMINDERID", id);
        intent.putExtra("TITLE",title);
        intent.putExtra("CONTENT", content);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long Reminder_type=0;
        if(Reminder_cursor.getInt(3)==0){
            alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            return;
        }
        switch (Reminder_cursor.getInt(2)){
            case 0:
                Reminder_type=AlarmManager.INTERVAL_DAY;
                break;
            case 1:
                Reminder_type=AlarmManager.INTERVAL_DAY*7;
                break;
            case 2:
                Reminder_type=AlarmManager.INTERVAL_DAY*30;
                break;
            case 3:
                Reminder_type=AlarmManager.INTERVAL_DAY*365;
                break;
        }
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() ,Reminder_type, sender);
    }

    public void setReminderByInput(Context context,Cursor Reminder_cursor,int reminder_id, int id,String name,String subject) {

        Log.v("setReminder",String.format("context:%s,reminder_id:%s,title:%s,content:%s,date:%s,name:%s,subject:%d"
                ,context,id,name,subject,Reminder_cursor.getString(1),Reminder_cursor.getString(2),Reminder_cursor.getInt(3)));
        CalendarFunction calFunction=new CalendarFunction();
        Calendar calendar=calFunction.DateTimeTextToCalendarType(Reminder_cursor.getString(1),Reminder_cursor.getString(2));
        String BROADCAST_ACTION = "net.macdidi.broadcast01.action.MYBROADCAST01";
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("REMINDERID", reminder_id);
        intent.putExtra("EXAMID", id);
        intent.putExtra("NAME",name);
        intent.putExtra("SUBJECT", subject);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    public boolean isEdittextNotEmpty(EditText et){
        if(!et.getText().toString().isEmpty()) return true;
        Log.w("EditText 為空",et.getText().toString()+"內容為空");
        return false;
    }

    public boolean isEdittextNotEmpty(EditText et,String EditName, Context context){
        if(isEdittextNotEmpty(et)) return true;
        Toast.makeText(context,EditName+"內容不可為空", Toast.LENGTH_LONG).show();
        return false;
    }
    
    public boolean isDateType(EditText et){
        if(calendarFunction.isCalendarType(et.getText().toString(),"yyyy-MM-dd")) return true;
        Log.w("EditText 日期格式錯誤",et.getText().toString()+"日期格式錯誤");
        return false;
    }

    public boolean isDateType(EditText et,String EditName, Context context){
        if(isDateType(et)) return true;
        Toast.makeText(context,EditName+"內容格式錯誤", Toast.LENGTH_LONG).show();
        return false;
    }
    
    public boolean CompareDate(EditText BeforeDate,EditText AfterDate ) {
        if(calendarFunction.CompareDate(BeforeDate.getText().toString(),AfterDate.getText().toString())) return true;
        Log.w("EditText 日期順序錯誤",String.format("Before:%s / After:%s,日期格式錯誤",BeforeDate.getText().toString(),AfterDate.getText().toString()));
        return false;
    }

    public boolean CompareDate(EditText BeforeDate,EditText AfterDate,String EditName, Context context) {
        if(!CompareDate(BeforeDate,AfterDate)){
            Toast.makeText(context,EditName+"日期不合", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean CompareTime(String BeforeTime,String AfterTime ) {
        if(calendarFunction.CompareTime(BeforeTime,AfterTime)) return true;
        Log.w("時間順序錯誤",String.format("Before:%s / After:%s,時間格式錯誤",BeforeTime,AfterTime));
        return false;
    }

    public boolean CompareTime(EditText BeforeTime,EditText AfterTime ) {
        if(calendarFunction.CompareTime(BeforeTime.getText().toString(),AfterTime.getText().toString())) return true;
        Log.w("EditText 時間順序錯誤",String.format("Before:%s / After:%s,時間格式錯誤",BeforeTime.getText().toString(),AfterTime.getText().toString()));
        return false;
    }

    public boolean CompareTime(EditText BeforeTime,EditText AfterTime,String EditName, Context context) {
        if(!CompareTime(BeforeTime,AfterTime)){
            Toast.makeText(context,EditName+"時間不合", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
