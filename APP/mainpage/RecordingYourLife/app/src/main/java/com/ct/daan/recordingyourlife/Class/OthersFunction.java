package com.ct.daan.recordingyourlife.Class;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.util.Log;

public class OthersFunction {
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




}
